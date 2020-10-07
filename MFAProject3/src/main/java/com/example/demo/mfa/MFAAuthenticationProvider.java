package com.example.demo.mfa;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.example.demo.service.AccountUserDetails;

/**
 * MFAコードで認証するための認証プロバイダ
 */
public class MFAAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		// アカウントID、パスワードで認証する。
		super.additionalAuthenticationChecks(userDetails, authentication);

		// MFAコードで認証する。
		if (authentication.getDetails() instanceof MFAWebAuthenticationDetails) {
			System.out.println("MFA認証開始");
			String secret = ((AccountUserDetails) userDetails).getUser().getSecretKey();

			// DBにSecretKeyが存在する場合のみMFAコードで認証する。
			if (!StringUtils.isEmpty(secret) && StringUtils.hasText(secret)) {
				Integer totpKey = ((MFAWebAuthenticationDetails) authentication.getDetails()).getTotpKey();

				if (totpKey != null) {
					try {
						if (!verifyCode(secret, totpKey, 0)) {
							throw new BadCredentialsException("MFA Code is not valid");
						}
					} catch (InvalidKeyException | NoSuchAlgorithmException e) {
						throw new InternalAuthenticationServiceException("MFA Code verify failed", e);
					}
					System.out.println("MFA認証");
				} else {
					throw new BadCredentialsException("totpKey is null.");
				}
			} else {
				System.out.println("SecretKeyが無いのでMFA認証なし");
			}
		}

	}

	public static boolean verifyCode(String secret, int code, int variance) throws InvalidKeyException, NoSuchAlgorithmException {
		long timeIndex = System.currentTimeMillis() / 1000 / 30;
		byte[] secretBytes = new Base32().decode(secret);

		timeIndex = 1;

		// test
		byte[] hs = calculateHmacSha1(secret,timeIndex);
		long sbin = dt(hs);

		for (int i = -variance; i <= variance; i++) {
			long c = getCode(secretBytes, timeIndex + i);
//			System.out.println(new Long(c).toString());
			if (c == code) {
				return true;
			}
		}

		return false;
	}

	public static long getCode(byte[] secret, long timeIndex) throws NoSuchAlgorithmException, InvalidKeyException {

		SecretKeySpec keySpec = new SecretKeySpec(secret, "HmacSHA1");

		byte[] timeBytes = longToBytes(timeIndex);

		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);

		byte[] signBytes = mac.doFinal(timeBytes);

		System.out.println("get:");
		for (byte signByte : signBytes) {
			System.out.printf("%02x", signByte & 0xff);
		}
		System.out.println("");

		int offset = signBytes[19] & 0xf;

		long truncatedHash = signBytes[offset] & 0x7f;
		for (int i = 1; i < 4; i++) {
			truncatedHash <<= 8;
			truncatedHash |= signBytes[offset + i] & 0xff;
		}
		System.out.println("truncatedHash get:" + truncatedHash);
		return truncatedHash %= 1000000;
	}

	public static byte[] calculateHmacSha1(String key, long cnt) throws NoSuchAlgorithmException, InvalidKeyException {

		byte[] secretBytes = new Base32().decode(key);

		SecretKeySpec keySpec = new SecretKeySpec(secretBytes, "HmacSHA1");

		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);

		byte[] signBytes = mac.doFinal(longToBytes(cnt));

		System.out.println("cal:");
		for (byte signByte : signBytes) {
			System.out.printf("%02x", signByte & 0xff);
		}
		System.out.println("");

		System.out.println("signBytes.length:" + signBytes.length);

		return signBytes;

	}

	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}

	public static long dt(byte[] hs) {
		int offset = hs[19] & 0xf;

		long truncatedHash = hs[offset] & 0x7f;
		for (int i = 1; i < 4; i++) {
			truncatedHash <<= 8;
			truncatedHash |= hs[offset + i] & 0xff;
		}
		System.out.println("truncatedHash cal:" + truncatedHash);
		return truncatedHash;
	}
}

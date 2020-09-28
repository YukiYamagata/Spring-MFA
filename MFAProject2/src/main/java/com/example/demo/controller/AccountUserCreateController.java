package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.MyUser;
import com.example.demo.service.AccountUserService;

@Controller
@RequestMapping("/account")
public class AccountUserCreateController {

	private AccountUserService accountUserService;

	@Autowired
	public AccountUserCreateController(AccountUserService accountUserService) {
		this.accountUserService = accountUserService;
	}

	@RequestMapping(method = RequestMethod.GET)
	String index(AccountUserCreateForm accountUserCreateForm) {
		return "account/create";
	}
	@RequestMapping("/create")
    String create(@Validated AccountUserCreateForm accountUserCreateForm, BindingResult result, Model model, RedirectAttributes attributes) {

        // 入力チェック
        if (result.hasErrors()) {
            return "account/create";
        }

        // Userの登録
        MyUser user = new MyUser();
        user.setUserName(accountUserCreateForm.getUserName());
        user.setPassword(new BCryptPasswordEncoder().encode(accountUserCreateForm.getPassword()));
        user.setName(accountUserCreateForm.getName());
        user.setMail(accountUserCreateForm.getMail());
        user.setRoleName("USER");

        // MFA利用時にはsecret_keyを生成する。
        String secret = null;
        if ("1".equals(accountUserCreateForm.getUseMfa())) {
            secret = createSecret();
            user.setUseMfa(true);
            user.setSecretKey(secret);
        } else {
        	user.setUseMfa(false);
        }

        // 作成したユーザを登録する。
        accountUserService.create(user);

        if (StringUtils.isNotBlank(secret)) {
            attributes.addFlashAttribute("qr", getQRBarcodeURL(user.getUserName(), "localhost", secret));
        }

        return "redirect:complete";
    }

    @GetMapping("complete")
    String complete() {
        return "account/complete";
    }

    private String createSecret() {
        byte[] buffer = new byte[10];
        new SecureRandom().nextBytes(buffer);
        String secret = new String(new Base32().encode(buffer));
        return secret;
    }

    public static String getQRBarcodeURL(String user, String host, String secret) {
        return "http://chart.googleapis.com/chart?" + getQRBarcodeURLQuery(user, host, secret);
    }

    public static String getQRBarcodeURLQuery(String user, String host, String secret) {
        try {
            return "chs=100x100&chld=M%7C0&cht=qr&chl=" + URLEncoder.encode(getQRBarcodeOtpAuthURL(user, host, secret), "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getQRBarcodeOtpAuthURL(String user, String host, String secret) {
        return String.format("otpauth://totp/%s@%s?secret=%s", user, host, secret);
    }
}

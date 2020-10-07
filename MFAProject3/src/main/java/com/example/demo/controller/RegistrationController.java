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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.MyUser;
import com.example.demo.form.RegistrationForm;
import com.example.demo.service.RegistrationUserService;

@Controller
@RequestMapping("/account")
public class RegistrationController {

	private RegistrationUserService registrationUserService;

	@Autowired
	public RegistrationController(RegistrationUserService registrationUserService) {
		this.registrationUserService = registrationUserService;
	}

	@GetMapping
	String index(RegistrationForm registrationForm) {
		return "account/registration";
	}

	@PostMapping("/register")
    String register(@Validated RegistrationForm registrationForm, BindingResult result, Model model, RedirectAttributes attributes) {

        // 入力チェック
        if (result.hasErrors()) {
            return "account/registration";
        }

        // Userの登録
        MyUser user = new MyUser();
        user.setUserName(registrationForm.getUserName());
        user.setPassword(new BCryptPasswordEncoder().encode(registrationForm.getPassword()));
        user.setName(registrationForm.getName());
        user.setRoleName("USER");

        // MFA利用時には秘密鍵を生成する。
        String secret = null;
        if ("1".equals(registrationForm.getUseMfa())) {
            secret = createSecret(); // 秘密鍵を生成
            user.setUseMfa(true);
            user.setSecretKey(secret);
        } else {
        	user.setUseMfa(false);
        }

        // 作成したユーザを登録する。
        registrationUserService.register(user);

        if (StringUtils.isNotBlank(secret)) {
            attributes.addFlashAttribute("qr", getQRCodeURL(user.getUserName(), "localhost", secret));
        }

        return "redirect:complete";
    }

    @GetMapping("/complete")
    String complete() {
        return "account/qr";
    }

    /**
     * 秘密鍵を生成する。
     * @return secretKey
     */
    private String createSecret() {
        byte[] buffer = new byte[10];
        new SecureRandom().nextBytes(buffer);
        String secret = new String(new Base32().encode(buffer));
        return secret;
    }

    public static String getQRCodeURL(String user, String host, String secret) {
        return "http://chart.googleapis.com/chart?" + getQRCodeURLQuery(user, host, secret);
    }

    public static String getQRCodeURLQuery(String user, String host, String secret) {
        try {
            return "chs=100x100&chld=M%7C0&cht=qr&chl=" + URLEncoder.encode(getQRCodeOtpAuthURL(user, host, secret), "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getQRCodeOtpAuthURL(String user, String host, String secret) {
        return String.format("otpauth://totp/%s@%s?secret=%s", user, host, secret);
    }
}

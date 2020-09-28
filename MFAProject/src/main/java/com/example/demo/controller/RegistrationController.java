//package com.example.demo.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class RegistrationController {
//
//	@GetMapping("/registrationConfirm")
//	public String confirmRegistration(@RequestParam("token") String token, Model model) {
//	    String result = userService.validateVerificationToken(token);
//	    if(result.equals("valid")) {
//	        User user = userService.getUser(token);
//	        if (user.isUsing2FA()) {
//	            model.addAttribute("qr", userService.generateQRUrl(user));
//	            return "redirect:/qrcode.html?lang=" + locale.getLanguage();
//	        }
//
//	        model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
//	        return "redirect:/login?lang=" + locale.getLanguage();
//	    }
//	}
//}

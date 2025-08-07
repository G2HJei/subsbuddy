package xyz.zlatanov.subsbuddy.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthController {

	@Value("${AUTH_KEY}")
	private String				authKey;

	private static final String	AUTH_SESSION_KEY	= "authenticated";

	@GetMapping("/login")
	public String loginPage(Model model,
			@RequestParam(value = "error", defaultValue = "#{null}") String error,
			HttpSession session) {
		// If already authenticated, redirect to home
		if (Boolean.TRUE.equals(session.getAttribute(AUTH_SESSION_KEY))) {
			return "redirect:/";
		}

		model.addAttribute("error", error);
		return "login";
	}

	@PostMapping("/login")
	public String authenticate(@RequestParam("authKey") String providedKey, HttpSession session) {
		if (authKey.equals(providedKey)) {
			session.setAttribute(AUTH_SESSION_KEY, true);
			return "redirect:/";
		} else {
			return "redirect:/login?error=Invalid authentication key";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		log.info("User logged out");
		return "redirect:/login";
	}
}
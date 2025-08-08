package xyz.zlatanov.subsbuddy.controller;

import static xyz.zlatanov.subsbuddy.controller.ErrorHandlerController.ERROR_QUERY_KEY;

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
	public String loginPage(Model model, HttpSession session,
			@RequestParam(value = ERROR_QUERY_KEY, defaultValue = "#{null}") String error) {
		if (alreadyAuthenticated(session)) {
			return "redirect:/";
		}
		model.addAttribute("error", error);
		return "login";
	}

	@PostMapping("/login")
	public String authenticate(HttpSession session, @RequestParam("authKey") String providedKey) {
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

	private static boolean alreadyAuthenticated(HttpSession session) {
		return Boolean.TRUE.equals(session.getAttribute(AUTH_SESSION_KEY));
	}
}
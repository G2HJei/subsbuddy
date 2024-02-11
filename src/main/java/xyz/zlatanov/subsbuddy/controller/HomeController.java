package xyz.zlatanov.subsbuddy.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import xyz.zlatanov.subsbuddy.service.HomeService;
import xyz.zlatanov.subsbuddy.util.WebUtils;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

	private HomeService service;

	@GetMapping
	public String home(HttpServletRequest request, Model model, @RequestParam(value = "error", defaultValue = "#{null}") String error) {
		model.addAttribute("model", service.getModel(WebUtils.getOwner(request)));
		model.addAttribute("error", error);
		return "upload";
	}

	@SneakyThrows
	@PostMapping
	public String upload(Model model, @RequestParam("srt") MultipartFile file, HttpServletRequest request) {
		service.upload(Objects.requireNonNull(file.getOriginalFilename()), file.getBytes(), WebUtils.getOwner(request));
		model.addAttribute("message", "Uploaded: " + file.getOriginalFilename());
		return "upload";
	}
}
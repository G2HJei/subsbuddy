package xyz.zlatanov.subsbuddy.controller;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.service.HomeService;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

	private HomeService service;

	@GetMapping
	public String upload(Model model) {
		// todo list existing and allow download
		return "upload";
	}

	@SneakyThrows
	@PostMapping
	public String processUpload(Model model, @RequestParam("srt") MultipartFile file) {
		service.upload(file.getOriginalFilename(), file.getBytes());
		model.addAttribute("message", "Uploaded: " + file.getOriginalFilename());
		return "upload";
	}
}
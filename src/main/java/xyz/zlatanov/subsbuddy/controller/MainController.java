package xyz.zlatanov.subsbuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class MainController {

	@GetMapping
	public String upload(Model model) {
		// todo list existing and allow download
		return "upload";
	}

	@PostMapping
	public String processUpload(Model model, @RequestParam("srt") MultipartFile file) {
		// todo store file and start translation
		model.addAttribute("message", "Uploaded: " + file.getOriginalFilename());
		return "upload";
	}
}
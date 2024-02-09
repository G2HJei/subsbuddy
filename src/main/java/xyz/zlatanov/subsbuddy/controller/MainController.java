package xyz.zlatanov.subsbuddy.controller;

import static xyz.zlatanov.subsbuddy.domain.FileType.SRT;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.command.upload.FileUploadCommand;
import xyz.zlatanov.subsbuddy.command.upload.UploadFileHandler;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class MainController {

	private UploadFileHandler uploadFileHandler;

	@GetMapping
	public String upload(Model model) {
		// todo list existing and allow download
		return "upload";
	}

	@PostMapping
	public String processUpload(Model model, @RequestParam("srt") MultipartFile file) {
		uploadFileHandler.uploadFile(new FileUploadCommand()
				.type(SRT)
				.filename(file.getOriginalFilename()));
		model.addAttribute("message", "Uploaded: " + file.getOriginalFilename());
		return "upload";
	}
}
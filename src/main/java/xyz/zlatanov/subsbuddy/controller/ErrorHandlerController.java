package xyz.zlatanov.subsbuddy.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class ErrorHandlerController {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, Model model) {
		model.addAttribute("error", "File too big. (max 1MB)");
		return "upload";
	}

	@ExceptionHandler(Exception.class)
	public String unknownException(Exception e, Model model) {
		model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
		return "upload";
	}
}

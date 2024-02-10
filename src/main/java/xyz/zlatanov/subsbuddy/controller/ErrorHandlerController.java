package xyz.zlatanov.subsbuddy.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import xyz.zlatanov.subsbuddy.exception.SubsBuddyException;

@ControllerAdvice
public class ErrorHandlerController {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(Model model) {
		model.addAttribute("error", "File too big. (max 100KB)");
		return "upload";
	}

	@ExceptionHandler(SubsBuddyException.class)
	public String nonSrtFile(SubsBuddyException e, Model model) {
		model.addAttribute("error", e.getMessage());
		return "upload";
	}

	@ExceptionHandler(Exception.class)
	public String unknownException(Exception e, Model model) {
		model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
		return "upload";
	}
}

package xyz.zlatanov.subsbuddy.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;
import xyz.zlatanov.subsbuddy.exception.SubsBuddyException;
import xyz.zlatanov.subsbuddy.util.WebUtils;

@ControllerAdvice
public class ErrorHandlerController {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(HttpServletRequest request) {
		return WebUtils.getErrorQueryParamRedirect(request, "File too big. (max 100KB)");
	}

	@ExceptionHandler(SubsBuddyException.class)
	public String nonSrtFile(HttpServletRequest request, SubsBuddyException e) {
		return WebUtils.getErrorQueryParamRedirect(request, e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public String unknownException(HttpServletRequest request, Exception e) {
		return WebUtils.getErrorQueryParamRedirect(request, "An unexpected error occurred: " + e.getMessage());
	}
}

package xyz.zlatanov.subsbuddy.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.servlet.http.HttpServletRequest;
import xyz.zlatanov.subsbuddy.core.exception.SubsBuddyException;
import xyz.zlatanov.subsbuddy.web.util.WebUtils;

@ControllerAdvice
public class ErrorHandlerController {

	public static final String ERROR_QUERY_KEY = "error";

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(HttpServletRequest request) {
		return WebUtils.getErrorQueryParamRedirect(request, ERROR_QUERY_KEY, "File too big. (max 100KB)");
	}

	@ExceptionHandler(SubsBuddyException.class)
	public String nonSrtFile(HttpServletRequest request, SubsBuddyException e) {
		return WebUtils.getErrorQueryParamRedirect(request, ERROR_QUERY_KEY, e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public String unknownException(HttpServletRequest request, Exception e) {
		return WebUtils.getErrorQueryParamRedirect(request, ERROR_QUERY_KEY, "An unexpected error occurred: " + e.getMessage());
	}
}

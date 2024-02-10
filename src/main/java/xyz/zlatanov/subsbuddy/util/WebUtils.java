package xyz.zlatanov.subsbuddy.util;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtils {

	public static String getOwner(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("x-forwarded-for"))
				.orElseGet(request::getRemoteAddr);
	}
}

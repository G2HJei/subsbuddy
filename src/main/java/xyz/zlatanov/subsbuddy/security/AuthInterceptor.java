package xyz.zlatanov.subsbuddy.security;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.val;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private static final String AUTH_SESSION_KEY = "authenticated";

	@Override
	@SneakyThrows
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
		if (isLoginResource(request) || isAlreadyAuthenticated(request)) {
			return true;
		}
		response.sendRedirect("/login");
		return false;
	}

	private static boolean isLoginResource(HttpServletRequest request) {
		val requestURI = request.getRequestURI();
		return requestURI.equals("/login") ||
				requestURI.equals("/favicon.ico") ||
				requestURI.endsWith(".css") ||
				requestURI.startsWith("/images/");
	}

	private static boolean isAlreadyAuthenticated(HttpServletRequest request) {
		val session = request.getSession(false);
		return session != null && Boolean.TRUE.equals(session.getAttribute(AUTH_SESSION_KEY));
	}
}
package xyz.zlatanov.subsbuddy.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Optional;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

public class WebUtils {

	public static String getOwner(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader("x-forwarded-for"))
				.orElseGet(request::getRemoteAddr);
	}

	public static String getErrorQueryParamRedirect(HttpServletRequest request, String msg) {
		val msgEncoded = UriUtils.encodeQueryParam(msg, UTF_8);
		val uri = ServletUriComponentsBuilder.fromContextPath(request)
				.queryParam("error", msgEncoded)
				.build();
		return "redirect:" + uri.getPath() + "?" + uri.getQuery();
	}
}

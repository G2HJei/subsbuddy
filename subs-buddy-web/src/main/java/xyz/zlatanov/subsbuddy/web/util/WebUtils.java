package xyz.zlatanov.subsbuddy.web.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = PRIVATE)
public class WebUtils {

	public static String getErrorQueryParamRedirect(HttpServletRequest request, String key, String msg) {
		val msgEncoded = UriUtils.encodeQueryParam(msg, UTF_8);
		val uri = ServletUriComponentsBuilder.fromContextPath(request)
				.queryParam(key, msgEncoded)
				.build();
		return "redirect:" + uri.getPath() + "?" + uri.getQuery();
	}
}

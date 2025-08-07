package xyz.zlatanov.subsbuddy.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import lombok.val;

class WebUtilsTest {

	@Test
	void getErrorQueryParamRedirect_message_encodes() {
		val request = new MockHttpServletRequest();
		request.setContextPath("/");
		assertEquals("redirect:/?message=Test%20message%22", WebUtils.getErrorQueryParamRedirect(request, "message", "Test message\""));
	}
}
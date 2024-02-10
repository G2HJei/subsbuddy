package xyz.zlatanov.subsbuddy.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class WebUtilsTest {

	@Test
	void getOwner_header_returns() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("x-forwarded-for", "header");
		assertEquals("header", WebUtils.getOwner(request));
	}

	@Test
	void getOwner_remoteAddr_returns() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRemoteAddr("addr");
		assertEquals("addr", WebUtils.getOwner(request));
	}
}
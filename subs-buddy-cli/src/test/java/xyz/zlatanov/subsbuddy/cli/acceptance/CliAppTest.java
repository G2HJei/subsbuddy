package xyz.zlatanov.subsbuddy.cli.acceptance;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import lombok.val;

@ShellTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CliAppTest {

	@Autowired
	ShellTestClient client;

	@Test
	void test() {
		val session = client
				.nonInterative("translate", "--input", "given.srt", "--output", "--actual.srt")
				.run();

		try {
			await().atMost(2, SECONDS).untilAsserted(() -> {
				// file named "actual.srt" exists
				// contents of "actual.srt" is the same as "expected.srt"
			});
		} catch (Exception e) {
			// clean up "actual.srt" if exists
		}
	}
}

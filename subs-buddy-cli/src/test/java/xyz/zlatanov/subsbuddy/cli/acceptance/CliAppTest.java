package xyz.zlatanov.subsbuddy.cli.acceptance;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.shell.test.ShellAssertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.AutoConfigureShell;
import org.springframework.shell.test.autoconfigure.AutoConfigureShellTestClient;
import org.springframework.test.annotation.DirtiesContext;

import lombok.SneakyThrows;
import lombok.val;

@SpringBootTest
@AutoConfigureShell
@AutoConfigureShellTestClient
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CliAppTest {

	private final String	timestamp	= String.valueOf(Instant.now().toEpochMilli());
	private final Path		given		= path("given_" + timestamp + ".srt");
	private final Path		actual		= path("actual_" + timestamp + ".srt");

	@Autowired
	ShellTestClient			client;

	@Test
	void shouldShowQuota() {
		val session = executeCommand("quota");
		assertThat(session.screen()).containsText("-1%");
	}

	@Test
	void shouldTranslateSingleFile() {
		setupInputFile();
		executeCommand("translate", "--input", unixPath(given), "--output", unixPath(actual));
		assertTranslation();
	}

	@SneakyThrows
	static Path path(String resourcePath) {
		return Paths.get(System.getProperty("java.io.tmpdir")).resolve(resourcePath);
	}

	ShellTestClient.NonInteractiveShellSession executeCommand(String... args) {
		val session = client.nonInterative(args).run();
		await().atMost(2, SECONDS).untilAsserted(session::isComplete);
		return session;
	}

	@SneakyThrows
	void setupInputFile() {
		val givenContent = """
				1
				00:00:39,467 --> 00:00:41,175
				Who is Don Draper?
				""";
		Files.writeString(given, givenContent);
	}

	static String unixPath(Path path) {
		return path.toString().replace('\\', '/');
	}

	@SneakyThrows
	void assertTranslation() {
		val actualContent = Files.readString(actual);
		String expectedContent = """
				1
				00:00:39,467 --> 00:00:41,175
				WHO IS DON DRAPER?
				""";
		assertEquals(expectedContent, actualContent);
	}

}

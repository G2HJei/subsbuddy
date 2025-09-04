package xyz.zlatanov.subsbuddy.cli.acceptance;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.shell.test.ShellAssertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import org.junit.jupiter.api.BeforeAll;
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

	static final String	TEST_DIR		= System.getProperty("java.io.tmpdir") + "/subs-buddy-" + Instant.now().toEpochMilli();
	static final String	IN_DIR_MULTI	= TEST_DIR + "-multiple";
	static final String	OUT_DIR_MULTI	= TEST_DIR + "-multiple/output";
	final String		given			= TEST_DIR + "/given.srt";
	final String		actual			= TEST_DIR + "/actual.srt";
	final String		file1			= IN_DIR_MULTI + "/file-1.srt";
	final String		file2			= IN_DIR_MULTI + "/file-2.srt";
	final String		file1Output		= OUT_DIR_MULTI + "/file-1.srt";
	final String		file2Output		= OUT_DIR_MULTI + "/file-2.srt";
	final String		givenLine1		= """
			1
			00:00:39,467 --> 00:00:41,175
			Who is Don Draper?
			""";
	final String		givenLine2		= """
			1
			00:29:50,267 --> 00:29:51,642
			Mr. Draper?
			""";

	@Autowired
	ShellTestClient		client;

	@BeforeAll
	@SneakyThrows
	static void setup() {
		Files.createDirectories(Paths.get(TEST_DIR));
	}

	@Test
	void shouldShowQuota() {
		val session = executeCommand("quota");
		assertThat(session.screen()).containsText("-1%");
	}

	@Test
	void shouldTranslateSingleFile() {
		setupInputFile();
		executeCommand("translate", "--input", unixPath(given), "--output", unixPath(actual));
		assertTranslation(givenLine1.toUpperCase(), actual);
	}

	@Test
	void shouldTranslateDirectory() {
		setupInputDirectory();
		executeCommand("translate", "--input", unixPath(IN_DIR_MULTI), "--output", unixPath(OUT_DIR_MULTI));
		assertTranslation(givenLine1.toUpperCase(), file1Output);
		assertTranslation(givenLine2.toUpperCase(), file2Output);
	}

	ShellTestClient.NonInteractiveShellSession executeCommand(String... args) {
		val session = client.nonInterative(args).run();
		await().atMost(2, SECONDS).untilAsserted(session::isComplete);
		return session;
	}

	@SneakyThrows
	void setupInputFile() {
		Files.writeString(Paths.get(given), givenLine1);
	}

	@SneakyThrows
	void setupInputDirectory() {
		Files.createDirectories(Paths.get(IN_DIR_MULTI));
		Files.createDirectories(Paths.get(OUT_DIR_MULTI));
		Files.writeString(Paths.get(file1), givenLine1);
		Files.writeString(Paths.get(file2), givenLine2);
	}

	static String unixPath(String url) {
		val path = Paths.get(url);
		return path.toString().replace('\\', '/');
	}

	@SneakyThrows
	static void assertTranslation(String expected, String actualPath) {
		val actualContent = Files.readString(Paths.get(actualPath));
		assertEquals(expected, actualContent);
	}

}

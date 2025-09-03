package xyz.zlatanov.subsbuddy.cli;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;

@Command
@RequiredArgsConstructor
@Slf4j
public class SubtitleCommands {

	private final SubsBuddyClient client;

	@Command(description = "Translate SRT subtitle file.")
	@SneakyThrows
	public String translate(
			@Option(required = true, description = "Input file path.") String input,
			@Option(required = true, description = "Output file path.") String output) {
		val inputContent = Files.readString(Path.of(input));
		val translatedContent = client.translateSrt(inputContent);
		Files.writeString(Path.of(output), translatedContent);
		return "Translation complete.";
	}

	@Command(description = "Check remaining translation API quota")
	public String quota() {
		return client.usagePercent() + "%";
	}
}
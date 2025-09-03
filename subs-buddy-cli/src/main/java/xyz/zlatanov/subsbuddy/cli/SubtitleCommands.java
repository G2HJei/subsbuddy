package xyz.zlatanov.subsbuddy.cli;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import lombok.RequiredArgsConstructor;
import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;

@Command
@RequiredArgsConstructor
public class SubtitleCommands {

	private SubsBuddyClient client;

	@Command(description = "Translate SRT subtitle file.")
	public String translate(
			@Option(required = true, description = "Input file/directory path.") String input,
			@Option(required = true, description = "Output file/directory path.") String output) {
		return "Translation complete.";
	}

	@Command(description = "Check remaining translation API quota")
	public String quota() {
		return client.usagePercent() + "%";
	}
}
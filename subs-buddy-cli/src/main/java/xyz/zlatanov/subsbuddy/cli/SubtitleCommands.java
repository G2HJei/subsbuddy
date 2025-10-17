package xyz.zlatanov.subsbuddy.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;
import xyz.zlatanov.subsbuddy.core.domain.exception.SubsBuddyException;

@Command
@RequiredArgsConstructor
@Slf4j
public class SubtitleCommands {

	private final SubsBuddyClient client;

	@Command(description = "Translate SRT subtitle file or directory.")
	@SneakyThrows
	public String translate(
			@Option(required = true, description = "Input file or directory path. Unix path syntax.") String input,
			@Option(description = "Output file or directory path. Unix path syntax. Defaults to \"<inputDirectory>/output\"") String output) {
		val inputPath = Path.of(input);
		if (Files.isDirectory(inputPath)) {
			translateDirectory(inputPath, outputDir(input, output));
		} else {
			translateFile(inputPath, outputFile(input, output));
		}
		return "Translation complete.";

	}

	@SneakyThrows
	private void translateFile(Path inputPath, Path outputPath) {
		log.info("Translating \"{}\"...\n", inputPath.getFileName().toString());
		val inputContent = Files.readString(inputPath);
		val translatedContent = client.translateSrt(inputContent);
		Files.writeString(outputPath, translatedContent);
	}

	@SneakyThrows
	private void translateDirectory(Path inputDir, Path outputDir) {
		log.info("Translating directory \"{}\"\n", inputDir.toString());
		val srtFiles = listSrtFile(inputDir);
		log.info("{} files found.\n", srtFiles.size());
		Files.createDirectories(outputDir);
		for (val srtFile : srtFiles) {
			val fileName = srtFile.getFileName().toString();
			val outputFile = outputDir.resolve(fileName);
			translateFile(srtFile, outputFile);
		}
	}

	@SneakyThrows
	private List<Path> listSrtFile(Path path) {
		try (val files = Files.list(path)) {
			val srtFiles = files
					.filter(p -> p.toString().toLowerCase().endsWith(".srt"))
					.toList();
			if (srtFiles.isEmpty()) {
				throw new SubsBuddyException("No .srt files found in the input directory.");
			}
			return srtFiles;
		}
	}

	@Command(description = "Check remaining translation API quota")
	public String quota() {
		return client.usagePercent() + "%";
	}

	private static Path outputFile(String input, String output) {
		return Path.of(Optional.ofNullable(output)
				.orElse(input.substring(0, input.length() - 4) + "-translated.srt"));
	}

	private static Path outputDir(String input, String output) {
		return Path.of(Optional.ofNullable(output)
				.orElse(input + "/output"));
	}
}
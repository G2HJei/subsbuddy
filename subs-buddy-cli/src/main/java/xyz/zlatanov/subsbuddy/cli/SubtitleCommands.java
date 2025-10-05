package xyz.zlatanov.subsbuddy.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
			@Option(required = true, description = "Input file or directory path.") String input,
			@Option(required = true, description = "Output file or directory path.") String output) {
		val inputPath = Path.of(input);
		val outputPath = Path.of(output);
		if (Files.isDirectory(inputPath)) {
			translateDirectory(inputPath, outputPath);
		} else {
			translateFile(inputPath, outputPath);
		}
		return "Translation complete.";

	}

	@SneakyThrows
	private void translateFile(Path inputPath, Path outputPath) {
		log.info("Translating {}...", inputPath.getFileName().toString());
		val inputContent = Files.readString(inputPath);
		val translatedContent = client.translateSrt(inputContent);
		Files.writeString(outputPath, translatedContent);
	}

	@SneakyThrows
	private void translateDirectory(Path inputDir, Path outputDir) {
		log.info("Translating directory {}", inputDir.toString());
		val srtFiles = listSrtFile(inputDir);
		log.info("{} files found.", srtFiles.size());
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
}
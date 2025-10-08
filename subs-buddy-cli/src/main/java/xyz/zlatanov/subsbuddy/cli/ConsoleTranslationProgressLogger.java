package xyz.zlatanov.subsbuddy.cli;

import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.core.client.translatetext.log.TranslationProgressLogger;

@Slf4j
public class ConsoleTranslationProgressLogger implements TranslationProgressLogger {

	@Override
	public int progressPercentStep() {
		return 5;
	}

	@Override
	public void start() {
		log.info("0%              100%\n");
	}

	@Override
	public void progress(int percent) {
		log.info("|");
	}

	@Override
	public void end() {
		log.info("|    ✓\n");
	}
}

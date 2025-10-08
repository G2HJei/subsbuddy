package xyz.zlatanov.subsbuddy.core.client.translatetext.log;

public interface TranslationProgressLogger {

	default int progressPercentStep() {
		return 10;
	}

	default void start() {

	}

	default void progress(int percent) {
	}

	default void end() {
	}

}

package xyz.zlatanov.subsbuddy.exception;

public class TranslationException extends SubsBuddyException {

	public TranslationException() {
		super("An error occurred during translation.");
	}

	public TranslationException(String message) {
		super(message);
	}
}

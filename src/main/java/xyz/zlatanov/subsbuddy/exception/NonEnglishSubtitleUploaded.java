package xyz.zlatanov.subsbuddy.exception;

public class NonEnglishSubtitleUploaded extends SubsBuddyException {

	public NonEnglishSubtitleUploaded() {
		super("Unsupported language detected. Supported languages: English.");
	}
}

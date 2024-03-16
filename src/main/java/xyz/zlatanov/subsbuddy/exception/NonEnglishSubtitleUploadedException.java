package xyz.zlatanov.subsbuddy.exception;

public class NonEnglishSubtitleUploadedException extends SubsBuddyException {

	public NonEnglishSubtitleUploadedException() {
		super("Unsupported language detected. Supported languages: English.");
	}
}

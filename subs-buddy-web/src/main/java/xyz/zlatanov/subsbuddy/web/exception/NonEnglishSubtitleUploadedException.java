package xyz.zlatanov.subsbuddy.web.exception;

import xyz.zlatanov.subsbuddy.core.exception.SubsBuddyException;

public class NonEnglishSubtitleUploadedException extends SubsBuddyException {

	public NonEnglishSubtitleUploadedException() {
		super("Unsupported language detected. Supported languages: English.");
	}
}

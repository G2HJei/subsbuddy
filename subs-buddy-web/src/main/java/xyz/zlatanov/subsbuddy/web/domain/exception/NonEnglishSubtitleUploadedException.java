package xyz.zlatanov.subsbuddy.web.domain.exception;

import xyz.zlatanov.subsbuddy.core.domain.exception.SubsBuddyException;

public class NonEnglishSubtitleUploadedException extends SubsBuddyException {

	public NonEnglishSubtitleUploadedException() {
		super("Unsupported language detected. Supported languages: English.");
	}
}

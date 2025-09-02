package xyz.zlatanov.subsbuddy.web.domain.exception;

import xyz.zlatanov.subsbuddy.core.domain.exception.SubsBuddyException;

public class NotSupportedFileTypeException extends SubsBuddyException {

	public NotSupportedFileTypeException() {
		super("Please select .srt file.");
	}
}

package xyz.zlatanov.subsbuddy.web.exception;

import xyz.zlatanov.subsbuddy.core.exception.SubsBuddyException;

public class NotSupportedFileTypeException extends SubsBuddyException {

	public NotSupportedFileTypeException() {
		super("Please select .srt file.");
	}
}

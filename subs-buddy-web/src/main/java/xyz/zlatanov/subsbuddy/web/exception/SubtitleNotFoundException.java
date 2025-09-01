package xyz.zlatanov.subsbuddy.web.exception;

import xyz.zlatanov.subsbuddy.core.exception.SubsBuddyException;

public class SubtitleNotFoundException extends SubsBuddyException {

	public SubtitleNotFoundException() {
		super("The requested file could not be found.");
	}
}

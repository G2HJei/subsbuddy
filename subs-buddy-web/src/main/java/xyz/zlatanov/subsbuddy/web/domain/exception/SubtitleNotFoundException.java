package xyz.zlatanov.subsbuddy.web.domain.exception;

import xyz.zlatanov.subsbuddy.core.domain.exception.SubsBuddyException;

public class SubtitleNotFoundException extends SubsBuddyException {

	public SubtitleNotFoundException() {
		super("The requested file could not be found.");
	}
}

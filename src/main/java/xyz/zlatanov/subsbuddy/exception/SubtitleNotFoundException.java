package xyz.zlatanov.subsbuddy.exception;

public class SubtitleNotFoundException extends SubsBuddyException {

	public SubtitleNotFoundException() {
		super("The requested file could not be found.");
	}
}

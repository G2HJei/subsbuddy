package xyz.zlatanov.subsbuddy.exception;

public class AlreadyUploadedException extends SubsBuddyException {

	public AlreadyUploadedException() {
		super("You have already uploaded this file. Please check its translation status.");
	}
}

package xyz.zlatanov.subsbuddy.exception;

public class AlreadyUploaded extends SubsBuddyException {

	public AlreadyUploaded() {
		super("You have already uploaded this file. Please check its translation status.");
	}
}

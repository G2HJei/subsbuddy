package xyz.zlatanov.subsbuddy.exception;

public class NotSupportedFileTypeException extends SubsBuddyException{
    public NotSupportedFileTypeException() {
        super("Please select .srt file.");
    }
}

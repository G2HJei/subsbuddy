package xyz.zlatanov.subsbuddy.exception;

public class NotSupportedFileType extends SubsBuddyException{
    public NotSupportedFileType() {
        super("Please select .srt file.");
    }
}

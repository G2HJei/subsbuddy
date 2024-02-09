package xyz.zlatanov.subsbuddy.exception;

public class NotSupportedFileType extends RuntimeException{
    public NotSupportedFileType() {
        super("Please select .srt file.");
    }
}

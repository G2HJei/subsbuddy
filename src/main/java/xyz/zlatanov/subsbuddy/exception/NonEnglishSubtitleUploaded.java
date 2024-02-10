package xyz.zlatanov.subsbuddy.exception;

public class NonEnglishSubtitleUploaded extends RuntimeException{
    public NonEnglishSubtitleUploaded() {
        super("Unsupported language detected. Supported languages: English.");
    }
}

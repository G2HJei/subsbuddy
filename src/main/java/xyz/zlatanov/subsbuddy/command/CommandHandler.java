package xyz.zlatanov.subsbuddy.command;

public interface CommandHandler<T> {

	void execute(T command);
}

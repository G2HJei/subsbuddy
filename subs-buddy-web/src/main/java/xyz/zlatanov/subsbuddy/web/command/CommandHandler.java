package xyz.zlatanov.subsbuddy.web.command;

public interface CommandHandler<T> {

	void execute(T command);
}

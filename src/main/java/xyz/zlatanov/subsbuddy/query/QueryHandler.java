package xyz.zlatanov.subsbuddy.query;

public interface QueryHandler<T, S> {

	T execute(S query);
}

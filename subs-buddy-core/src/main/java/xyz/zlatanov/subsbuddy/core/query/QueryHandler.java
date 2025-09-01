package xyz.zlatanov.subsbuddy.core.query;

public interface QueryHandler<T, S> {

	T execute(S query);
}

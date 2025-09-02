package xyz.zlatanov.subsbuddy.web.query;

public interface QueryHandler<T, S> {

	T execute(S query);
}

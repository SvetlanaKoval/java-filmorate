package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Storage<T> {

    protected long idGenerator = 0;
    protected final Map<Long, T> storage = new HashMap<>();

    public abstract Collection<T> getAll();

    public abstract T create(T t);

    public abstract T update(T t);

    public abstract T delete(T t);

    public abstract T getById(Long id);

}

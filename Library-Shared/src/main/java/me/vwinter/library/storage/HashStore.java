package me.vwinter.library.storage;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HashStore<T extends Storable> implements Store<T> {

    protected final Map<UUID, T> map;

    public HashStore() {
        this.map = Maps.newConcurrentMap();
    }

    @Override
    public T getById(final UUID uniqueId) {
        return map.get(uniqueId);
    }

    @Override
    public void add(final T t) {
        if (t == null) {
            throw new RuntimeException("Could not add null reference to the store.");
        }
        map.put(t.getUniqueId(), t);
    }

    @Override
    public void remove(final T t) {
        if (t == null) {
            throw new RuntimeException("Could not remove null reference from the store.");
        }

        map.remove(t.getUniqueId());
    }

    @Override
    public void remove(final UUID uniqueId) {
        if (uniqueId == null) {
            throw new RuntimeException("Could not remove null reference from the store.");
        }

        map.remove(uniqueId);
    }

    @Override
    public boolean isPresent(final T t) {
        return map.containsKey(t.getUniqueId());
    }

    @Override
    public boolean isPresent(final UUID uniqueId) {
        return map.containsKey(uniqueId);
    }

    @Override
    public boolean isPresent(final T t, final Consumer<? super T> consumer) {
        boolean contains = isPresent(t);

        if (contains) {
            consumer.accept(t);
        }

        return contains;
    }

    @Override
    public boolean isPresent(final UUID uniqueId, final Consumer<? super T> consumer) {
        T t = map.get(uniqueId);

        if (t != null) {
            consumer.accept(t);
        }

        return t != null;
    }

    @Override
    public Stream<T> stream() {
        return map.values().stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return map.values().parallelStream();
    }

    @Override
    public Iterator<T> iterator() {
        return map.values().iterator();
    }
}

package me.vwinter.library.storage.named;

import me.vwinter.library.storage.HashStore;
import me.vwinter.library.storage.Storable;

import java.util.function.Consumer;

public class NamedHashStore<T extends Storable & Nameable> extends HashStore<T> implements NamedStore<T> {

    @Override
    public T getByName(final String name) {
        for (final T t : this) {
            if (t.getName().equals(name)) {
                return t;
            }
        }

        return null;
    }

    @Override
    public T getByNameIgnoreCase(final String name) {
        for (final T t : this) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }

        return null;
    }

    @Override
    public boolean isPresent(final String name) {
        return getByName(name) != null;
    }

    @Override
    public boolean isPresentIgnoreCase(final String name) {
        return getByNameIgnoreCase(name) != null;
    }

    @Override
    public boolean isPresent(final String name, final Consumer<? super T> consumer) {
        final T t = getByName(name);

        if (t != null) {
            consumer.accept(t);
        }

        return t != null;
    }

    @Override
    public boolean isPresentIgnoreCase(final String name, final Consumer<? super T> consumer) {
        final T t = getByNameIgnoreCase(name);

        if (t != null) {
            consumer.accept(t);
        }

        return t != null;
    }
}

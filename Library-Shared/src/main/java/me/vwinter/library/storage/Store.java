package me.vwinter.library.storage;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Store<T extends Storable> extends Iterable<T> {

    /**
     * Returns an object by it's {@link UUID} unique id.
     */
    T getById(final UUID uniqueId);

    /**
     * Add an object to the store.
     */
    void add(final T t);

    /**
     * Remove an object from the store.
     */
    void remove(final T t);

    /**
     * Remove an object from the store by it's {@link UUID} unique id.
     */
    void remove(final UUID uniqueId);

    /**
     * Returns a boolean whether the object is present in the store or not.
     */
    boolean isPresent(final T t);

    /**
     * Returns a boolean whether an object with the {@link UUID} unique id, is present in the store or not.
     */
    boolean isPresent(final UUID uniqueId);

    /**
     * Returns a boolean whether the object is present in the store or not. If it is, the consumer is accepted.
     */
    boolean isPresent(final T t, Consumer<? super T> consumer);

    /**
     * Returns a boolean whether an object with the {@link UUID} unique id, is present in the store or not. If it is, the consumer is accepted.
     */
    boolean isPresent(final UUID uniqueId, Consumer<? super T> consumer);

    /**
     * Returns an {@link Stream} of objects stored in this store.
     */
    Stream<T> stream();

    /**
     * Returns a parallel {@link Stream} of objects stored in this store.
     */
    Stream<T> parallelStream();

}

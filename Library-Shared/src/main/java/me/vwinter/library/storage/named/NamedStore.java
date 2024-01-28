package me.vwinter.library.storage.named;

import me.vwinter.library.storage.Storable;
import me.vwinter.library.storage.Store;

import java.util.function.Consumer;

public interface NamedStore<T extends Storable & Nameable> extends Store<T> {

    /**
     * Returns an object with the name.
     */
    T getByName(final String name);

    /**
     * Returns an object with the name, ignoring case. See {@link String#equalsIgnoreCase(String)}
     */
    T getByNameIgnoreCase(final String name);

    /**
     * Returns a boolean whether an object with the name is present in the store or not.
     */
    boolean isPresent(final String name);

    /**
     * Returns a boolean whether an object with the name, ignoring case, is present in the store or not.
     */
    boolean isPresentIgnoreCase(final String name);

    /**
     * Returns a boolean whether an object with the name is present in the store or not. If it is, the consumer is accepted.
     */
    boolean isPresent(final String name, final Consumer<? super T> consumer);

    /**
     * Returns a boolean whether an object with the name, ignoring case, is present in the store or not. If it is, the consumer is accepted.
     */
    boolean isPresentIgnoreCase(final String name, final Consumer<? super T> consumer);

}

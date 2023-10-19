package me.vwinter.library.storage;

import java.util.UUID;

public interface Storable {

    /**
     * Returns the unique id for this object.
     */
    UUID getUniqueId();

}

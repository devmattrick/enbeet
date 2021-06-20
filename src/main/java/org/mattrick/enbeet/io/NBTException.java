package org.mattrick.enbeet.io;

import java.io.IOException;

/**
 * An exception has occurred in relation to NBT parsing.
 */
public class NBTException extends IOException {

    /**
     * Create a new NBTException.
     * @param message The message to include with the exception.
     */
    public NBTException(String message) {
        super(message);
    }

}

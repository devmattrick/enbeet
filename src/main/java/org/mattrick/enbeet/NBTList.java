package org.mattrick.enbeet;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents an NBT ListTag, which contains one type of NBT Tag.
 */
public class NBTList extends ArrayList<Object> {

    private final TagType type;

    /**
     * Create a new NBTList of the given type.
     * @param type The type of the NBTList.
     */
    public NBTList(TagType type) {
        this.type = type;
    }

    /**
     * Get the type of this NBTList.
     * @return The type of this NBTList.
     */
    public TagType getType() {
        return type;
    }

    /**
     * Get the Object at the given index.
     * @param index The index to get.
     * @return An Optional, containing an Object at the index.
     */
    public Optional<Object> get(int index) {
        return Optional.of(super.get(index));
    }


    /**
     * Get the Object of the given type at the given index.
     * @param type The type of the object.
     * @param index The index to get.
     * @param <T> The type of the object to get.
     * @return An Optional, containing an Object of type T at the index. Will be empty if the type doesn't match.
     */
    public <T> Optional<T> get(Class<T> type, int index) {
        if (this.type != TagType.from(type)) {
            return Optional.empty();
        }

        return Optional.of(super.get(index)).map((obj) -> {
            try {
                return type.cast(obj);
            } catch (ClassCastException ignored) {}

            return null;
        });
    }

    /**
     * Get the Byte at the given index.
     * @param index The index to get.
     * @return An Optional, containing the Byte at the index. Will be empty if the type doesn't match.
     */
    public Optional<Byte> getByte(int index) {
        return get(Byte.class, index);
    }

    /**
     * Get the Short at the given index.
     * @param index The index to get.
     * @return An Optional, containing the Short at the index. Will be empty if the type doesn't match.
     */
    public Optional<Short> getShort(int index) {
        return get(Short.class, index);
    }

    /**
     * Get the Integer at the given index.
     * @param index The index to get.
     * @return An Optional, containing the Integer at the index. Will be empty if the type doesn't match.
     */
    public Optional<Integer> getInt(int index) {
        return get(Integer.class, index);
    }

    /**
     * Get the Long at the given index.
     * @param index The index to get.
     * @return An Optional, containing the Long at the index. Will be empty if the type doesn't match.
     */
    public Optional<Long> getLong(int index) {
        return get(Long.class, index);
    }

    /**
     * Get the Float at the given index.
     * @param index The index to get.
     * @return An Optional, containing the Float at the index. Will be empty if the type doesn't match.
     */
    public Optional<Float> getFloat(int index) {
        return get(Float.class, index);
    }

    /**
     * Get the Double at the given index.
     * @param index The index to get.
     * @return An Optional, containing the Double at the index. Will be empty if the type doesn't match.
     */
    public Optional<Double> getDouble(int index) {
        return get(Double.class, index);
    }

    /**
     * Get the byte[] at the given index.
     * @param index The index to get.
     * @return An Optional, containing the byte[] at the index. Will be empty if the type doesn't match.
     */
    public Optional<byte[]> getByteArray(int index) {
        return get(byte[].class, index);
    }

    /**
     * Get the String at the given index.
     * @param index The index to get.
     * @return An Optional, containing the String at the index. Will be empty if the type doesn't match.
     */
    public Optional<String> getString(int index) {
        return get(String.class, index);
    }

    /**
     * Get the NBTList at the given index.
     * @param index The index to get.
     * @return An Optional, containing the NBTList at the index. Will be empty if the type doesn't match.
     */
    public Optional<NBTList> getList(int index) {
        return get(NBTList.class, index);
    }

    /**
     * Get the NBTCompound at the given index.
     * @param index The index to get.
     * @return An Optional, containing the NBTCompound at the index. Will be empty if the type doesn't match.
     */
    public Optional<NBTCompound> getCompound(int index) {
        return get(NBTCompound.class, index);
    }

    /**
     * Get the int[] at the given index.
     * @param index The index to get.
     * @return An Optional, containing the int[] at the index. Will be empty if the type doesn't match.
     */
    public Optional<int[]> getIntArray(int index) {
        return get(int[].class, index);
    }

    /**
     * Get the long[] at the given index.
     * @param index The index to get.
     * @return An Optional, containing the long[] at the index. Will be empty if the type doesn't match.
     */
    public Optional<long[]> getLongArray(int index) {
        return get(long[].class, index);
    }

}

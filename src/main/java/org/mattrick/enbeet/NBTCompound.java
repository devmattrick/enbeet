package org.mattrick.enbeet;

import java.util.*;

/**
 * A Minecraft NBT Compound.
 */
public class NBTCompound {

    private String name;
    private final Map<String, Object> data = new HashMap<>();

    /**
     * Create a new NBTCompound.
     */
    public NBTCompound() {
        this(null);
    }

    /**
     * Create a new NBTCompound with the given name in its root.
     */
    public NBTCompound(String name) {
        this.name = name;
    }

    /**
     * Get the name of this NBTCompound ("named tag"), if it exists.
     * @return An Optional, containing the name of this tag if it exists.
     */
    public Optional<String> getName() {
        return Optional.of(name);
    }

    /**
     * Set the name of this NBTCompound ("named tag").
     * @param name Set the name of this NBTCompound.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set a value in the NBTCompound.
     * @param value The value to set.
     * @param path The path to set to the value.
     */
    public void set(Object value, String... path) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be blank");
        }

        NBTCompound curr = this;

        // Loop through every part except the last (since the last part will contain the value itself)
        for (int i = 0; i < path.length - 1; i++) {
            String part = path[i];

            // Get the existing value of the current part
            Object currVal = curr.data.get(part);
            // Either get the existing NBTCompound or create a new one to replace it with...
            NBTCompound newVal =
                    currVal instanceof NBTCompound ? (NBTCompound) currVal : new NBTCompound();
            // Put the new (or existing) NBTCompound in the current NBTCompound
            curr.data.put(part, newVal);
            // Finally, update the curr variable to track our new NBTCompound
            curr = newVal;
        }

        // For the last path part, put the passed value object
        curr.data.put(path[path.length - 1], value);
    }

    /**
     * Get the value at the given path.
     * @param path The path to get.
     * @return An Optional containing the value at the path if it exists.
     */
    public Optional<Object> get(String... path) {
        if (path == null) {
            return Optional.of(this);
        }

        Object curr = this;

        for (String part : path) {
            // If there's still a part left, and the current value is not a compound then this value
            // doesn't exist
            if (!(curr instanceof NBTCompound comp)) {
                return Optional.empty();
            }

            // Otherwise, we get that part from the internal data map of the object, check if it
            // exists, and
            if (!comp.data.containsKey(part)) {
                return Optional.empty();
            }
            curr = comp.data.get(part);
        }

        return Optional.of(curr);
    }

    /**
     * Get the value of the given type at the given path.
     * @param type The type to get.
     * @param path The path to get.
     * @param <T> The type of the object at the path.
     * @return An Optional containing the value at the path if it exists and matches the provided type.
     */
    public <T> Optional<T> get(Class<T> type, String... path) {
        return get(path).map(obj -> {
            try {
                return type.cast(obj);
            } catch (ClassCastException ignored) {
            }

            return null;
        });
    }

    /**
     * Get a Byte at the given path.
     * @param path The path to get.
     * @return An Optional containing the Byte at the path if it exists.
     */
    public Optional<Byte> getByte(String... path) {
        return get(Byte.class, path);
    }

    /**
     * Get a Short at the given path.
     * @param path The path to get.
     * @return An Optional containing the Short at the path if it exists.
     */
    public Optional<Short> getShort(String... path) {
        return get(Short.class, path);
    }

    /**
     * Get a Integer at the given path.
     * @param path The path to get.
     * @return An Optional containing the Integer at the path if it exists.
     */
    public Optional<Integer> getInt(String... path) {
        return get(Integer.class, path);
    }

    /**
     * Get a Long at the given path.
     * @param path The path to get.
     * @return An Optional containing the Long at the path if it exists.
     */
    public Optional<Long> getLong(String... path) {
        return get(Long.class, path);
    }

    /**
     * Get a Float at the given path.
     * @param path The path to get.
     * @return An Optional containing the Float at the path if it exists.
     */
    public Optional<Float> getFloat(String... path) {
        return get(Float.class, path);
    }

    /**
     * Get a Double at the given path.
     * @param path The path to get.
     * @return An Optional containing the Double at the path if it exists.
     */
    public Optional<Double> getDouble(String... path) {
        return get(Double.class, path);
    }

    /**
     * Get a byte[] at the given path.
     * @param path The path to get.
     * @return An Optional containing the byte[] at the path if it exists.
     */
    public Optional<byte[]> getByteArray(String... path) {
        return get(byte[].class, path);
    }

    /**
     * Get a String at the given path.
     * @param path The path to get.
     * @return An Optional containing the String at the path if it exists.
     */
    public Optional<String> getString(String... path) {
        return get(String.class, path);
    }

    /**
     * Get a NBTList at the given path.
     * @param path The path to get.
     * @return An Optional containing the NBTList at the path if it exists.
     */
    public Optional<NBTList> getList(String... path) {
        return get(NBTList.class, path);
    }

    /**
     * Get a NBTCompound at the given path.
     * @param path The path to get.
     * @return An Optional containing the NBTCompound at the path if it exists.
     */
    public Optional<NBTCompound> getCompound(String... path) {
        return get(NBTCompound.class, path);
    }

    /**
     * Get a int[] at the given path.
     * @param path The path to get.
     * @return An Optional containing the int[] at the path if it exists.
     */
    public Optional<int[]> getIntArray(String... path) {
        return get(int[].class, path);
    }

    /**
     * Get a long[] at the given path.
     * @param path The path to get.
     * @return An Optional containing the long[] at the path if it exists.
     */
    public Optional<long[]> getLongArray(String... path) {
        return get(long[].class, path);
    }

    /**
     * Get a int[] (which was coded as a varint array) at the given path.
     * @param path The path to get.
     * @return An Optional containing the varint array at the path if it exists.
     */
    public Optional<int[]> getVarIntArray(String... path) {
        Optional<byte[]> optionalData = getByteArray(path);

        if (optionalData.isEmpty()) {
            return Optional.empty();
        }

        List<Integer> result = new ArrayList<>();
        byte[] data = optionalData.get();
        int index = 0;
        int i = 0;
        int value;
        int varint_length;
        while (i < data.length) {
            value = 0;
            varint_length = 0;

            while(true) {
                value |= (data[i] & 127) << (varint_length++ * 7);
                if (varint_length > 5) {
                    throw new RuntimeException("VarInt too big (probably due to corrupted data)");
                }

                if ((data[i] & 128) != 128) {
                    i++;
                    break;
                }
                i++;
            }

            result.add(index, value);
            index++;
        }

        return Optional.of(result.stream().mapToInt(val -> val).toArray());
    }

    /**
     * Get the backing Map of the NBTCompound.
     * @return The backing Map.
     */
    public Map<String, Object> data() {
        return data;
    }

    @Override
    public String toString() {
        // TODO Output valid SNBT
        return "NBTCompound{" + data + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NBTCompound that = (NBTCompound) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

}

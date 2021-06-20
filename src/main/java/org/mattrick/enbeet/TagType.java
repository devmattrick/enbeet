package org.mattrick.enbeet;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type of a given NBT tag.
 */
public enum TagType {
    END(0, void.class),
    BYTE(1, byte.class),
    SHORT(2, short.class),
    INT(3, int.class),
    LONG(4, long.class),
    FLOAT(5, float.class),
    DOUBLE(6, double.class),
    BYTE_ARRAY(7, byte[].class),
    STRING(8, String.class),
    LIST(9, NBTList.class),
    COMPOUND(10, NBTCompound.class),
    INT_ARRAY(11, int[].class),
    LONG_ARRAY(12, long[].class);

    private static final Map<Byte, TagType> ID_MAP = Stream.of(values())
            .collect(Collectors.toMap(TagType::getId, Function.identity()));

    private static final Map<Class<?>, TagType> TYPE_MAP = Stream.of(values())
            .collect(Collectors.toMap(TagType::getType, Function.identity()));

    private final byte id;
    private final Class<?> type;

    TagType(int id, Class<?> type) {
        this.id = (byte) id;
        this.type = type;
    }

    /**
     * Get the numerical ID of the tag.
     *
     * @return The byte ID of the tag.
     */
    public byte getId() {
        return this.id;
    }

    public Class<?> getType() {
        return this.type;
    }

    /**
     * Get the tag from its ID.
     *
     * @param id The byte ID of the tag.
     * @return The tag.
     */
    public static TagType from(byte id) {
        return ID_MAP.get(id);
    }

    public static TagType from(Class<?> type) {
        type = unboxType(type);
        return TYPE_MAP.get(type);
    }

    private static Class<?> unboxType(Class<?> type) {
        if (type == Void.class) {
            return void.class;
        }
        if (type == Byte.class) {
            return byte.class;
        }
        if (type == Short.class) {
            return short.class;
        }
        if (type == Integer.class) {
            return Integer.class;
        }
        if (type == Long.class) {
            return long.class;
        }
        if (type == Float.class) {
            return float.class;
        }
        if (type == Double.class) {
            return double.class;
        }

        return type;
    }

}

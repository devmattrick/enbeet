package org.mattrick.enbeet.io;

import org.mattrick.enbeet.NBTCompound;
import org.mattrick.enbeet.NBTList;
import org.mattrick.enbeet.TagType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

/**
 * Reads an NBTCompound from an InputStream.
 *
 * Automatically decompresses data that is gzipped.
 */
public class NBTReader {

    private final DataInputStream in;

    /**
     * Creates an NBTReader using the specified InputStream. Will attempt to ungzip compressed data, otherwise it will
     * not perform any decompression.
     * @param in The InputStream.
     * @throws IOException if there was an error ungzipping the data.
     */
    public NBTReader(InputStream in) throws IOException {
        this.in = new DataInputStream(conditionallyUngzip(in));
    }

    /**
     * Read the NBTCompound from the InputStream.
     * @return The NBT Compound that was read.
     * @throws IOException if there was an issue with reading the NBTCompound.
     */
    public NBTCompound read() throws IOException {
        TagType type = readType();

        if (type == TagType.END) {
            return new NBTCompound();
        }

        if (type != TagType.COMPOUND) {
            throw new IOException("Expected COMPOUND at root, instead got " + type.name() + ".");
        }

        // Read and ignore the name portion of the "named tag"
        in.readUTF();

        return readCompound();
    }

    private TagType readType() throws IOException {
        byte id = in.readByte();
        TagType type = TagType.from(id);

        if (type == null) {
            throw new NBTException("Invalid NBT tag type id: " + id);
        }

        return type;
    }

    private NBTCompound readCompound() throws IOException {
        NBTCompound comp = new NBTCompound();

        TagType type = readType();
        while (type != TagType.END) {
            String key = in.readUTF();
            Object value = readTag(type);

            comp.set(value, key);

            type = readType();
        }

        return comp;
    }

    private Object readTag(TagType type) throws IOException {
        return switch (type) {
            case END -> null;
            case BYTE -> in.readByte();
            case SHORT -> in.readShort();
            case INT -> in.readInt();
            case LONG -> in.readLong();
            case FLOAT -> in.readFloat();
            case DOUBLE -> in.readDouble();
            case BYTE_ARRAY -> readByteArray();
            case STRING -> in.readUTF();
            case LIST -> readList();
            case COMPOUND -> readCompound();
            case INT_ARRAY -> readIntArray();
            case LONG_ARRAY -> readLongArray();
        };
    }

    private byte[] readByteArray() throws IOException {
        int len = in.readInt();
        byte[] value = new byte[len];

        for (int i = 0; i < len; i++) {
            value[i] = in.readByte();
        }

        return value;
    }

    private NBTList readList() throws IOException {
        TagType listType = readType();

        int len = in.readInt();
        if (len < 0) {
            len = 0;
        }

        NBTList list = new NBTList(listType);
        for (int i = 0; i < len; i++) {
            list.add(readTag(listType));
        }
        return list;
    }

    private int[] readIntArray() throws IOException {
        int len = in.readInt();
        int[] value = new int[len];

        for (int i = 0; i < len; i++) {
            value[i] = in.readInt();
        }

        return value;
    }

    private long[] readLongArray() throws IOException {
        int len = in.readInt();
        long[] value = new long[len];

        for (int i = 0; i < len; i++) {
            value[i] = in.readLong();
        }

        return value;
    }

    private InputStream conditionallyUngzip(InputStream in) throws IOException {
        PushbackInputStream pushback = new PushbackInputStream(in, 2);
        byte[] bytes = new byte[2];
        int len = pushback.read(bytes);
        pushback.unread(bytes, 0, len);

        int magic = ((bytes[1] << 8) & 0xff00) | bytes[0];
        if (magic == GZIPInputStream.GZIP_MAGIC) {
            return new GZIPInputStream(pushback);
        }

        return pushback;
    }

}

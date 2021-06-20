package org.mattrick.enbeet.io;

import org.mattrick.enbeet.NBTCompound;
import org.mattrick.enbeet.NBTList;
import org.mattrick.enbeet.TagType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public class NBTWriter {

    private final DataOutputStream out;

    /**
     * Create a new NBTWriter with the given output stream. Will apply Gzip compression by default.
     * @param out The OutputStream to write to.
     * @throws IOException if there was an issue creating the GzipOutputStream.
     */
    public NBTWriter(OutputStream out) throws IOException {
        this(out, true);
    }

    /**
     * Create a new NBTWriter, optionally applying Gzip compression.
     * @param out The OutputStream to write to.
     * @param gzip Should this NBTWriter apply Gzip compression.
     * @throws IOException if there was an issue creating the GzipOutputStream.
     */
    public NBTWriter(OutputStream out, boolean gzip) throws IOException {
        Objects.requireNonNull(out);

        if (gzip) {
            out = new GZIPOutputStream(out);
        }

        this.out = new DataOutputStream(out);
    }

    /**
     * Writes the given NBTCompound to the given OutputStream.
     * @param nbt The NBTCompound to write.
     * @throws IOException if there was an issue writing to the OutputStream.
     */
    public void write(NBTCompound nbt) throws IOException {
        Objects.requireNonNull(nbt);

        writeTagId(TagType.COMPOUND);
        out.writeUTF(nbt.getName().orElse(""));
        writeCompound(nbt);
    }

    private void writeTagId(TagType type) throws IOException {
        out.writeByte(type.getId());
    }

    private void writeTag(String key, Object value) throws IOException {
        TagType type = TagType.from(value.getClass());

        if (type == null) {
            throw new NBTException("Cannot write " + value.getClass().getName() + " object to NBT");
        }

        writeTagId(type);
        out.writeUTF(key);
        writeValue(type, value);
    }

    private void writeValue(TagType type, Object value) throws IOException {
        switch (type) {
            case END -> {}
            case BYTE -> out.writeByte((byte) value);
            case SHORT -> out.writeShort((short) value);
            case INT -> out.writeInt((int) value);
            case LONG -> out.writeLong((long) value);
            case FLOAT -> out.writeFloat((float) value);
            case DOUBLE -> out.writeDouble((double) value);
            case BYTE_ARRAY -> writeByteArray((byte[]) value);
            case STRING -> out.writeUTF((String) value);
            case LIST -> writeList((NBTList) value);
            case COMPOUND -> writeCompound((NBTCompound) value);
            case INT_ARRAY -> writeIntArray((int[]) value);
            case LONG_ARRAY -> writeLongArray((long[]) value);
        }
    }

    private void writeByteArray(byte[] data) throws IOException {
        out.writeInt(data.length);

        for (byte value : data) {
            out.writeByte(value);
        }
    }

    private void writeList(NBTList data) throws IOException {
        TagType type = data.getType();
        out.writeByte(type.getId());
        out.writeByte(data.size());
        for (Object value : data) {
            writeValue(type, value);
        }
    }

    private void writeCompound(NBTCompound comp) throws IOException {
        for (Map.Entry<String, Object> entry : comp.data().entrySet()) {
            writeTag(entry.getKey(), entry.getValue());
        }
        writeTagId(TagType.END);
    }

    private void writeIntArray(int[] data) throws IOException {
        out.writeInt(data.length);

        for (int value : data) {
            out.writeInt(value);
        }
    }

    private void writeLongArray(long[] data) throws IOException {
        out.writeInt(data.length);

        for (long value : data) {
            out.writeLong(value);
        }
    }

}

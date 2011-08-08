package org.lwes.serializer;

import org.lwes.Event;
import org.lwes.util.EncodedString;
import org.lwes.util.IPAddress;
import org.lwes.util.NumberCodec;

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * This contains low level type serialization used by the
 * rest of the system.
 *
 * @author Anthony Molinaro
 */
public class Serializer {

    public static int serializeBYTE(byte aByte, byte[] bytes, int offset) {
        bytes[offset] = aByte;
        return (1);
    }
    
    public static int serializeUBYTE(short aUByte, byte[] bytes, int offset) throws IllegalArgumentException {
        if (aUByte < 0 || aUByte > 255) {
            throw new IllegalArgumentException("Unsigned byte "+aUByte+" out of range 0..255");
        }
        bytes[offset] = (byte) (aUByte&0xff);
        return (1);
    }

    public static int serializeBOOLEAN(boolean aBoolean, byte[] bytes, int offset) {
        if (aBoolean) {
            bytes[offset] = (byte) 0x01;
        }
        else {
            bytes[offset] = (byte) 0x00;
        }
        return (1);
    }

    public static int serializeUINT16(int anUnsignedShortInt, byte[] bytes,
                                      int offset) {
        bytes[offset] = (byte) ((anUnsignedShortInt & (255 << 8)) >> 8);
        bytes[offset + 1] = (byte) ((anUnsignedShortInt & (255 << 0)) >> 0);
        return (2);
    }

    public static int serializeINT16(short aShortInt, byte[] bytes, int offset) {
        bytes[offset] = (byte) ((aShortInt & (255 << 8)) >> 8);
        bytes[offset + 1] = (byte) ((aShortInt & (255 << 0)) >> 0);
        return (2);
    }

    public static int serializeUINT32(long anUnsignedInt, byte[] bytes,
                                      int offset) {
        bytes[offset] = (byte) ((anUnsignedInt & 0xff000000) >> 24);
        bytes[offset + 1] = (byte) ((anUnsignedInt & 0x00ff0000) >> 16);
        bytes[offset + 2] = (byte) ((anUnsignedInt & 0x0000ff00) >> 8);
        bytes[offset + 3] = (byte) ((anUnsignedInt & 0x000000ff) >> 0);
        return (4);
    }

    public static int serializeINT32(int anInt, byte[] bytes,
                                     int offset) {
        bytes[offset] = (byte) ((anInt & (255 << 24)) >> 24);
        bytes[offset + 1] = (byte) ((anInt & (255 << 16)) >> 16);
        bytes[offset + 2] = (byte) ((anInt & (255 << 8)) >> 8);
        bytes[offset + 3] = (byte) ((anInt & (255 << 0)) >> 0);
        return (4);
    }

    public static int serializeINT64(long anInt, byte[] bytes, int offset) {
        NumberCodec.encodeLongUnchecked(anInt, bytes, offset);
        return (8);
    }

    public static int serializeUINT64(long anInt, byte[] bytes, int offset) {
        NumberCodec.encodeLongUnchecked(anInt, bytes, offset);
        return (8);
    }

    public static int serializeUINT64(BigInteger anInt, byte[] bytes, int offset) {
        // TODO: write a BigInteger serialization method
        NumberCodec.encodeLongUnchecked(anInt.longValue(), bytes, offset);
        return (8);
    }

    /**
     * @deprecated
     */
    public static int serializeSTRING(String aString, byte[] bytes, int offset) {
        return serializeSTRING(aString, bytes, offset, Event.DEFAULT_ENCODING);
    }

    public static int serializeSTRING(String aString, byte[] bytes, int offset,
                                      short encoding) {
        byte[] stringBytes =
                EncodedString.getBytes(aString, Event.ENCODING_STRINGS[encoding]);
        int length = stringBytes.length;
        if (length < 65535 && length >= 0) {
            offset += serializeUINT16(length, bytes, offset);
            System.arraycopy(stringBytes, 0, bytes, offset, length);
            return (length + 2);
        }
        return 0;

    }

    /**
     * String arrays are serialized as follows:
     * <array_name_len><array_name_bytes><type>
     * <array_length><serialized_type_1>...<serialized_type_n>
     *
     * @param value
     * @param bytes
     * @param offset
     * @param encoding
     * @return
     */
    public static int serializeStringArray(String[] value,
                                           byte[] bytes,
                                           int offset,
                                           short encoding) {

        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (String s : value) {
            numbytes = serializeSTRING(s, bytes, offset, encoding);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeInt16Array(short[] value,
                                          byte[] bytes,
                                          int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (short s : value) {
            numbytes = serializeINT16(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeInt32Array(int[] value,
                                          byte[] bytes,
                                          int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (int s : value) {
            numbytes = serializeINT32(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeInt64Array(long[] value,
                                          byte[] bytes,
                                          int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (long s : value) {
            numbytes = serializeINT64(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeUInt16Array(int[] value,
                                           byte[] bytes,
                                           int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (int s : value) {
            numbytes = serializeUINT16(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeUInt32Array(long[] value,
                                           byte[] bytes,
                                           int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (long s : value) {
            numbytes = serializeUINT32(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeUInt64Array(long[] value,
                                           byte[] bytes,
                                           int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (long s : value) {
            numbytes = serializeUINT64(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeBooleanArray(boolean[] value,
                                            byte[] bytes,
                                            int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (boolean s : value) {
            numbytes = serializeBOOLEAN(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }

    public static int serializeByteArray(byte[] value,
                                         byte[] bytes,
                                         int offset) {
        int numbytes = 0;
        int offsetStart = offset;
        numbytes = serializeUINT16(value.length, bytes, offset);
        offset += numbytes;
        for (byte s : value) {
            numbytes = serializeBYTE(s, bytes, offset);
            offset += numbytes;
        }
        return (offset - offsetStart);
    }
    
    /*
       * @deprecated
       */
    public static int serializeEVENTWORD(String aString, byte[] bytes, int offset) {
        return serializeEVENTWORD(aString, bytes, offset, Event.DEFAULT_ENCODING);
    }

    private static int serializeEVENTWORD(String aString, byte[] bytes,
                                          int offset, short encoding) {
        byte[] stringBytes =
                EncodedString.getBytes(aString, Event.ENCODING_STRINGS[encoding]);
        int length = stringBytes.length;
        if (0 <= length && length <= 255) {
            offset += serializeUBYTE((short) length, bytes, offset);
            System.arraycopy(stringBytes, 0, bytes, offset, length);
            return (length + 1);
        }
        return 0;

    }

    public static int serializeATTRIBUTEWORD(String aString, byte[] bytes,
                                             int offset) {
        return serializeEVENTWORD(aString, bytes, offset, Event.DEFAULT_ENCODING);
    }

    public static int serializeIPADDR(IPAddress anIPAddress, byte[] bytes,
                                      int offset) {
        byte[] inetaddr = anIPAddress.getInetAddressAsBytes();
        bytes[offset + 3] = inetaddr[0];
        bytes[offset + 2] = inetaddr[1];
        bytes[offset + 1] = inetaddr[2];
        bytes[offset] = inetaddr[3];
        return (4);
    }

    public static int serializeIPADDR(InetAddress anIPAddress, byte[] bytes,
                                      int offset) {
        byte[] inetaddr = anIPAddress.getAddress();
        bytes[offset + 3] = inetaddr[0];
        bytes[offset + 2] = inetaddr[1];
        bytes[offset + 1] = inetaddr[2];
        bytes[offset] = inetaddr[3];
        return (4);
    }
}

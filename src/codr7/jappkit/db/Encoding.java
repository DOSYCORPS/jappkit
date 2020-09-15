package codr7.jappkit.db;

import codr7.jappkit.db.errors.IOError;

import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public final class Encoding {
    public static long readLong(SeekableByteChannel in) {
        ByteBuffer buf = ByteBuffer.allocate(1);

        try {
            if (in.read(buf) == -1) { throw new IOError(new EOFException()); }
        }
        catch (IOException e){ throw new IOError(e); }

        buf.rewind();
        byte len = buf.get();
        buf = ByteBuffer.allocate(len);

        try { in.read(buf); }
        catch (IOException e){ throw new IOError(e); }

        buf.rewind();
        byte[] bs = new byte[len];
        buf.get(bs);
        return Long.valueOf(new String(bs)).longValue();
    }

    public static String readString(SeekableByteChannel in) {
        long len = readLong(in);
        ByteBuffer buf = ByteBuffer.allocate((int)len);

        try { in.read(buf); }
        catch (IOException e) { throw new IOError(e); }

        buf.rewind();
        byte[] bs = new byte[(int)len];
        buf.get(bs);
        return new String(bs);
    }

    public static void writeLong(long it, SeekableByteChannel out) {
        byte[] bs = null;
        try { bs = Long.valueOf(it).toString().getBytes("UTF-8"); }
        catch (UnsupportedEncodingException e) { throw new E(e.getMessage()); }

        ByteBuffer buf = ByteBuffer.allocate(bs.length + 1);
        buf.put((byte)bs.length);
        buf.put(bs);
        buf.rewind();

        try { out.write(buf); }
        catch (IOException e) { throw new IOError(e); }
    }

    public static void writeString(String it, SeekableByteChannel out) {
        byte[] bs = null;
        try { bs = it.getBytes("UTF-8"); }
        catch (UnsupportedEncodingException e) { throw new E(e.getMessage()); }

        writeLong(bs.length, out);
        ByteBuffer buf = ByteBuffer.allocate(bs.length);
        buf.put(bs);
        buf.rewind();

        try { out.write(buf); }
        catch (IOException e) { throw new IOError(e); }
    }

    private Encoding() { }
}

package org.dynmap.utils;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class BufferInputStreamTest {

    @Test
    public void testConstructorWithBuffer() {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data);
        assertEquals(5, stream.length());
        assertSame(data, stream.buffer());
    }

    @Test
    public void testConstructorWithBufferAndLength() {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data, 3);
        assertEquals(3, stream.length());
        assertSame(data, stream.buffer());
    }

    @Test
    public void testReadSingleByte() {
        byte[] data = {10, 20, 30};
        BufferInputStream stream = new BufferInputStream(data);
        assertEquals(10, stream.read());
        assertEquals(20, stream.read());
        assertEquals(30, stream.read());
        assertEquals(-1, stream.read()); // EOF
    }

    @Test
    public void testReadSingleByteUnsigned() {
        // Test that bytes are returned as unsigned (0-255)
        byte[] data = {(byte) 255, (byte) 128};
        BufferInputStream stream = new BufferInputStream(data);
        assertEquals(255, stream.read());
        assertEquals(128, stream.read());
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data);
        byte[] buffer = new byte[3];
        int bytesRead = stream.read(buffer, 0, 3);
        assertEquals(3, bytesRead);
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
    }

    @Test
    public void testReadArrayPartial() throws IOException {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        byte[] buffer = new byte[5];
        int bytesRead = stream.read(buffer, 0, 5);
        assertEquals(3, bytesRead);
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
    }

    @Test
    public void testReadArrayAtEOF() throws IOException {
        byte[] data = {1, 2};
        BufferInputStream stream = new BufferInputStream(data);
        stream.read();
        stream.read();
        byte[] buffer = new byte[5];
        int bytesRead = stream.read(buffer, 0, 5);
        assertEquals(-1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadArrayNullBuffer() throws IOException {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        stream.read(null, 0, 3);
    }

    @Test(expected = IOException.class)
    public void testReadArrayNegativeOffset() throws IOException {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        byte[] buffer = new byte[5];
        stream.read(buffer, -1, 3);
    }

    @Test
    public void testAvailable() {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data);
        assertEquals(5, stream.available());
        stream.read();
        assertEquals(4, stream.available());
        stream.read();
        stream.read();
        assertEquals(2, stream.available());
    }

    @Test
    public void testSkip() {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data);
        long skipped = stream.skip(2);
        assertEquals(2, skipped);
        assertEquals(3, stream.read());
    }

    @Test
    public void testSkipPastEnd() {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        long skipped = stream.skip(10);
        assertEquals(3, skipped);
        assertEquals(-1, stream.read());
    }

    @Test
    public void testSkipNegative() {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        stream.read();
        long skipped = stream.skip(-5);
        assertEquals(0, skipped);
    }

    @Test
    public void testMarkSupported() {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        assertTrue(stream.markSupported());
    }

    @Test
    public void testMarkAndReset() {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data);
        stream.read();
        stream.read();
        stream.mark(0); // Mark at position 2
        assertEquals(3, stream.read());
        assertEquals(4, stream.read());
        stream.reset();
        assertEquals(3, stream.read()); // Back to position 2
    }

    @Test
    public void testResetWithoutMark() {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        stream.read();
        stream.read();
        stream.reset(); // Should reset to 0 (default mark)
        assertEquals(1, stream.read());
    }

    @Test
    public void testClose() {
        byte[] data = {1, 2, 3};
        BufferInputStream stream = new BufferInputStream(data);
        stream.close(); // Should not throw
        // Stream should still be usable after close (no-op)
        assertEquals(1, stream.read());
    }

    @Test
    public void testEmptyBuffer() {
        byte[] data = {};
        BufferInputStream stream = new BufferInputStream(data);
        assertEquals(0, stream.available());
        assertEquals(-1, stream.read());
    }

    @Test
    public void testLengthVsBufferLength() {
        byte[] data = {1, 2, 3, 4, 5};
        BufferInputStream stream = new BufferInputStream(data, 2);
        assertEquals(2, stream.available());
        assertEquals(1, stream.read());
        assertEquals(2, stream.read());
        assertEquals(-1, stream.read()); // EOF at specified length
    }
}

package org.dynmap.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class Vector3DTest {

    private static final double DELTA = 1e-10;

    @Test
    public void testDefaultConstructor() {
        Vector3D v = new Vector3D();
        assertEquals(0.0, v.x, DELTA);
        assertEquals(0.0, v.y, DELTA);
        assertEquals(0.0, v.z, DELTA);
    }

    @Test
    public void testParameterizedConstructor() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        assertEquals(1.0, v.x, DELTA);
        assertEquals(2.0, v.y, DELTA);
        assertEquals(3.0, v.z, DELTA);
    }

    @Test
    public void testCopyConstructor() {
        Vector3D original = new Vector3D(5.0, 6.0, 7.0);
        Vector3D copy = new Vector3D(original);
        assertEquals(original.x, copy.x, DELTA);
        assertEquals(original.y, copy.y, DELTA);
        assertEquals(original.z, copy.z, DELTA);
        // Ensure it's a copy, not a reference
        original.x = 100.0;
        assertEquals(5.0, copy.x, DELTA);
    }

    @Test
    public void testSet() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D();
        Vector3D result = v2.set(v1);
        assertEquals(1.0, v2.x, DELTA);
        assertEquals(2.0, v2.y, DELTA);
        assertEquals(3.0, v2.z, DELTA);
        assertSame(v2, result);
    }

    @Test
    public void testAdd() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(4.0, 5.0, 6.0);
        Vector3D result = v1.add(v2);
        assertEquals(5.0, v1.x, DELTA);
        assertEquals(7.0, v1.y, DELTA);
        assertEquals(9.0, v1.z, DELTA);
        assertSame(v1, result);
    }

    @Test
    public void testSubtract() {
        Vector3D v1 = new Vector3D(5.0, 7.0, 9.0);
        Vector3D v2 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D result = v1.subtract(v2);
        assertEquals(4.0, v1.x, DELTA);
        assertEquals(5.0, v1.y, DELTA);
        assertEquals(6.0, v1.z, DELTA);
        assertSame(v1, result);
    }

    @Test
    public void testScale() {
        Vector3D v = new Vector3D(2.0, 3.0, 4.0);
        Vector3D result = v.scale(2.0);
        assertEquals(4.0, v.x, DELTA);
        assertEquals(6.0, v.y, DELTA);
        assertEquals(8.0, v.z, DELTA);
        assertSame(v, result);
    }

    @Test
    public void testInnerProduct() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(4.0, 5.0, 6.0);
        double result = v1.innerProduct(v2);
        // 1*4 + 2*5 + 3*6 = 4 + 10 + 18 = 32
        assertEquals(32.0, result, DELTA);
    }

    @Test
    public void testCrossProduct() {
        Vector3D v1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D v2 = new Vector3D(0.0, 1.0, 0.0);
        Vector3D result = v1.crossProduct(v2);
        // i x j = k
        assertEquals(0.0, v1.x, DELTA);
        assertEquals(0.0, v1.y, DELTA);
        assertEquals(1.0, v1.z, DELTA);
        assertSame(v1, result);
    }

    @Test
    public void testCrossProductGeneral() {
        Vector3D v1 = new Vector3D(2.0, 3.0, 4.0);
        Vector3D v2 = new Vector3D(5.0, 6.0, 7.0);
        v1.crossProduct(v2);
        // (3*7 - 4*6, 4*5 - 2*7, 2*6 - 3*5) = (21-24, 20-14, 12-15) = (-3, 6, -3)
        assertEquals(-3.0, v1.x, DELTA);
        assertEquals(6.0, v1.y, DELTA);
        assertEquals(-3.0, v1.z, DELTA);
    }

    @Test
    public void testLength() {
        Vector3D v = new Vector3D(3.0, 4.0, 0.0);
        assertEquals(5.0, v.length(), DELTA);
    }

    @Test
    public void testLengthUnit() {
        Vector3D v = new Vector3D(1.0, 0.0, 0.0);
        assertEquals(1.0, v.length(), DELTA);
    }

    @Test
    public void testLengthZero() {
        Vector3D v = new Vector3D();
        assertEquals(0.0, v.length(), DELTA);
    }

    @Test
    public void testEquals() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(1.0, 2.0, 3.0);
        assertTrue(v1.equals(v2));
        assertTrue(v2.equals(v1));
    }

    @Test
    public void testEqualsNotEqual() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(1.0, 2.0, 4.0);
        assertFalse(v1.equals(v2));
    }

    @Test
    public void testEqualsNull() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        assertFalse(v.equals(null));
    }

    @Test
    public void testEqualsWrongType() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        assertFalse(v.equals("not a vector"));
    }

    @Test
    public void testHashCodeConsistency() {
        Vector3D v1 = new Vector3D(1.0, 2.0, 3.0);
        Vector3D v2 = new Vector3D(1.0, 2.0, 3.0);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    public void testToString() {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);
        String str = v.toString();
        assertTrue(str.contains("1.0"));
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("3.0"));
    }
}

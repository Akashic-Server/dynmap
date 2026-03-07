package org.dynmap.utils;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class DynIntHashMapTest {

    @Test
    public void testDefaultConstructor() {
        DynIntHashMap map = new DynIntHashMap();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        DynIntHashMap map = new DynIntHashMap(100);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    public void testConstructorWithCapacityAndLoadFactor() {
        DynIntHashMap map = new DynIntHashMap(100, 0.5f);
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeCapacity() {
        new DynIntHashMap(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorZeroLoadFactor() {
        new DynIntHashMap(10, 0.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeLoadFactor() {
        new DynIntHashMap(10, -0.5f);
    }

    @Test
    public void testCopyConstructor() {
        DynIntHashMap original = new DynIntHashMap();
        original.put(1, "one");
        original.put(2, "two");

        DynIntHashMap copy = new DynIntHashMap(original);
        assertEquals(2, copy.size());
        assertEquals("one", copy.get(1));
        assertEquals("two", copy.get(2));

        // Verify it's a deep copy
        original.put(1, "modified");
        assertEquals("one", copy.get(1));
    }

    @Test
    public void testPutAndGet() {
        DynIntHashMap map = new DynIntHashMap();
        assertNull(map.put(1, "one"));
        assertEquals("one", map.get(1));
        assertEquals(1, map.size());
    }

    @Test
    public void testPutReplace() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");
        Object oldValue = map.put(1, "ONE");
        assertEquals("one", oldValue);
        assertEquals("ONE", map.get(1));
        assertEquals(1, map.size());
    }

    @Test
    public void testPutNullValue() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, null);
        assertNull(map.get(1));
        assertTrue(map.containsKey(1));
    }

    @Test
    public void testGetNonExistent() {
        DynIntHashMap map = new DynIntHashMap();
        assertNull(map.get(999));
    }

    @Test
    public void testContainsKey() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");
        assertTrue(map.containsKey(1));
        assertFalse(map.containsKey(2));
    }

    @Test
    public void testContainsValue() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");
        map.put(2, "two");
        assertTrue(map.containsValue("one"));
        assertTrue(map.containsValue("two"));
        assertFalse(map.containsValue("three"));
    }

    @Test
    public void testContainsValueNull() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, null);
        assertTrue(map.containsValue(null));
    }

    @Test
    public void testRemove() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");
        map.put(2, "two");

        Object removed = map.remove(1);
        assertEquals("one", removed);
        assertEquals(1, map.size());
        assertFalse(map.containsKey(1));
        assertTrue(map.containsKey(2));
    }

    @Test
    public void testRemoveNonExistent() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");

        Object removed = map.remove(999);
        assertNull(removed);
        assertEquals(1, map.size());
    }

    @Test
    public void testClear() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get(1));
    }

    @Test
    public void testIsEmpty() {
        DynIntHashMap map = new DynIntHashMap();
        assertTrue(map.isEmpty());

        map.put(1, "one");
        assertFalse(map.isEmpty());

        map.remove(1);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testKeysWithValue() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "value");
        map.put(2, "value");
        map.put(3, "other");

        List<Integer> keys = map.keysWithValue("value");
        assertEquals(2, keys.size());
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
    }

    @Test
    public void testKeysWithValueNull() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, null);
        map.put(2, null);
        map.put(3, "value");

        List<Integer> keys = map.keysWithValue(null);
        assertEquals(2, keys.size());
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
    }

    @Test
    public void testKeysWithValueNoMatch() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(1, "one");
        map.put(2, "two");

        List<Integer> keys = map.keysWithValue("nonexistent");
        assertTrue(keys.isEmpty());
    }

    @Test
    public void testRehashing() {
        // Use small initial capacity to trigger rehash
        DynIntHashMap map = new DynIntHashMap(2, 0.75f);

        // Add enough entries to trigger rehash
        for (int i = 0; i < 100; i++) {
            map.put(i, "value" + i);
        }

        assertEquals(100, map.size());

        // Verify all entries are still accessible
        for (int i = 0; i < 100; i++) {
            assertEquals("value" + i, map.get(i));
        }
    }

    @Test
    public void testNegativeKey() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(-1, "negative");
        assertEquals("negative", map.get(-1));
        assertTrue(map.containsKey(-1));
    }

    @Test
    public void testZeroKey() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(0, "zero");
        assertEquals("zero", map.get(0));
        assertTrue(map.containsKey(0));
    }

    @Test
    public void testMaxIntKey() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(Integer.MAX_VALUE, "max");
        assertEquals("max", map.get(Integer.MAX_VALUE));
    }

    @Test
    public void testMinIntKey() {
        DynIntHashMap map = new DynIntHashMap();
        map.put(Integer.MIN_VALUE, "min");
        assertEquals("min", map.get(Integer.MIN_VALUE));
    }
}

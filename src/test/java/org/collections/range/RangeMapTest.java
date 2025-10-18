package org.collections.range;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;

public class RangeMapTest {
    private RangeMap<Integer, String> rangeMap;

    @BeforeEach
    void setUp() {
        rangeMap = new RangeMap<>();
    }

    @Test
    void testPutNullRange() {
        assertNull(rangeMap.put(null, "value"));
        assertTrue(rangeMap.isEmpty());
    }

    @Test
    void testPutFirstRange() {
        Range<Integer> range = new Range<Integer>().closed(1, 5);
        String result = rangeMap.put(range, "A");
        
        assertNull(result);
        assertEquals(1, rangeMap.size());
        assertEquals("A", rangeMap.get(range));
    }

    @Test
    void testPutNonOverlappingRanges() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        
        rangeMap.put(range1, "A");
        rangeMap.put(range2, "B");
        
        assertEquals(2, rangeMap.size());
        assertEquals("A", rangeMap.get(range1));
        assertEquals("B", rangeMap.get(range2));
    }

    @Test
    void testPutOverlappingRangesDifferentValues() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 10);
        Range<Integer> range2 = new Range<Integer>().closed(3, 7);
        
        rangeMap.put(range1, "A");
        rangeMap.put(range2, "B");
        
        assertEquals(3, rangeMap.size());

        assertEquals("A", rangeMap.get(new Range<Integer>().open(1, 3)));
        assertEquals("B", rangeMap.get(new Range<Integer>().open(3, 10)));
        assertEquals("A", rangeMap.get(new Range<Integer>().open(7, 10)));

    }

    @Test
    void testPutOverlappingRangesSameValues() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 10);
        Range<Integer> range2 = new Range<Integer>().closed(5, 15);
        
        rangeMap.put(range1, "A");
        rangeMap.put(range2, "A");
        
        assertEquals(1, rangeMap.size());
        
        Range<Integer> mergedRange = rangeMap.firstKey();
        assertEquals(1, mergedRange.getLeft());
        assertEquals(15, mergedRange.getRight());
        assertEquals("A", rangeMap.get(mergedRange));
    }

    @Test
    void testPutCompletelyContainedRange() {
        Range<Integer> outer = new Range<Integer>().closed(1, 20);
        Range<Integer> inner = new Range<Integer>().closed(5, 10);
        
        rangeMap.put(outer, "A");
        rangeMap.put(inner, "B");
        
        assertEquals(3, rangeMap.size());
    }

    @Test
    void testPutExactSameRangeDifferentValue() {
        Range<Integer> range = new Range<Integer>().closed(1, 10);
        
        rangeMap.put(range, "A");
        String previous = rangeMap.put(range, "B");
        
        assertEquals("A", previous);
        assertEquals(1, rangeMap.size());
        assertEquals("B", rangeMap.get(range));
    }

    @Test
    void testRemoveNull() {
        assertNull(rangeMap.remove(null));
    }

    @Test
    void testRemoveWrongClassType() {
        rangeMap.put(new Range<Integer>().closed(1, 5), "A");
        assertNull(rangeMap.remove(67.69));
        assertEquals(1, rangeMap.size());
    }

    @Test
    void testRemoveFromEmptyMap() {
        Range<Integer> range = new Range<Integer>().closed(1, 5);
        assertNull(rangeMap.remove(range));
    }

    @Test
    void testRemoveExactRange() {
        Range<Integer> range = new Range<Integer>().closed(1, 10);
        rangeMap.put(range, "A");
        
        String removed = rangeMap.remove(range);
        
        assertEquals("A", removed);
        assertTrue(rangeMap.isEmpty());
    }

    @Test
    void testRemovePartOfRange() {
        Range<Integer> original = new Range<Integer>().closed(1, 10);
        Range<Integer> toRemove = new Range<Integer>().closed(3, 7);
        
        rangeMap.put(original, "A");
        String removed = rangeMap.remove(toRemove);
        
        assertEquals("A", removed);
        assertEquals(2, rangeMap.size());
        
        int rangeCount = 0;
        for (Range<Integer> r : rangeMap.keySet()) {
            rangeCount++;
            assertEquals("A", rangeMap.get(r));
            assertTrue((r.getLeft() == 1 && r.getRight() == 3) ||
                      (r.getLeft() == 7 && r.getRight() == 10));
        }
        assertEquals(2, rangeCount);
    }

    @Test
    void testRemoveFromBeginning() {
        Range<Integer> original = new Range<Integer>().closed(1, 10);
        Range<Integer> toRemove = new Range<Integer>().closed(1, 4);
        
        rangeMap.put(original, "A");
        String removed = rangeMap.remove(toRemove);
        
        assertEquals("A", removed);
        assertEquals(1, rangeMap.size());
        
        Range<Integer> remaining = rangeMap.firstKey();
        assertEquals(4, remaining.getLeft());
        assertEquals(10, remaining.getRight());
    }

    @Test
    void testRemoveFromEnd() {
        Range<Integer> original = new Range<Integer>().closed(1, 10);
        Range<Integer> toRemove = new Range<Integer>().closed(7, 10);
        
        rangeMap.put(original, "A");
        String removed = rangeMap.remove(toRemove);
        
        assertEquals("A", removed);
        assertEquals(1, rangeMap.size());
        
        Range<Integer> remaining = rangeMap.firstKey();
        assertEquals(1, remaining.getLeft());
        assertEquals(7, remaining.getRight());
    }

    @Test
    void testRemoveOverlappingMultipleRanges() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        Range<Integer> toRemove = new Range<Integer>().closed(3, 12);
        
        rangeMap.put(range1, "A");
        rangeMap.put(range2, "B");
        
        String removed = rangeMap.remove(toRemove);
        
        assertNotNull(removed);

        assertEquals(2, rangeMap.size());
    }

    @Test
    void testComplexScenarioMultipleOperations() {
        rangeMap.put(new Range<Integer>().closed(1, 10), "A");
        rangeMap.put(new Range<Integer>().closed(20, 30), "B");
        
        assertEquals(2, rangeMap.size());
        
        rangeMap.put(new Range<Integer>().closed(5, 25), "A");
        
        assertTrue(rangeMap.size() >= 2);
        
        rangeMap.remove(new Range<Integer>().closed(8, 22));
        
        assertTrue(rangeMap.size() >= 2);
        
        for (String value : rangeMap.values()) {
            assertTrue("A".equals(value) || "B".equals(value));
        }
    }

    @Test
    void testKeySetAndValues() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        
        rangeMap.put(range1, "A");
        rangeMap.put(range2, "B");
        
        Set<Range<Integer>> keySet = rangeMap.keySet();
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains(range1));
        assertTrue(keySet.contains(range2));
        
        assertEquals(2, rangeMap.values().size());
        assertTrue(rangeMap.values().contains("A"));
        assertTrue(rangeMap.values().contains("B"));
    }

    @Test
    void testEntrySet() {
        Range<Integer> range = new Range<Integer>().closed(1, 10);
        rangeMap.put(range, "test");
        
        Set<Map.Entry<Range<Integer>, String>> entries = rangeMap.entrySet();
        assertEquals(1, entries.size());
        
        Map.Entry<Range<Integer>, String> entry = entries.iterator().next();
        assertEquals(range, entry.getKey());
        assertEquals("test", entry.getValue());
    }

    @Test
    void testContainsKeyAndValue() {
        Range<Integer> range = new Range<Integer>().closed(1, 10);
        
        assertFalse(rangeMap.containsKey(range));
        assertFalse(rangeMap.containsValue("A"));
        
        rangeMap.put(range, "A");
        
        assertTrue(rangeMap.containsKey(range));
        assertTrue(rangeMap.containsValue("A"));
    }

    @Test
    void testClear() {
        rangeMap.put(new Range<Integer>().closed(1, 5), "A");
        rangeMap.put(new Range<Integer>().closed(10, 15), "B");
        
        assertEquals(2, rangeMap.size());
        
        rangeMap.clear();
        
        assertTrue(rangeMap.isEmpty());
        assertEquals(0, rangeMap.size());
    }

    @Test
    void testFirstAndLastKey() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        Range<Integer> range3 = new Range<Integer>().closed(20, 25);
        
        rangeMap.put(range2, "B");
        rangeMap.put(range1, "A");
        rangeMap.put(range3, "C");
        
        assertEquals(range1, rangeMap.firstKey());
        assertEquals(range3, rangeMap.lastKey());
    }

    @Test
    void testWithDifferentTypes() {
        Range<Integer> openRange = new Range<Integer>().open(1, 5);
        Range<Integer> closedRange = new Range<Integer>().closed(3, 8);
        
        rangeMap.put(openRange, "open");
        rangeMap.put(closedRange, "closed");
        
        assertTrue(rangeMap.size() >= 2);
    }
}
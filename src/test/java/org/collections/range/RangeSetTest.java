package org.collections.range;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

public class RangeSetTest {
    private RangeSet<Integer> rangeSet;

    @BeforeEach
    void setUp() {
        rangeSet = new RangeSet<>();
    }

    @Test
    void testAddNullRange() {
        assertFalse(rangeSet.add(null));
        assertTrue(rangeSet.isEmpty());
    }

    @Test
    void testAddNonOverlappingRange() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        
        assertTrue(rangeSet.add(range1));
        assertTrue(rangeSet.add(range2));
        
        assertEquals(2, rangeSet.size());
        assertTrue(rangeSet.contains(range1));
        assertTrue(rangeSet.contains(range2));
    }

    @Test
    void testAddOverlappingLeft() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(3, 8);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        assertEquals(1, rangeSet.size());
        Range<Integer> merged = rangeSet.first();
        assertEquals(1, merged.getLeft());
        assertEquals(8, merged.getRight());
    }

    @Test
    void testAddOverlappingRight() {
        Range<Integer> range1 = new Range<Integer>().closed(10, 15);
        Range<Integer> range2 = new Range<Integer>().closed(7, 12);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        assertEquals(1, rangeSet.size());
        Range<Integer> merged = rangeSet.first();
        assertEquals(7, merged.getLeft());
        assertEquals(15, merged.getRight());
    }

    @Test
    void testAddMultipleOverlapping() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(3, 8);
        Range<Integer> range3 = new Range<Integer>().closed(6, 12);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        rangeSet.add(range3);
        
        assertEquals(1, rangeSet.size());
        Range<Integer> merged = rangeSet.first();
        assertEquals(1, merged.getLeft());
        assertEquals(12, merged.getRight());
    }

    @Test
    void testAddAdjacentRanges() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(5, 10);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        assertEquals(1, rangeSet.size());
        Range<Integer> merged = rangeSet.first();
        assertEquals(1, merged.getLeft());
        assertEquals(10, merged.getRight());
    }

    @Test
    void testAddWithDifferentTypes() {
        Range<Integer> range1 = new Range<Integer>().open(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(5, 10);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        assertEquals(1, rangeSet.size());
        Range<Integer> merged = rangeSet.first();
        assertEquals(1, merged.getLeft());
        assertEquals(10, merged.getRight());
    }

    @Test
    void testAddMergeBothSides() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        Range<Integer> range3 = new Range<Integer>().closed(5, 10);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        rangeSet.add(range3);
        
        assertEquals(1, rangeSet.size());
        Range<Integer> merged = rangeSet.first();
        assertEquals(1, merged.getLeft());
        assertEquals(15, merged.getRight());
    }

    @Test
    void testRemoveNull() {
        assertFalse(rangeSet.remove(null));
    }

    @Test
    void testRemoveWrongClassType() {
        rangeSet.add(new Range<Integer>().closed(1, 5));
        assertFalse(rangeSet.remove("not a range"));
        assertEquals(1, rangeSet.size());
    }

    @Test
    void testRemoveFromEmptySet() {
        Range<Integer> range = new Range<Integer>().closed(1, 5);
        assertFalse(rangeSet.remove(range));
    }

    @Test
    void testRemoveExactMatch() {
        Range<Integer> range = new Range<Integer>().closed(1, 5);
        rangeSet.add(range);
        
        assertTrue(rangeSet.remove(range));
        assertTrue(rangeSet.isEmpty());
    }

    @Test
    void testRemoveFromMiddle() {
        Range<Integer> original = new Range<Integer>().closed(1, 10);
        Range<Integer> toRemove = new Range<Integer>().closed(4, 6);
        
        rangeSet.add(original);
        assertTrue(rangeSet.remove(toRemove));
        
        assertEquals(2, rangeSet.size());
        
        Iterator<Range<Integer>> iterator = rangeSet.iterator();
        Range<Integer> first = iterator.next();
        Range<Integer> second = iterator.next();
        
        assertTrue((first.getLeft() == 1 && first.getRight() == 4) ||
                  (first.getLeft() == 6 && first.getRight() == 10));
    }

    @Test
    void testRemoveFromLeft() {
        Range<Integer> original = new Range<Integer>().closed(1, 10);
        Range<Integer> toRemove = new Range<Integer>().closed(1, 4);
        
        rangeSet.add(original);
        assertTrue(rangeSet.remove(toRemove));
        
        assertEquals(1, rangeSet.size());
        Range<Integer> remaining = rangeSet.first();
        assertEquals(4, remaining.getLeft());
        assertEquals(10, remaining.getRight());
    }

    @Test
    void testRemoveFromRight() {
        Range<Integer> original = new Range<Integer>().closed(1, 10);
        Range<Integer> toRemove = new Range<Integer>().closed(6, 10);
        
        rangeSet.add(original);
        assertTrue(rangeSet.remove(toRemove));
        
        assertEquals(1, rangeSet.size());
        Range<Integer> remaining = rangeSet.first();
        assertEquals(1, remaining.getLeft());
        assertEquals(6, remaining.getRight());
    }

    @Test
    void testRemoveMultipleOverlapping() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        Range<Integer> toRemove = new Range<Integer>().closed(3, 12);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        assertTrue(rangeSet.remove(toRemove));
        
        // Should leave [1,3) and (12,15]
        assertEquals(2, rangeSet.size());
    }

    @Test
    void testIsPointInTrue() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        assertTrue(rangeSet.isPointIn(3));
        assertTrue(rangeSet.isPointIn(12));
        assertTrue(rangeSet.isPointIn(1));  // Closed bound
        assertTrue(rangeSet.isPointIn(5));  // Closed bound
    }

    @Test
    void testIsPointInFalse() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        assertFalse(rangeSet.isPointIn(0));
        assertFalse(rangeSet.isPointIn(7));
        assertFalse(rangeSet.isPointIn(20));
    }

    @Test
    void testIsPointInEmptySet() {
        assertFalse(rangeSet.isPointIn(5));
    }

    @Test
    void testIsPointInWithOpenBounds() {
        Range<Integer> range = new Range<Integer>().open(1, 5);
        rangeSet.add(range);
        
        assertTrue(rangeSet.isPointIn(3));
        assertFalse(rangeSet.isPointIn(1));  // Open bound
        assertFalse(rangeSet.isPointIn(5));  // Open bound
    }

    @Test
    void testComplexScenario() {
        // Add multiple ranges
        rangeSet.add(new Range<Integer>().closed(1, 5));
        rangeSet.add(new Range<Integer>().closed(3, 8));  // Should merge
        rangeSet.add(new Range<Integer>().closed(10, 15));
        
        assertEquals(2, rangeSet.size()); // [1,8] and [10,15]
        
        // Remove overlapping range
        rangeSet.remove(new Range<Integer>().closed(4, 12));
        
        // Should result in [1,4) and (12,15]
        assertEquals(2, rangeSet.size());
        
        // Test points
        assertTrue(rangeSet.isPointIn(2));
        assertFalse(rangeSet.isPointIn(5));
        assertFalse(rangeSet.isPointIn(10));
        assertTrue(rangeSet.isPointIn(13));
    }

    @Test
    void testSinglePointRanges() {
        Range<Integer> pointRange = new Range<Integer>().closed(5, 5);
        rangeSet.add(pointRange);
        
        assertTrue(rangeSet.isPointIn(5));
        assertEquals(1, rangeSet.size());
        
        rangeSet.remove(pointRange);
        assertFalse(rangeSet.isPointIn(5));
        assertTrue(rangeSet.isEmpty());
    }

    @Test
    void testTreeSetInheritance() {
        Range<Integer> range1 = new Range<Integer>().closed(1, 5);
        Range<Integer> range2 = new Range<Integer>().closed(10, 15);
        
        rangeSet.add(range1);
        rangeSet.add(range2);
        
        // Test inherited TreeSet methods
        assertFalse(rangeSet.isEmpty());
        assertEquals(2, rangeSet.size());
        assertTrue(rangeSet.contains(range1));
        
        rangeSet.clear();
        assertTrue(rangeSet.isEmpty());
    }
}
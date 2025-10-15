import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RangeTest {
    private Range<Integer> range;
    private Range<Integer> range2;

    @BeforeEach
    void setUp() {
        range = new Range<Integer>();
        range2 = new Range<Integer>();
    }

    @Test
    void open() {
        Range<Integer> result = range.open(2, 5);
        
        assertSame(range, result);
        assertEquals(2, range.getLeft());
        assertEquals(5, range.getRight());
        assertEquals(Range.RangeType.OPEN, range.getLeftType());
        assertEquals(Range.RangeType.OPEN, range.getRightType());
    }

    @Test
    void openEqualBounds() {
        Range<Integer> result = range.open(5, 5);
        
        assertNull(result);
    }

    @Test
    void openClosed() {
        range.openClosed(2, 5);
        
        assertEquals(2, range.getLeft());
        assertEquals(5, range.getRight());
        assertEquals(Range.RangeType.OPEN, range.getLeftType());
        assertEquals(Range.RangeType.CLOSED, range.getRightType());
    }

    @Test
    void closedOpen() {
        range.closedOpen(2, 5);
        
        assertEquals(2, range.getLeft());
        assertEquals(5, range.getRight());
        assertEquals(Range.RangeType.CLOSED, range.getLeftType());
        assertEquals(Range.RangeType.OPEN, range.getRightType());
    }

    @Test
    void closed() {
        range.closed(2, 5);
        
        assertEquals(2, range.getLeft());
        assertEquals(5, range.getRight());
        assertEquals(Range.RangeType.CLOSED, range.getLeftType());
        assertEquals(Range.RangeType.CLOSED, range.getRightType());
    }

    @Test
    void closedEqualBounds() {
        Range<Integer> result = range.closed(5, 5);
        
        assertNotNull(result);
        assertEquals(5, range.getLeft());
        assertEquals(5, range.getRight());
    }

    @Test
    void has() {
        range.closed(1, 3);
        assertEquals(true, range.has(2));
        assertEquals(false, range.has(0));

        range.closedOpen(3, 5);
        assertEquals(true, range.has(3));
        assertEquals(false, range.has(5));

        range.openClosed(5, 7);
        assertEquals(true, range.has(7));
        assertEquals(false, range.has(5));

        range.open(7, 9);
        assertEquals(true, range.has(8));
        assertEquals(false, range.has(10));
    }

    @Test
    void connects() {
        range.closed(0, 3);
        range2.open(3, 4);
        assertEquals(true, range.connects(range2));
        assertEquals(true, range2.connects(range));
        range2.closed(3, 5);
        assertEquals(true, range.connects(range2));
        assertEquals(true, range2.connects(range));
    }

    @Test
    void enclose() {
        range.closed(0, 1);
        range2.open(3, 4);
        range.enclose(range2);
        assertEquals(new Range<Integer>().closedOpen(0, 4), range);
        
        range.closedOpen(0, 4);
        range2.openClosed(-2, 4);
        range.enclose(range2);
        assertEquals(new Range<Integer>().openClosed(-2, 4), range);
    }

    @Test
    void substract() {
        range.closed(0, 5);
        range2.openClosed(4, 7);
        assertArrayEquals(new Range[]{new Range<Integer>().closed(0, 4), null}, range.substract(range2));
        
        range2.open(1, 3);
        assertArrayEquals(new Range[]{new Range<Integer>().closed(0, 1), new Range<Integer>().closed(3, 5)}, range.substract(range2));

        range2.closedOpen(0, 3);
        assertArrayEquals(new Range[]{null, new Range<Integer>().closed(3, 5)}, range.substract(range2));
        
        range2.open(1, 5);
        assertArrayEquals(new Range[]{new Range<Integer>().closed(0, 1), new Range<Integer>().closed(5, 5)}, range.substract(range2));
    }

    @Test
    void testToString() {
        range.closed(0, 6);
        assertEquals("[0; 6]", range.toString());
        range.closedOpen(0, 5);
        assertEquals("[0; 5)", range.toString());
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        range.closedOpen(3, 5);
        range2 = range.clone();

        assertEquals(true, range != range2);
        assertEquals(true, range.getClass() == range2.getClass());
        assertEquals(true, range.equals(range2));
    }
}

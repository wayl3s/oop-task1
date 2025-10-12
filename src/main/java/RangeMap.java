package main.java;

import java.util.TreeMap;

public class RangeMap<K extends Comparable<K>, V> extends TreeMap<Range<K>, V> {
    @Override
    public V put(Range<K> range,V value) {
        return null;
    }

    @Override
    public V remove(Object range) {
        return null;
    }
}

package org.collections.range;
import java.util.Map;
import java.util.TreeMap;

public class RangeMap<K extends Comparable<K>, V> extends TreeMap<Range<K>, V> {
    @Override
    public V put(Range<K> range, V value) {
        if (range == null) return null;

        Map.Entry<Range<K>, V> tempEntry = super.floorEntry(range);
        V removedValue = null;

        if (tempEntry != null) {
            while (range.compareTo(tempEntry.getKey()) == 0) {
                if (removedValue == null) {
                    removedValue = tempEntry.getValue();
                }
                Range<K>[] remains = tempEntry.getKey().substract(range);
                V tempValue = tempEntry.getValue();
                super.remove(tempEntry.getKey());
                if (remains != null) {
                    for (Range<K> rem : remains) {
                        if (rem != null) {
                            super.put(rem, tempValue);
                        }
                    }
                }
                tempEntry = super.floorEntry(range);
                if (tempEntry == null || super.isEmpty()) break;
            }
            if (tempEntry != null) {
                if (range.connects(tempEntry.getKey()) && value.equals(tempEntry.getValue())) {
                    range.enclose(tempEntry.getKey());
                    super.remove(tempEntry.getKey());
                }
            }
        }

        tempEntry = super.ceilingEntry(range);

        if (tempEntry != null) {
            if (removedValue == null) {
                removedValue = tempEntry.getValue();
            }
            while (range.compareTo(tempEntry.getKey()) == 0) {
                Range<K>[] remains = tempEntry.getKey().substract(range);
                V tempValue = tempEntry.getValue();
                super.remove(tempEntry.getKey());
                if (remains != null) {
                    for (Range<K> rem : remains) {
                        if (rem != null) {
                            super.put(rem, tempValue);
                        }
                    }
                }
                tempEntry = super.ceilingEntry(range);
                if (tempEntry == null || super.isEmpty()) break;
            }
            if (tempEntry != null) {
                if (range.connects(tempEntry.getKey()) && value.equals(tempEntry.getValue())) {
                    range.enclose(tempEntry.getKey());
                    super.remove(tempEntry.getKey());
                }
            }
        }

        super.put(range, value);
        return removedValue;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object object) {
        if (object == null || object.getClass() != new Range<K>().getClass()) return null;
        
        Range<K> range = (Range<K>) object;
        V removedValue = null;

        Map.Entry<Range<K>, V> tempEntry = super.floorEntry(range);

        if (tempEntry != null) {
            while (range.compareTo(tempEntry.getKey()) == 0) {
                if (removedValue == null) {
                    removedValue = tempEntry.getValue();
                }
                Range<K>[] remains = tempEntry.getKey().substract(range);
                V value = tempEntry.getValue();
                super.remove(tempEntry.getKey());
                if (remains != null) {
                    for (Range<K> rem : remains) {
                        if (rem != null) {
                            super.put(rem, value);
                        }
                    }
                }
                tempEntry = super.floorEntry(range);
                if (tempEntry == null || super.isEmpty()) break;
            }
        }

        tempEntry = super.ceilingEntry(range);

        if (tempEntry != null) {
            while (range.compareTo(tempEntry.getKey()) == 0) {
                if (removedValue == null) {
                    removedValue = tempEntry.getValue();
                }
                Range<K>[] remains = tempEntry.getKey().substract(range);
                V value = tempEntry.getValue();
                super.remove(tempEntry.getKey());
                if (remains != null) {
                    for (Range<K> rem : remains) {
                        if (rem != null) {
                            super.put(rem, value);
                        }
                    }
                }
                tempEntry = super.ceilingEntry(range);
                if (tempEntry == null || super.isEmpty()) break;
            }
        }
        
        return removedValue;
    }

    public boolean isKeyPointIn(K point) {
        return super.containsKey(new Range<K>().closed(point, point));
    }
}

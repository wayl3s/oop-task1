import java.util.TreeSet;

public class RangeSet<T extends Comparable<T>> extends TreeSet<Range<T>> {
    @Override
    public boolean add(Range<T> range) {
        if (range != null) {
            Range<T> closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
            if (closest != null) {
                while (range.compareTo(closest) == 0) {
                    range.enclose(closest);
                    super.remove(closest);
                    if (super.isEmpty()) {
                        break;
                    }
                    closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                }
            }
            super.add(range);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object object) {
        if (object != null) {
            if (object.getClass() != Range.class.getClass()) {
                Range<T> range = (Range<T>) object;
                Range<T> closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                if (closest != null) {
                    Range<T> clone = null;
                    try {
                        clone = range.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    while (range.compareTo(closest) == 0) {
                        clone.enclose(closest);
                        if (clone.equals(range)) {
                            super.remove(closest);
                            closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                            if (closest == null) {
                                break;
                            }
                            continue;
                        }
                        try {
                            clone = range.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        Range<T> difference = closest.difference(range);
                        this.add(difference);
                        closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                        if (closest == null) {
                            break;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
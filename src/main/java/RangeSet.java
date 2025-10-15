import java.util.TreeSet;

public class RangeSet<T extends Comparable<T>> extends TreeSet<Range<T>> {
    @Override
    public boolean add(Range<T> range) {
        if (range == null) {
            return false;
        }
        Range<T> ceiling = super.ceiling(range);
        if (ceiling != null) {
            while (range.connects(ceiling)) {
                range.enclose(ceiling);
                super.remove(ceiling);
                ceiling = super.ceiling(range);
                if (ceiling == null || super.isEmpty()) {
                    break;
                }
            }
        }
        Range<T> floor = super.floor(range);
        if (floor != null) {
            while (range.connects(floor)) {
                range.enclose(floor);
                super.remove(floor);
                floor = super.floor(floor);
                if (floor == null || super.isEmpty()) {
                    break;
                }
            }
        }
        super.add(range);
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean remove(Object object) {
        if (object == null || object.getClass() != new Range<T>().getClass()) {
            return false;
        }
        Range<T> range = (Range<T>) object;
        Range<T> closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
        if (closest != null) {
            Range<T> clone = null;
            while (range.compareTo(closest) == 0) {
                try {
                    clone = range.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                clone.enclose(closest);
                if (clone.equals(range)) {
                    super.remove(closest);
                    closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                    if (closest == null) {
                        break;
                    }
                    continue;
                }
                Range[] substracted = closest.substract(range);
                super.remove(closest);
                this.add(substracted[0]);
                this.add(substracted[1]);
                closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                if (closest == null) {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isIn(T point) {
        return super.contains(new Range<T>().closed(point, point));
    } 
}
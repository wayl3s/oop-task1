import java.util.TreeSet;

public class RangeSet extends TreeSet<Range> {
    public boolean add(Range range) {
        if (super.isEmpty()) {
            super.add(range);
            return false;
        }
        if (range != null) {
            Range closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
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
    };

    public boolean remove(Range range) {
        if (super.isEmpty()) {
            return false;
        }
        Range closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
        Range clone = null;
        try {
            clone = range.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (closest != null) {
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
                Range difference = closest.difference(range);
                this.add(difference);
                closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
                if (closest == null) {
                    break;
                }
            }
        }
        return true;
    };
}
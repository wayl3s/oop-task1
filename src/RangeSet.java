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
        Range difference = super.ceiling(range).difference(range);
        this.add(difference);
        return true;
    };
}
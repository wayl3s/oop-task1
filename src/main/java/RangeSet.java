import java.util.TreeSet;

public class RangeSet<T extends Comparable<T>> extends TreeSet<Range<T>> {
    @Override
    public boolean add(Range<T> range) {
        if (range == null) return false;

        Range<T> tempRange = super.floor(range);
        if (tempRange != null) {
            while (range.connects(tempRange)) {
                range.enclose(tempRange);
                super.remove(tempRange);
                tempRange = super.floor(tempRange);
                if (tempRange == null || super.isEmpty()) break;
            }
        }
        
        tempRange = super.ceiling(range);
        if (tempRange != null) {
            while (range.connects(tempRange)) {
                range.enclose(tempRange);
                super.remove(tempRange);
                tempRange = super.ceiling(range);
                if (tempRange == null || super.isEmpty()) break;
            }
        }
        
        super.add(range);
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean remove(Object object) {
        if (object == null || object.getClass() != new Range<T>().getClass()) return false;
        
        Range<T> range = (Range<T>) object;
        Range<T> closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);

        if (closest == null) return false;

        Range<T> clone;
        while (range.compareTo(closest) == 0) {
            try {
                clone = range.clone();
                clone.enclose(closest);
                if (clone.equals(range)) {
                    super.remove(closest);

                    closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range); 
                    if (closest == null) break;

                    continue;
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            
            Range[] substracted = closest.substract(range);
            super.remove(closest);
            
            this.add(substracted[0]);
            this.add(substracted[1]);

            closest = super.ceiling(range) == null ? super.ceiling(range): super.floor(range);
            if (closest == null) break;
        }
        return true;    
    }

    public boolean isPointIn(T point) {
        return super.contains(new Range<T>().closed(point, point));
    } 
}
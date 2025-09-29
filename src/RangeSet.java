import java.util.Collection;
import java.util.TreeSet;

public class RangeSet extends TreeSet<Range> {

    public int size() {
        return super.size();
    }

    public boolean add(Range range) {

    };

    public boolean remove(Object var1);

    public boolean containsAll(Collection<?> var1);

    public boolean addAll(Collection<? extends E> var1);

    public boolean retainAll(Collection<?> var1);

    public boolean removeAll(Collection<?> var1);

    public void clear();

    public boolean equals(Object var1);
   
//     private TreeSet<Range> set = new TreeSet<>();

//     public void add(Range range) {
//         if (range != null) {
//             Range ceiling = set.ceiling(range);
//             if (range.compareTo(ceiling) == 0) {
//                 ceiling.extend(range);
//             }
//         }
//     }

//     public Range ceiling(Range range) {
        
//     }

//     public boolean contains(Object o) {
//       return this.m.containsKey(o);
//    }

}
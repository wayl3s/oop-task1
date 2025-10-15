public class Main {
    public static void main(String[] args) {
        Range<Double> x1 = new Range<Double>().openClosed(2.0, 7.0);
        Range<Double> x2 = new Range<Double>().closed(7.0, 9.0);
        Range<Double> x3 = new Range<Double>().open(1.0, 2.0);
        Range<Double> x4 = new Range<Double>().openClosed(5.0, 9.0);
        Range<Double> x5 = new Range<Double>().open(8.0, 9.0);
        Range<Double> x6 = new Range<Double>().open(1.0, 8.5);

        // int out = x1.compareTo(x2);
        // x1.extend(x2);
        // System.out.println(x1.toString());
        // Range x2_clone = x2;
        // x2_clone.closed(0, 1);
        // System.out.println(x2_clone.toString());
        

        // TreeSet<Range> ranges = new TreeSet<Range>();

       

        // System.out.println(x2.compareTo(x4));
        // System.out.println(ranges.ceiling(x4));
        
        // System.out.print(ranges.toString());

        System.out.println(x3.equals(x6));

        RangeSet<Double> ranges = new RangeSet<Double>();

        ranges.add(x1);
        ranges.add(x2);
        ranges.add(x3);

        System.out.println(ranges.toString());
        ranges.remove(x4);
        ranges.add(x5);

        System.out.println(ranges.toString());
        ranges.remove(x6);
        System.out.println(ranges.toString());
    }
}

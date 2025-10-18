package org.collections.range;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose tested class:\n RangeSet, RangeMap");
            String str = scanner.nextLine();
            if (str.equals("exit")) break;
            if (str.equals("RangeSet")) {
                System.out.println("Choose type:\n Integer, Double");
                str = scanner.nextLine();
                if (str.equals("Integer")) {
                    RangeSet<Integer> rangeSet = new RangeSet<Integer>();
                    while (true) {
                        System.out.println("*****************");
                        System.out.printf("Current set: %s\nName function and enter Range\n add, remove\nExample: add [0;4)\n", rangeSet.toString());
                        str = scanner.nextLine();
                        if (str.equals("exit")) break;
                        if (str.split(" ", 2)[0].equals("add")) {
                            rangeSet.add(new Range<Integer>().fromString(str.split(" ", 2)[1], Integer::parseInt));
                        } else if (str.split(" ", 2)[0].equals("remove")) {
                            rangeSet.remove(new Range<Integer>().fromString(str.split(" ", 2)[1], Integer::parseInt));
                        }
                    }
                    break;
                } else if (str.equals("Double")) {
                    RangeSet<Double> rangeSet = new RangeSet<Double>();
                    while (true) {
                        System.out.println("*****************");
                        System.out.printf("Current set: %s\nName function and enter Range\n add, remove\nExample: add [0.0;4.0)\n", rangeSet.toString());
                        str = scanner.nextLine();
                        if (str.equals("exit")) break;
                        if (str.split(" ", 2)[0].equals("add")) {
                            rangeSet.add(new Range<Double>().fromString(str.split(" ", 2)[1], Double::parseDouble));
                        } else if (str.split(" ", 2)[0].equals("remove")) {
                            rangeSet.remove(new Range<Double>().fromString(str.split(" ", 2)[1], Double::parseDouble));
                        }
                    }
                    break;
                }
            } else if (str.equals("RangeMap")) {
                System.out.println("Choose type:\n Integer, Double");
                str = scanner.nextLine();
                if (str.equals("Integer")) {
                    RangeMap<Integer, String> rangeSet = new RangeMap<Integer, String>();
                    while (true) {
                        System.out.println("*****************");
                        System.out.printf("Current set: %s\nName function and enter Range\n put, remove\nExample: add [0;4) word\n", rangeSet.toString());
                        str = scanner.nextLine();
                        if (str.equals("exit")) break;
                        if (str.split(" ", 2)[0].equals("add")) {
                            rangeSet.put(new Range<Integer>().fromString(str.split(" ", 3)[1], Integer::parseInt), str.split(" ", 3)[2]);
                        } else if (str.split(" ", 2)[0].equals("remove")) {
                            rangeSet.remove(new Range<Integer>().fromString(str.split(" ", 3)[1], Integer::parseInt), str.split(" ", 3)[2]);
                        }
                    }
                    break;
                } else if (str.equals("Double")) {
                    RangeMap<Double, String> rangeSet = new RangeMap<Double, String>();
                    while (true) {
                        System.out.println("*****************");
                        System.out.printf("Current set: %s\nName function and enter Range\n put, remove\nExample: add [0.0;4.0) word\n", rangeSet.toString());
                        str = scanner.nextLine();
                        if (str.equals("exit")) break;
                        if (str.split(" ", 3)[0].equals("add")) {
                            rangeSet.put(new Range<Double>().fromString(str.split(" ", 3)[1], Double::parseDouble), str.split(" ", 3)[2]);
                        } else if (str.split(" ", 3)[0].equals("remove")) {
                            rangeSet.remove(new Range<Double>().fromString(str.split(" ", 3)[1], Double::parseDouble), str.split(" ", 3)[2]);
                        }
                    }
                    break;
                }   
            }
        }
        scanner.close();
    }
}

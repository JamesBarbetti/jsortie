package jsortie.testing.object;

import java.util.Comparator;

public class MyInteger 
  implements Comparable<MyInteger> {
  protected int i;
  protected String s;
  static double comparisons;
  public int intValue() { return i; }
  public MyInteger(int iValue)  {
    i = iValue;
    s = "X" + iValue + "Y" + iValue + "Z" + iValue;
  }
  public boolean equals(MyInteger other) {
    return i == other.i;
  }
  public boolean equals(Object other) {
    return i == ((MyInteger)other).i;
  }
  @Override public String toString() {
    return "" + i;
  }
  @Override public int compareTo(MyInteger o) {
    return Integer.compare(i, o.i);
  }
  public static Comparator<MyInteger> integerComparator  
    = new Comparator<MyInteger>() {
    public int compare(MyInteger o1, MyInteger o2) {
      return Integer.compare(o1.i, o2.i);
    }
    @Override public String toString() {
      return "integerComparator";
    }
  };
  public static Comparator<MyInteger> nullSafeComparator
    = new Comparator<MyInteger>() {
    public int compare(MyInteger o1, MyInteger o2) {
      if (o1 == null) {
        if (o2 == null) {
          return 0;
        } else {
          return -1;
        }
      } else if (o2==null) {
        return 1;
      } else {
        return Integer.compare(o1.i, o2.i);
      }
    }
    @Override public String toString() {
      return "nullSafeComparator";
    }
  };
  protected static Comparator<MyInteger> countingComparator  
    = new Comparator<MyInteger>() {
    public int compare(MyInteger o1, MyInteger o2) {
      ++comparisons;
      return Integer.compare(o1.i, o2.i);
    }
    @Override public String toString() {
      return "countingComparator";
    }
  };
  public static void setComparisonCount(double count) {
    comparisons = count;
  }
  public static double getComparisonCount() {
    return comparisons;
  }
}
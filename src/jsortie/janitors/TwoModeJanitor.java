package jsortie.janitors;

import jsortie.RangeSorter;

public class TwoModeJanitor implements RangeSorter {
  protected RangeSorter small;
  protected int         cutOff;
  protected RangeSorter large;  	  
  @Override
  public String toString() {
    String name = this.getClass().getSimpleName();
    name += "( " + small.toString() 
      + ", " + cutOff + ", " + large.toString() + ")";
    return name;
  }	  
  public TwoModeJanitor ( RangeSorter smallRangeSorter
                        , int threshold
                        , RangeSorter largeRangeSorter) {
    small  = smallRangeSorter;
    cutOff = threshold;
    large  = largeRangeSorter;
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (stop-start<cutOff) {
      small.sortRange(vArray, start, stop);
    } else {
      large.sortRange(vArray, start, stop);
    }
  }
}

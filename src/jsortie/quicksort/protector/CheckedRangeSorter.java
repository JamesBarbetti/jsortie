package jsortie.quicksort.protector;

import java.util.Arrays;

import jsortie.RangeSorter;
import jsortie.exception.SortingFailureException;
import jsortie.helper.DumpRangeHelper;

public class CheckedRangeSorter 
  implements RangeSorter
{
  protected RangeSorter innerSorter;
	
  public CheckedRangeSorter(RangeSorter innerSorter) {
    this.innerSorter = innerSorter;
  }
	
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + innerSorter.toString() + ")";
  }
	
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    int[] copy;
    if (start+1<stop) {
      copy = new int [ stop - start ];
      for (int i=start; i<stop; ++i) {
        copy[i-start] = vArray[i];
      }
      Arrays.sort(copy);
      innerSorter.sortRange(vArray, start, stop);
      for (int i=start; i<stop; ++i) {
        if ( vArray[i] != copy[i-start]) {
          if ( copy.length < 100 )
          {
        	DumpRangeHelper.dumpRange("output", vArray, start, stop);
        	DumpRangeHelper.dumpRange("expected", copy, 0, stop-start);
          }
        	
          throw new SortingFailureException(innerSorter.getClass().getSimpleName() 
            + " did not sort array subrange [" + start + ".." + (stop-1) + "]"
            + " correctly: in it's output element [" + i + "] was " + vArray[i]
            + " but should have been " + copy[i-start]);
        }
      }
    } else {
      innerSorter.sortRange(vArray, start, stop);
    }		
  }
}

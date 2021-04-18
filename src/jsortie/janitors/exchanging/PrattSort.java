package jsortie.janitors.exchanging;

import java.util.Arrays;

import jsortie.RangeSorter;
import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.InsertionSort;

public class PrattSort extends InsertionSort implements RangeSorter {
  @Override
  public String toString() { return this.getClass().getSimpleName(); }
	
	/*
	 * Strategic clean-up?! This is designed for running the last few passes of a 
	 * Pratt sort (which is a variant of ShellSort, mentioned in 
	 * Knuth, Art of Programming volume 3, which I chose because the CPU “should be able to see
	 * ” that it can pipeline it efficiently, particularly if you run two passes, with 
	 * double the gap, one left-to-right, the other right-to-left), it seems that the 
	 * pipelining does trump the cache.
	 * 
	 */
	
  public static boolean compareAndSwapIntoOrder(int[] vArray, int left, int right) {
    boolean swapped = ( vArray[right] < vArray[left] );
    if (swapped) {
      RangeSortHelper.swapElements(vArray, left, right);
    }
    return swapped;
  }
	
  static int[] gapTable() {
    int gaps[] = new int[500];
    int i = 0;
    int j = 0;
    int w = 0;
    int p2 = 2;
    int p3 = 3;

    gaps[0] = 1;
    for (;;) {
      while( p2 == p3 ) {
        gaps[++w] = p2;
        p2 = gaps[++j] * 2;
        p3 = gaps[++i] * 3;
      }
      if ( p2 < p3  ) {
        gaps[++w] = p2;
        p2 = gaps[++j] * 2;
        if (p2 > 1000000000) {
          break;
        }
      } else {
        gaps[++w] = p3;
        p3 = gaps[++i] * 3;
        if (p3 > 600000000) {
          break;
        }
      }
    }
    gaps = Arrays.copyOf(gaps, w+1);
    return gaps;
  }
	
  //Todo: theoretically, every power of 2, up to about a billion, should be in this
  //      table, but I'm only using it as a janitor.  It doesn't need to do that
  static int gaps[] = gapTable(); 

  //Note: This class is intended to determine whether pipelining trumps the cache.

  int maxGapIndex = 0;
	
  public PrattSort(int maxN) {
    for ( maxGapIndex = 0; maxGapIndex < gaps.length && gaps[maxGapIndex] < maxN ; ++maxGapIndex );	
  }
	
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    for (int gapIndex = maxGapIndex; gapIndex>= 0; --gapIndex) {
      int gap = gaps[gapIndex];
      int stopHere = stop - gap;
      for (int i=start; i<stopHere; ++i) {
        compareAndSwapIntoOrder(vArray, start, start+gap );				
      }
    }
    sortSmallRange(vArray, start, stop);
  } 
}

package jsortie.object.quicksort.selector;

import java.util.Comparator;

import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;

public class Algorithm489ObjectSelector<T> 
  extends ObjectQuickSelector<T> 
  implements SinglePivotObjectPartitioner<T>
           , SinglePivotObjectSelector<T> {
  protected int threshold2  = 10;
  protected int threshold   = 600;
  protected boolean relaxed = false;
  protected ObjectBinaryInsertionSort<T> littleSorter;
  public Algorithm489ObjectSelector() {
    littleSorter = new ObjectBinaryInsertionSort<T>();
  }
  public Algorithm489ObjectSelector(boolean relax) { 
    littleSorter = new ObjectBinaryInsertionSort<T>();
    this.relaxed = relax; 
  }
  public Algorithm489ObjectSelector(int threshold) { 
    littleSorter = new ObjectBinaryInsertionSort<T>();
    this.threshold = threshold; 
  }
  public Algorithm489ObjectSelector
    ( int threshold, boolean relax) { 
    littleSorter = new ObjectBinaryInsertionSort<T>();
    this.threshold = threshold; 
    this.relaxed = relax; 
  }
  
  public Algorithm489ObjectSelector 
    ( SinglePivotObjectSelector<T> selector
    , SinglePivotObjectPartitioner<T> basePartitioner
    , boolean relax) {
    super(selector, basePartitioner);
    relaxed = relax;
  }
			
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int targetIndex) {
    int pivotIndex;
    if (!relaxed) {
      partitionRangeExactly
        ( comparator, vArray, start, stop
        , targetIndex);
      return targetIndex;
    }
    if (threshold<stop-start) {
      pivotIndex 
        = partitionSample 
          ( comparator, vArray, start, stop
          , targetIndex);
    } else {
      pivotIndex 
        = selector.selectPivotIndex
          ( comparator, vArray, start, stop );
    }
    return partitioner.partitionRange
           ( comparator, vArray, start, stop
           , pivotIndex);	
  }

  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int targetIndex ) {
    int pivotIndex = targetIndex;
    for (;;) {
      if ( threshold2 < stop-start ) {
        littleSorter.sortRange
          ( comparator, vArray, start, stop );
        return;
      } 
      if (threshold < stop-start ) {
        partitionSample
          ( comparator, vArray
          , start, stop, pivotIndex);
      }
      int split 
        = partitioner.partitionRange
          ( comparator, vArray,  start, stop
          , pivotIndex );	
      if ( split < targetIndex) {
        start = split+1;
      } else if (targetIndex<split) {
        stop = split;
      } else {
        return;
      }
      pivotIndex 
        = selector.selectPivotIndex
          ( comparator, vArray, start, stop );
    }
  }
  public int partitionSample
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex) {
    int n     = stop-start;
    int i     = pivotIndex - start + 1;	
    double z  = Math.log(n);
    double s  = 0.5 * Math.exp( 2 * z/3 ); // 0.5 * n ^ (2/3)
    double sd = 0.5 * Math.sqrt( z*s*(n-s)/n)*Math.signum(i-n/2);
    int left  = (int)(pivotIndex-i*s/n+sd);
    int right = (int)(pivotIndex+(n-i)*s/n+sd);
    if (left<start) left=start;
    if (stop<right) right=stop;
    if (start<left || right<stop) {
      partitionRangeExactly
        ( comparator, vArray, left, right
        , pivotIndex );
      return pivotIndex;
    } else {
      throw new IllegalArgumentException
                ( "partitionSample did *not*" 
                + " choose a smaller sample" );
    }
  }
  
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int sampleCount 
      = (int) Math.floor( Math.sqrt( (stop-start) / Math.log(2)));
    sampleCount += 1 - (sampleCount&1); //must be odd
    int sampleStart = start + (stop-start-sampleCount)/2;
    partitionRangeExactly 
      ( comparator, vArray
      , sampleStart, sampleStart+sampleCount
      , sampleStart + (sampleCount/2));
    return sampleStart + sampleCount / 2;
  }
}

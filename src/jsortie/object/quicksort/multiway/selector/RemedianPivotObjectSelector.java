package jsortie.object.quicksort.multiway.selector;

import java.util.Comparator;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class RemedianPivotObjectSelector<T> 
  implements SinglePivotObjectSelector<T>
           , MultiPivotObjectSelector<T> {
  protected MultiPivotObjectUtils<T> utils 
    = new MultiPivotObjectUtils<T>();
  protected ObjectInsertionSort<T> isort 
    = new ObjectInsertionSort<T>();
  int pivotCount = 1;
  public RemedianPivotObjectSelector() {
    this.pivotCount = 1;
  }
  public RemedianPivotObjectSelector
    ( int pivotCountToUse ) {
    this.pivotCount = pivotCountToUse;
  }
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  public int medianOf3Candidates
    ( Comparator<? super T> comparator
    , T [] vArray, int a, int b, int c) {
    if (comparator.compare(vArray[a],vArray[b])<=0) 
      if (comparator.compare(vArray[b],vArray[c])<=0)
        return b; //order was: abc
      else if (comparator.compare(vArray[a],vArray[c])<=0)
        return c; //order was: acb
      else
        return a; //order was: cab
    else if (comparator.compare(vArray[c],vArray[b])<0)
      return b;     //order was cba
    else if (comparator.compare(vArray[a],vArray[c])<=0)
      return a;     //order was bac
    else
      return c;	  //order was bca
  }
  public int selectCandidate
    ( Comparator<? super T> comparator
    , T [] vArray, int start, int count) {
    //assumed: count is power(3,k), 
    //         some integer k >= 0
    if (3<count) {
      int a = selectCandidate
              ( comparator, vArray
              , start, count/3 );
      int b = selectCandidate
              ( comparator, vArray
              , start+count/3, count/3);
      int c = selectCandidate
              ( comparator, vArray
              , start+2*count/3, count/3);
      return medianOf3Candidates
             ( comparator
             , vArray, a, b, c );
    } else if (count==3) {
      return medianOf3Candidates
             ( comparator, vArray
             , start, start+1, start+2 );
    } else {
      return start + (count)/2;
    }
  }	
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comp
    , T [] vArray, int start, int stop) {
    int count = stop - start;
    if      ( count < 32 )    { 
      return start + count/2; 
    } else if ( count < 81 )    { 
      return selectCandidate
             ( comp, vArray
             , start + (count-3)/2,  3  );
    } else if ( count < 729 )   { 
      return selectCandidate
             ( comp, vArray
             , start + (count-9)/2,  9  );
    } else if ( count < 6561 )  { 
      return selectCandidate
             ( comp, vArray
             , start + (count-27)/2, 27 );
    } else if ( count < 59049 ) { 
      return selectCandidate
             ( comp, vArray
             , start + (count-81)/2, 81 );
    } else {
      int m = 243;
      int m_squared_times_9 = 531441;
      while ( m_squared_times_9 <  count ) {
        m                 *= 3;
        m_squared_times_9 *= 9;
      }
      return selectCandidate
             ( comp, vArray
             , start + (count - m ) / 2, m );
    }
  }
  //This is rather... dirty
  @Override
  public int[] selectPivotIndices
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (pivotCount<2) { 
      return new int[] { 
        selectPivotIndex
          ( comparator, vArray
          , start, stop ) }; 
    }
    //Determine sample size
    int count = stop - start;
    if ( count < 9 * pivotCount ) {
      if ( count < pivotCount ) {
        throw new IllegalArgumentException
          ( "Cannot select " + pivotCount + " pivots "
          + "from only " + count + " elements");
      }
      return utils.sortEvenlySpacedIndices
             ( comparator, vArray
             , start, count, pivotCount );
    }
    int sample = 3 * pivotCount;
    while ( sample * sample * 9 <= count ) {
      sample *= 3;
    }
    int sampleStart 
      = start + (stop-start-sample) / 2; 
    int sampleStop  
      = sampleStart + sample;
    utils.collectPositionalSample
      ( vArray, start, stop
      , sampleStart, sampleStop );
    for ( int i = sampleStart
        ; i < sampleStop; i += pivotCount) {
      isort.sortRange
        ( comparator, vArray
        , i, i+pivotCount );
    }
    for (;sample>pivotCount;sample/=3) {
      int smaller = sample/3;
      for (int i=0; i<smaller; ++i) {
        sort3 ( comparator, vArray
              , sampleStart+i
              , sampleStart+smaller+i
              , sampleStart+smaller+smaller+i);
      }
      sampleStart += smaller;
      sampleStop  -= smaller;
      sample       = sampleStop-sampleStart;
    }
    int indices[] = new int[pivotCount];
    for (int i=0; i<pivotCount; ++i) {
      indices[i] = sampleStart+i;
    }
    return indices;
  }
  private void sort3
    ( Comparator<? super T> comparator
    , T[] vArray, int a, int b, int c) {
    if ( comparator.compare 
         ( vArray[a],vArray[b] ) <=0 ) 
      if ( comparator.compare
           ( vArray[b],vArray[c] ) <=0 ) { 
        //order was: abc, no exchanges needed
    } else if ( comparator.compare
                ( vArray[a], vArray[c] ) <= 0) {
      T v=vArray[b]; 
      vArray[b]=vArray[c]; 
      vArray[c]=v;   //order was: acb
    } else { 
      T v=vArray[a]; 
      vArray[a]=vArray[c]; 
      vArray[c]=vArray[b]; 
      vArray[b]=v; //order was: cab
    } else if ( comparator.compare
                ( vArray[c],vArray[b] ) < 0 ) {
      T v=vArray[a]; 
      vArray[a]=vArray[c]; 
      vArray[c]=v; //order was: cba
    } else if ( comparator.compare
                ( vArray[a], vArray[c] ) <=0 ) {
      T v=vArray[a]; 
      vArray[a]=vArray[b]; 
      vArray[b]=v; //order was: bac
    } else { 
      T v=vArray[a]; 
      vArray[a]=vArray[b]; 
      vArray[b]=vArray[c]; 
      vArray[c]=v; //order was: bca 
    } 
  }
}

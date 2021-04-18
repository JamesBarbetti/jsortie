package jsortie.mergesort.ternary;

import jsortie.StableRangeSorter;
import jsortie.mergesort.vanilla.MergesortBase;

public class TernaryMergesort
  extends MergesortBase {
  public TernaryMergesort
    ( StableRangeSorter janitor, int threshold ) {
    super(janitor, threshold);
  }	
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  protected int leftChildPartitionSize
    ( int parentPartitionSize ) {
    return parentPartitionSize / 3;
  }	
  protected int middleChildPartitionSize
    ( int parentPartitionSize ) {
    return parentPartitionSize / 3;
  }
  static public int copy
    ( int[] input, int aStart, int aStop
    , int[] output, int w) {
    int offset = w - aStart;
    for (; aStart < aStop; ++aStart) {
      output[ aStart+offset ] = input[aStart];
    }
    return 0;
  }
  static public int merge2 
    ( int[] input, int aStart, int aStop
    , int bStart, int bStop
    , int[] output, int w) {
    for (;;) {
      if ( input[aStart] <= input[bStart]) {
        output[w++] = input[aStart++];
        if (aStart==aStop) {
          return copy(input, bStart, bStop, output, w);
        }
      } else {
         output[w++] = input[bStart++];
         if (bStart==bStop) {
           return copy(input, aStart, aStop, output, w);
         }
      }
    }
  }
	
  enum mergeState { ABC, ACB, BAC, BCA, CAB, CBA };
	
  protected void merge3 ( int[] input, int aStart, int aStop
                        , int bStart, int bStop, int cStart, int cStop
                        , int[] output, int w) {
    mergeState state;
    if ( input[aStart] <= input[bStart])
      if ( input[bStart] <= input[cStart] )
        state = mergeState.ABC;
      else if (input[aStart] <= input[cStart] )
        state = mergeState.ACB;
      else
        state = mergeState.CAB;
    else if ( input[cStart] < input[bStart] )
      state = mergeState.CBA;
    else if ( input[aStart] <= input[cStart] )
      state = mergeState.BAC;
    else
      state = mergeState.BCA;

   for (;;) {
     switch (state) {
       case CBA: 
         do {
           output[w++]=input[cStart++];
           if (cStart==cStop) {
             merge2(input, aStart, aStop, bStart, bStop, output, w);
             return;
           }
         }
         while (input[cStart] < input[bStart]);
         if ( input[cStart] < input[aStart]) {
           state = mergeState.BCA;
           break;
         }
         //fall-through, from CBA, when next C >= A

       case BAC: 
         do {
           output[w++]=input[bStart++];
           if (bStart==bStop) {
             merge2(input, aStart, aStop, cStart, cStop, output, w);
             return;
           }
         } while (input[bStart] < input[aStart]);
         if ( input[cStart] < input[bStart]) {
           state = mergeState.ACB;
           break;
         }
         //fall-through, from BAC, when A <= next B <= C
				
       case ABC: 
         do {
           output[w++]=input[aStart++];
           if (aStart==aStop) {
             merge2(input, bStart, bStop, cStart, cStop, output, w);
             return;
           }
         } while (input[aStart] <= input[bStart]);
         if ( input[aStart] <= input[cStart]) {
           state = mergeState.BAC;
           break;
         }
         //fall-through, from ABC, when C <= next A
					
      case BCA: 
        do {
          output[w++]=input[bStart++];
          if (bStart==bStop) {
            merge2(input, aStart, aStop, cStart, cStop, output, w);
            return;
          }
        } while (input[bStart] <= input[cStart]);
        if ( input[bStart] < input[aStart]) {
          state = mergeState.CBA;
          break;
        }
        //fall-through, from BCA, when A <= next B

      case CAB: 
        do {
          output[w++]=input[cStart++];
          if (cStart==cStop) {
            merge2(input, aStart, aStop, bStart, bStop, output, w);
            return;
          }
        } while (input[cStart] < input[aStart]);
        if ( input[bStart] <= input[cStart]) {
          state = mergeState.ABC;
          break;
        }
        //fall-through, from CAB, when A<= next C <= B

      case ACB: 
        do {
          output[w++]=input[aStart++];
          if (aStart==aStop) {
            merge2(input, bStart, bStop, cStart, cStop, output, w);
            return;
          }
        } while (input[aStart] <= input[cStart]);
        if ( input[aStart] <= input[bStart])
          state = mergeState.CAB;
        else
          state = mergeState.CBA;
        break;				
      }			
    }		
  }
	
  protected void sortRangeUsing ( int[] vArray,    int start,     int stop
			                    , int[] vWorkArea, int workStart, int workStop) {
    int count     = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop);
    } else {
      int leftCount   = leftChildPartitionSize(count);
      if (leftCount<1) leftCount=1;
      int middleCount = middleChildPartitionSize(count);
      if (middleCount<1) middleCount=1;
      int lmCount     = leftCount+middleCount;
      sortRangeInto ( vArray,    start,               start+leftCount
                    , vWorkArea,  workStart );
      sortRangeInto ( vArray,    start+leftCount,     start+lmCount
    		        , vWorkArea, workStart+leftCount );
      sortRangeInto ( vArray,    start+lmCount,       stop
                    , vWorkArea, workStart+lmCount );
      merge3        ( vWorkArea, workStart,           workStart+leftCount
                    , workStart+leftCount, workStart+lmCount
                    , workStart+lmCount,   workStart+count
                    , vArray   , start);
    }
  }
	
  protected void sortRangeInto ( int[] vSourceArray, int start, int stop
                               , int[] vDestArray,   int destStart) {	
    int count = stop-start;
    if (count<janitorThreshold) {
      copyAndSortSmallRange(vSourceArray, start, stop, vDestArray, destStart);
    } else {
      int leftCount   = leftChildPartitionSize(count);
      if (leftCount<1) leftCount=1;
      int middleCount = middleChildPartitionSize(count);
      if (middleCount<1) middleCount=1;
      int lmCount     = leftCount+middleCount;
      int destStop    = destStart + (stop-start);
      sortRangeUsing( vSourceArray, start,           start+leftCount,  vDestArray, destStart,           destStart+leftCount);
      sortRangeUsing( vSourceArray, start+leftCount, start+lmCount,    vDestArray, destStart+leftCount, destStart+lmCount);
      sortRangeUsing( vSourceArray, start+lmCount,   stop,             vDestArray, destStart+lmCount,   destStop);
      merge3        ( vSourceArray, start,           start+leftCount
                            , start+leftCount, start+lmCount
                            , start+lmCount,   stop
                    , vDestArray  , destStart);
    }
  }
}

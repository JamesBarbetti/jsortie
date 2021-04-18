package jsortie.mergesort.ternary;

import jsortie.StableRangeSorter;

public class TernaryMergesort2 extends TernaryMergesort {
  public TernaryMergesort2(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }	
  protected class Merge {
    int[] source;
    int   a;
    int   aStop;
    int   b;
    int   bStop;
    int   c;
    int   cStop;
    int[] dest;
    int   w;
		
    class MergeState { MergeState step() { return null; } }
    MergeState stateABC = new MergeState() { MergeState step() {
      do {
        dest[w++]=source[a++];
        if (aStop<=a) {
          merge2(source, b, bStop, c, cStop, dest, w);
          return null;
        }
      } while (source[a] <= source[b]);
      return( source[a] <= source[c]) ? stateBAC : stateBCA;
     } };
     MergeState stateACB = new MergeState() { MergeState step() { 
       do {
         dest[w++]=source[a++];
         if (aStop<=a) {
           merge2(source, b, bStop, c, cStop, dest, w);
           return null;
         }
       } while (source[a] <= source[c]);
       return ( source[a] <= source[b]) ? stateCAB : stateCBA;
     } };
     MergeState stateCAB = new MergeState() { MergeState step() { 
       do {
         dest[w++]=source[c++];
         if (cStop<=c) {
           merge2(source, a, aStop, b, bStop, dest, w);
           return null;
         }
       } while (source[c] < source[a]);
       return ( source[b] <= source[c]) ? stateABC : stateACB;
     } };
     MergeState stateCBA = new MergeState() { MergeState step() {
       do {
         dest[w++]=source[c++];
         if (cStop<=c) {
           merge2(source, a, aStop, b, bStop, dest, w);
           return null;
         }
       } while (source[c] < source[b]);
       return ( source[c] < source[a]) ? stateBCA : stateBAC;
     } };
     MergeState stateBAC = new MergeState() { MergeState step() { 
       do {
         dest[w++]=source[b++];
         if (bStop<=b) {
           merge2(source, a, aStop, c, cStop, dest, w);
           return null;
         }
       } while (source[b] < source[a]);
       return ( source[c] < source[b]) ? stateACB: stateABC;
     } };
     MergeState stateBCA = new MergeState() { MergeState step() { 
       do {
         dest[w++]=source[b++];
         if (bStop<=b) {
           merge2(source, a, aStop, c, cStop, dest, w);
           return null;
         }
       } while (source[b] <= source[c]);
       return ( source[b] < source[a]) ? stateCBA : stateCAB;
     } };
		
     void  run() {
       MergeState state;
       if ( source[a] <= source[b])
         if ( source[b] <= source[c] )
           state = stateABC;
         else if (source[a] <= source[c] )
           state = stateACB;
         else
           state = stateCAB;
       else if ( source[c] < source[b] )
         state = stateCBA;
       else if ( source[a] <= source[c] )
         state = stateBAC;
       else
         state = stateBCA;
			
       do {
         state = state.step();
       }
       while (state!=null);
     }
  }	
	
  protected void merge3 ( int[] input, int aStart, int aStop
                        , int bStart, int bStop, int cStart, int cStop
                        , int[] output, int outputStart) {
    Merge merge  = new Merge();
    merge.source = input;
    merge.a      = aStart;
    merge.aStop  = aStop;
    merge.b      = bStart;
    merge.bStop  = bStop;
    merge.c      = cStart;
    merge.cStop  = cStop;
    merge.dest   = output;
    merge.w      = outputStart;		
    merge.run();
  }
}

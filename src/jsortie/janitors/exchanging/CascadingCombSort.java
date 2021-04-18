package jsortie.janitors.exchanging;
import jsortie.janitors.insertion.InsertionSort;

public class CascadingCombSort
  extends Combsort {
  //
  //A version of Combsort that processes 
  //combsort passes "in parallel", as
  //much as possible.  Probably this is going 
  //too far, and it would be better to do 
  //fewer passes at a time (no more than, 
  //say, eight).  The array look-ups into 
  //gap[] probably don't help either.
  //In practice, for sorting integers, 
  //this is about 2/3 as fast as a standard
  //Combsort.
  //
  public CascadingCombSort() {
    super(1.3);
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count = stop - start;
    int lastGapIndex = 0;
    while( lastGapIndex+1<gaps.length
           && gaps[lastGapIndex+1] < count ) {
      ++lastGapIndex;
    }
    int j = start + gaps[lastGapIndex];
    for ( ; 0 <= lastGapIndex; --lastGapIndex ) {
      for (; j<stop; ++j) {
        int g=lastGapIndex;
        int h=j;
        do {
          int gap= gaps[g];
          h -= gap;
          if (h<start) {
            break;
          }
          int i = h + gap;
          int vLeft = vArray[h];
          int vRight = vArray[i];
          if (vRight<vLeft) {
            vArray[h] = vRight;
            vArray[i] = vLeft;
          }
          --g;
        } while (0<=g);
      }
      j -= gaps[lastGapIndex];
    }
    InsertionSort.sortSmallRange
      ( vArray, start, stop );
  }
}

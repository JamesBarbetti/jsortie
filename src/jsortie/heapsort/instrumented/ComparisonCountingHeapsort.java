package jsortie.heapsort.instrumented;

import jsortie.janitors.insertion.InsertionSort;

public class ComparisonCountingHeapsort
  extends ComparisonCountingHeapsortBase {
  //Note: this uses bottom-up heap construction and extraction,
  boolean overshot = false;  
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - 2;
    for (int k = start+(stop-start-radix-1)/2; k>=start ; --k) {
      int i = k;
      int v = vArray[i];
      int j = i + i - fudge;
      do {
        if (j+1<stop) {
          ++constructionComparisonCount;
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;
      } while (j<stop);
      int lifts = siftUp(vArray, start, k, fudge, i, v);
      constructionLiftCount       += lifts;
      constructionLiftedItemCount += (0<lifts) ? 1 : 0;
      constructionComparisonCount += lifts + (overshot ? 0 : 1);
    }
  }  
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
  int fudge= start - 2;
    //heap extraction phase
    int firstChild=start+2;
    for (--stop;stop>=firstChild;stop--) {
      int v = vArray[stop];   
      int i = stop;     
      int j = start;
      //extract, assuming v will go into a bottom-level node
      do {
        if (j+1<stop) {
          ++extractionComparisonCount;
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i + i - fudge;
      } while (j<stop);
      int lifts = siftUp(vArray, start, start, fudge, i, v);
      sourcePositionToLiftCount[stop] += lifts;
      extractionLiftCount       += lifts;
      extractionLiftedItemCount += (0<lifts) ? 1 : 0;
      extractionComparisonCount += lifts + (overshot ? 0 : 1);
    }    
    InsertionSort.sortSmallRange(vArray, start, firstChild);
  }
  public int siftUp
    ( int[] vArray, int start, int top, int fudge, int i, int v ) {
    //search back up the path toward the top of the heap, to place v
    overshot = false;
    int g = (i-fudge)/radix + start - 2;
    int lifts = 0;
    while (top<=g && vArray[g]<v) {
      vArray[i] = vArray[g];
      i = g;
      g = (i-fudge)/radix + start - 2;
      ++lifts;
    }
    overshot = (g<top);
    vArray[i] = v;
    return lifts;
  }
}

package jsortie.heapsort.instrumented;

import jsortie.janitors.insertion.InsertionSort;

public class ComparisonCountingHeapsortRadix3 
  extends ComparisonCountingHeapsort {
  public ComparisonCountingHeapsortRadix3() {
    radix = 3;
  }
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - radix;
    for (int k = start + (stop-start-radix-1)/radix; k>=start ; --k) {
      int i = k;
      int v = vArray[i];
      int j = i + i + i - fudge;  
      do {
        if (j+2<stop) {
          constructionComparisonCount +=2;
          int j2 =  vArray[j+1]<vArray[j+2] ? 2 : 1;
          j  += vArray[j] < vArray[j+j2] ? j2 : 0;
        }
        else if (j+1<stop) {
          ++constructionComparisonCount;
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i + i + i - fudge;
      } while (j<stop);
      
      int lifts = siftUp(vArray, start, k, fudge, i, v);
      constructionLiftCount       += lifts;
      constructionLiftedItemCount += (0<lifts) ? 1 : 0;
      constructionComparisonCount += lifts + (overshot ? 0 : 1);
    }
  }  
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - radix;
    //heap extraction phase
    int firstChild=start+3;
    for (--stop;stop>=firstChild;stop--) {
      int v = vArray[stop];
      int i = stop;
      int j = start;
      //extract, assuming v will go into a bottom-level node
      do {
        if (j+2<stop) {
          extractionComparisonCount +=2;
          int j2 =  vArray[j+1]<vArray[j+2] ? 2 : 1;
          j  += vArray[j] < vArray[j+j2] ? j2 : 0;
        } 
        else if (j+1<stop) {
          ++extractionComparisonCount;
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i + i + i - fudge;
      } while (j<stop);
      int lifts = siftUp(vArray, start, start, fudge, i, v);
      sourcePositionToLiftCount[stop] += lifts;
      extractionLiftCount       += lifts;
      extractionLiftedItemCount += (0<lifts) ? 1 : 0;
      extractionComparisonCount += lifts + (overshot ? 0 : 1);
    }    
    InsertionSort.sortSmallRange(vArray, start, firstChild);
  }
}

package jsortie.heapsort.instrumented;

import jsortie.janitors.insertion.InsertionSort;

public class ComparisonCountingHeapsortTopDown 
  extends ComparisonCountingHeapsortBase {
  //Note: this uses top-down heap construction and extraction,
  //as per Floyd's Algorithm 245: Treesort3 (of 1964). 
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - 2;
    for (int k = start+(stop-start-3)/2; k>=start ; --k) {
      int i = k;
      int v = vArray[i];
      int j = i + i - fudge;
      do {
        if (j+1<stop) {
          ++constructionComparisonCount;
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        ++constructionComparisonCount;
        if ( vArray[j] < v ) {
          break;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;
      } while (j<stop);
      vArray[i] = v;
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
        ++extractionComparisonCount;
        if ( vArray[j] < v ) {
          break;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i + i - fudge;
      } while (j<stop);
      vArray[i] = v;
    }
    InsertionSort.sortSmallRange(vArray, start, firstChild);
  }

}

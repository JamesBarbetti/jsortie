package jsortie.heapsort.topdown;

import jsortie.RangeSorter;
import jsortie.heapsort.HeapsortBase;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;

public class TwoAtATimeHeapsort 
  extends HeapsortBase {
  //absolutely, categorically, *must* be at least 6
  protected int threshold       = 6; 
  protected RangeSorter janitor = new InsertionSort2Way();
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    int cutOff = start + threshold;
    if ( cutOff < stop ) {
      constructHeap(vArray, start, stop);
      extractFromHeap(vArray, start, stop);
    } else {
      cutOff = stop;
    }
    janitor.sortRange(vArray, start, cutOff);
  }
  @Override
  public void constructHeap
    ( int[] vArray, int start, int stop) {
    int fudge    = 2 - start;
    int evenStop = stop - ( (stop - start) & 1 );
    for ( int h = start+(evenStop-start-4)/2
        ; h>=start
        ;--h) {
      sift( vArray, h, h + h + fudge,  fudge, evenStop );
    }
    if (evenStop<stop) {
      sift( vArray, evenStop,   start, fudge, evenStop) ;
    }
    int cutOff = start + threshold;
    if ( cutOff < stop ) {
      stop = cutOff;
    } else {
      cutOff = stop;
    }
  }
  @Override 
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
    int cutOff = start + threshold;
    if ( cutOff < stop ) {
      int fudge    = 2 - start;
      int evenStop = stop - ( (stop - start) & 1 );
      for (evenStop-=2;cutOff<=evenStop;evenStop-=2) {
        sift( vArray, evenStop+1, start, fudge, evenStop );
        sift( vArray, evenStop,   start, fudge, evenStop );
      }
    }
  }
  protected void sift ( int[] vArray, int i, int j
                      , int fudge, int stop) {
    int v=vArray[i];		
    do {    	
      j+= (vArray[j]<vArray[j+1]) ? 1 : 0;
      if (vArray[j]<=v)  {
        vArray[i] = v;
        return;
      }      
      vArray[i] = vArray[j];
      i = j + j + fudge;

      if (stop<=i) {
        vArray[j] = v;
        return;
      }
      
      i+= (vArray[i]<vArray[i+1]) ? 1 : 0;
      if (vArray[i]<=v) {
        vArray[j] = v;
        return;
      }
      vArray[j] = vArray[i];
      j = i + i + fudge;
    } while (j<stop);
    vArray[i] = v;
  }	
}

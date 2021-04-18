package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class Java8Selector implements MultiPivotSelector {
  protected DirtySelectorHelper dsh = new DirtySelectorHelper();
  @Override public String toString() {
	  return this.getClass().getSimpleName();
  }
  @Override public int[] selectPivotIndices
    (int[] vArray, int start, int stop) {
    int count = (stop-start);
    int s     = ((count << 3) + count + 55) >> 6 ;    
    int c     = start + (count >> 1);
    int b     = c - s;
    int a     = b - s;
    int d     = c + s;
    int e     = d + s;
    
    //Gruesome five-element insertion sort appears here.
    
    int v;    
    if (vArray[b] < vArray[a]) { 
      v = vArray[b]; 
      vArray[b] = vArray[a]; 
      vArray[a] = v; 
    }
    if (vArray[c] < vArray[b]) { 
      v = vArray[c]; 
      vArray[c] = vArray[b]; 
      if (v < vArray[a]) { 
        vArray[b] = vArray[a]; 
        vArray[a] = v; 
      } else {
        vArray[b] = v;
      }
    }
    if (vArray[d] < vArray[c]) { 
      v = vArray[d]; 
      vArray[d] = vArray[c]; 
      if (v < vArray[b]) { 
        vArray[c] = vArray[b]; 
        if (v < vArray[a]) { 
          vArray[b] = vArray[a]; 
          vArray[a] = v; 
        } else {
          vArray[b] = v;
        }
      } else {
        vArray[c] = v;
      }
    }
    if (vArray[e] < vArray[d]) { 
      v = vArray[e]; 
      vArray[e] = vArray[d]; 
      if (v < vArray[c]) { 
        vArray[d] = vArray[c]; 
        if (v < vArray[b]) { 
          vArray[c] = vArray[b]; 
          if (v < vArray[a]) { 
            vArray[b] = vArray[a]; 
            vArray[a] = v; 
          } else {
            vArray[b] = v;
          }
        } else {
          vArray[c] = v;
        }
      } else {
        vArray[d] = v;
      }
    } 
    return new int[] { a, b, c, d, e };
  }
}

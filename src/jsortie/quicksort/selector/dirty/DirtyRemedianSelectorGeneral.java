package jsortie.quicksort.selector.dirty;

import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtyRemedianSelectorGeneral
  implements SinglePivotSelector {
  protected boolean uniform=false;
  protected int g = 3; 
		//g *must* be odd, and greater than 2.
		//but should be 3, 5, or 7.  Higher values perform badly
  protected HeapsortStandard hs = new HeapsortStandard();
  protected DirtySelectorHelper helper = new DirtySelectorHelper();
	
  public DirtyRemedianSelectorGeneral()      { }
  public DirtyRemedianSelectorGeneral(int g) { this.g = g + ((g&1)==0 ? 1 : 0); }
  public DirtyRemedianSelectorGeneral(int g, boolean isUniform) { 
    this.g = g + ((g&1)==0 ? 1 : 0); 
    this.uniform = isUniform;
  }
  @Override public String toString() { 
    return this.getClass().getSimpleName() + "(" 
      + (uniform ? "uniform" : "non-uniform") 
      + ((3<g) ? ( ", step" + g ) : "")
      + ")"; 
  }
  public int getSampleSize(int count, int radix) {
    int m = 1;
    while ( m <= Math.sqrt(count)) {
      m*=g;
    }
    return m;
  }
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int count = stop-start;
    int m = getSampleSize(count, 2);
    int c = start + count/2;
    for (int gap=m/g ; m>=g; m=gap, gap=m/g) {
      int r = c+m/2+1;
      r = ( stop<=r ) ? (stop-1) : r;
      helper.sortColumns(vArray, c-m/2, r, gap);
    }
    hs.sortRange(vArray, c-m/2, c+m/2);    
    return c;
  }
}

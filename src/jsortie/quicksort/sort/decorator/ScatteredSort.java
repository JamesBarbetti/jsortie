package jsortie.quicksort.sort.decorator;

import jsortie.RangeSorter;
import jsortie.helper.RotationHelper;

public class ScatteredSort implements RangeSorter {
  protected RangeSorter innerSort;
  static double reciprocalOfPhi = 2 / (1 + Math.sqrt(5));
  public RotationHelper helper = new RotationHelper();  
  public ScatteredSort
    ( RangeSorter sortThatWantsScatteredInput) {
    innerSort = sortThatWantsScatteredInput;
  }	
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + innerSort.toString() + ")";
  }  
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (start+1<stop)
    {
      scatter(vArray, start, stop);
    }
    innerSort.sortRange(vArray,  start, stop);
  }
  protected void scatter(int[] vArray, int start, int stop) {
   //it is assumed that stop-start>1 (any public method that 
   //calls this should ensure that is so)
   int count = stop - start;
   int jump  
     = ((int) Math.floor( reciprocalOfPhi * count + .5 )) 
     % count;
   while (1<helper.gcd(count, jump)) {
	 --jump;
   }
   int j=start+jump;
   for (int i=start;i<stop;++i) {
	 int vTemp = vArray[i];
	 vArray[i] = vArray[j];
	 vArray[j] = vTemp;
     j+=jump;
     if (stop<=j) j-=count;
   }
  }
}

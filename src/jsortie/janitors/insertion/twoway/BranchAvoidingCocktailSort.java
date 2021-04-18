package jsortie.janitors.insertion.twoway;


public class BranchAvoidingCocktailSort extends CocktailSort {
  @Override
  public int shuffleMinimumToLeft(int[] vArray, int start, int stop) {
   int vMin = vArray[stop-1];
   for ( int i = stop-2; start<=i; --i) {
	 int     vLift = vArray[i];
     boolean drop  = ( vLift <= vMin );
     vArray[i+1]   = drop ? vMin  : vLift;
     vMin          = drop ? vLift : vMin;
   }
   vArray[start] = vMin;
   return start+1;
  }
  @Override
  public int shuffleMaximumToRight(int[] vArray, int start, int stop) {
    int vMax = vArray[start];
    for (int i = start+1; i<stop; ++i) {
      int vLift = vArray[i];
      boolean drop = ( vMax <= vLift );
      vArray[i-1]  = drop ? vMax  : vLift;
      vMax         = drop ? vLift : vMax;
    }
    vArray[stop-1] = vMax;
    return stop-1;
  } 
}

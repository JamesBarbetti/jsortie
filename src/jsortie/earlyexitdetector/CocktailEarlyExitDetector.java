package jsortie.earlyexitdetector;

import jsortie.janitors.insertion.twoway.CocktailSort;

public class CocktailEarlyExitDetector
  implements RangeSortEarlyExitDetector {
  protected CocktailSort cocktail;
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public boolean exitEarlyIfSorted
    ( int[] vArray, int start, int stop ) {
    stop = cocktail.shuffleMaximumToRight
           ( vArray, start, stop );
    if ( start == stop ) {
      return true;
    }
    start = cocktail.shuffleMinimumToLeft
            ( vArray, start, stop );  
	return ( start == stop );
  }
}

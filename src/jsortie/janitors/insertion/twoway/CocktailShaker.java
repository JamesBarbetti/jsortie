package jsortie.janitors.insertion.twoway;

public interface CocktailShaker {
  public int shuffleMinimumToLeft (int[] vArray, int start, int stop);
  public int shuffleMaximumToRight(int[] vArray, int start, int stop);
}

package jsortie.janitors;

import jsortie.RangeSorter;

public class StrategicJanitorSort implements RangeSorter {
  protected RangeSorter main;
  protected RangeSorter janitor;
	
  public StrategicJanitorSort(RangeSorter mainSorter, RangeSorter janitorSorter) {
    main = mainSorter;
    janitor = janitorSorter;
  }
	
  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" 
           + main.toString() + "," + janitor.toString() + ")";
  }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    main.sortRange(vArray, start, stop);
    janitor.sortRange(vArray, start, stop);
  }
}

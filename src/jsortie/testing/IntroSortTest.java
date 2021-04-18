package jsortie.testing;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.governor.introsort.EnteroSort;
import jsortie.quicksort.governor.introsort.ExtroSort;
import jsortie.quicksort.governor.introsort.IntroSort;
import jsortie.quicksort.governor.introsort.MindYourHeadSort;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class IntroSortTest {
  @Test
  public void testIntroSorts() {
    SinglePivotSelector    s = new MiddleElementSelector();
    SinglePivotPartitioner p = new SkippyPartitioner();
    RangeSorter            i = new InsertionSort();
    int                    t = 5;
    RangeSorter            l = new HeapsortStandard();	
    SortList list = new SortList();
    list.add( new IntroSort(s,p,i,t,l));
    list.add( new ExtroSort(s,p,i,t,l));
    list.add( new EnteroSort(s,p,i,t,l));
    list.add( new MindYourHeadSort(s,p,i,t,l));
    MindYourHeadSort x = new MindYourHeadSort(s,p,i,t,l);
    System.out.println(x.getComparisonSpend(0, 500, 1001));
    System.out.println(x.getComparisonSpend(0, 100, 1001));
    System.out.println(2*Math.log(2)-1.0);
    System.out.println((0.5772157-1)/Math.log(2.0));
    System.out.println(0.5772157/Math.log(2.0));
    System.out.println(1.0/Math.log(2.0));
    System.out.println( (-1 + 16*(1-0.5772157)/15.0 ) / Math.log(2.0));
    System.out.println((1.4428 - 0.8328 ) / (1.4428 - 1.2645));
    double xx=0;
    for (double ii=3.0;ii<2000000000;ii=ii*2+1) {
      xx+= 0.5/ii;
      System.out.println(xx);
    }
    System.out.println(Math.exp(0.30334757574198445));
  }
}

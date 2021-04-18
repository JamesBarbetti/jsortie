package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jsortie.RangeSorter;
import jsortie.exception.SortingFailureException;
import jsortie.flags.QuadraticAverageCase;
import jsortie.helper.DumpRangeHelper;
import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class SortTest {
  static final int billion = 1000000000;
  static final int million = 1000000;
  static final int thousand = 1000;
  protected DegenerateInput degen = new DegenerateInput();
  protected RandomInput random = new RandomInput();
  static final int runCount(int n) { return (n<150000) ? 25 : 1; }
  public void testRandomInputs ( List<RangeSorter> sorts, int smallN
                               , int highN, boolean rawTimes) {
    boolean failed = false;
    int latin = 0;
    for (int n = smallN; n < highN; n = n * 11 / 10) {
      long totalTime[] = new long[sorts.size()];
      for (int i=0; i<sorts.size(); ++i) {
        totalTime[i]=0;
      }
      for (int run=0; run<runCount(n) &!failed ; ++run) {
        int input[] = random.randomPermutation(n);
        int copy[] = new int[n];
        for (int s = 0; s < sorts.size(); ++ s) {
          int sort = ((s + latin) % (sorts.size()));
          RangeSorter sorter = sorts.get(sort);
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          long stopTime = System.nanoTime();
          totalTime[sort] +=  stopTime - startTime;
          for (int k = 0; k < n; ++k) {
            if (k!=copy[k]) {
              System.out.println ( " Sort " + sort + "(" + sorter.toString() + ")" 
                + " failed for n=" + n + ";" 
                + " copy[" + k + "] was " + copy[k] );
              if (n<100) {
                DumpRangeHelper.dumpRange("input  ", input,  0, input.length);
                DumpRangeHelper.dumpRange("output ", copy,   0, input.length);
              }
              failed = true;
              throw new SortingFailureException
                        ("Sort " + sorter.toString() + " failed for n=" + n);
            }
          }
        }
        --latin;
        if (latin<0) {
          latin+=sorts.size();
        }
      }
      String s = Integer.toString(n);      
      if (!failed) {
        double bits = (double)n * Math.log(n) / Math.log(2) 
                   * (double)runCount(n);
        for (int i=0; i<sorts.size(); ++i) {
          if (rawTimes) {
            s+= "\t" + ((double)totalTime[i]/runCount(n))/billion;				
          } else {
            s+= "\t" + Math.floor( bits / totalTime[i] * 1000.0 + .5) / 1000.0;
          }
        }
        System.out.println ( s);
      }
    }		
  }
  public void testOnDuplicates(List<RangeSorter> sorts, int n) {
	//output is: mean milliseconds elapsed
    long totalTime[] = new long[sorts.size()];
    int latin = 0;
    int digits = ("" + n).length();
    for ( int d=1; d<n; d=((d<10)?(d+1):(d*11/10)) ) {
      for (int i=0; i<sorts.size(); ++i) {
        totalTime[i]=0;
      }
      for (int run=0; run<runCount(n); ++run) {
        int input[] = degen.permutationOfDupes(n,d);
        int copy[] = new int[n];
        int check[] = Arrays.copyOf(input, input.length);
        Arrays.sort(check);
        for (int s = 0; s < sorts.size() ; ++ s) {
          int sort = ((s + latin) % (sorts.size()));
          RangeSorter sorter = sorts.get(sort);
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          long stopTime = System.nanoTime();
          totalTime[sort] +=  stopTime - startTime; 
          for (int i=0; i<n; ++i) {
            if ( check[i] != copy[i] ) {                      
              throw new SortingFailureException("Sort " + sort + " "
                + " (" + sorter.toString() + ") failed"
                + " on " + d + " valued permutation: output[" + i + "]"
                + " was " + copy[i] + ", should have been " + check[i]);
            }
          }
          
        }
        --latin;
        if (latin<0) {
          latin+=sorts.size();
        }
      }
      String s = "Dupes " + d;
      for (int x=digits-(""+d).length();0<x;--x) {
        s+=" ";
      }
      for (int i=0; i<sorts.size(); ++i) {
        s+= "\t" + Math.floor(((double)totalTime[i]/runCount(n))/thousand+.5)/thousand;
      }
      System.out.println ( s);
    }
  }
  public void testOnOrderedInputs(List<RangeSorter> sorts, int n, String unit) {
    long totalTime[] = new long[sorts.size()];
    double divisor = million; //seconds
    if (unit.equals("millisecond")) divisor = thousand;
    if (unit.equals("microsecond")) divisor = 1;
    for ( int reversed=0; reversed<2; ++reversed) {
      for ( int d=1; d<=n; d=(d<10)?(d+1):(d*11/10) ) {
        for (int i=0; i<sorts.size(); ++i) {
          totalTime[i]=0;
        }
        int latin = 0;
        for (int run=0; run<runCount(n); ++run) {
          int input[] = degen.postRandomAppendPermutation(n,d);
          if (reversed!=0) {
            for (int i=0; i<(n-d)/2; ++i) {
              int x = input[i];
              input[i] = input[n-d-i];
              input[n-d-i] = x;
            }
          }
          int copy[] = new int[n];
          for (int s = 0; s < sorts.size(); ++ s) {
            int sort = ((s + latin) % (sorts.size()));
            RangeSorter sorter = sorts.get(sort);
            for (int i=0; i<n; ++i) {
              copy[i] = input[i];
            }						
            long startTime = System.nanoTime();
            sorter.sortRange(copy, 0, copy.length);
            long stopTime = System.nanoTime();
            totalTime[sort] +=  stopTime - startTime;
          }
          --latin;
          if (latin<0) {
            latin+=sorts.size();
          }
        }
        String s = ((reversed>0) ? "Reversed " : "Ordered ")
                 + Integer.toString(d);
        for (int i=0; i<sorts.size(); ++i) {
          double timePerRun = (double)totalTime[i]/runCount(n);
          s+= "\t" + Math.floor (timePerRun/divisor) /thousand;
        }
        System.out.println ( s);
      }
    }
  }
  public void testOnUpdatedInputs(List<RangeSorter> sorts, int n) {
    long totalTime[] = new long[sorts.size()];
    int latin = 0;
    for ( int d=1; d<n; d=(d<10)?(d+1):(d*11/10) ) {
      for (int i=0; i<sorts.size(); ++i) {
        totalTime[i]=0;
      }
      for (int run=0; run<runCount(n); ++run) {
        int input[] = degen.postRandomUpdatePermutation(n,d);
        int copy[] = new int[n];
        for (int s = 0; s < sorts.size(); ++ s) {
          int sort = ((s + latin) % (sorts.size()));
          RangeSorter sorter = sorts.get(sort);
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          long stopTime = System.nanoTime();
          totalTime[sort] +=  stopTime - startTime;
        }
        --latin;
        if (latin<0) {
          latin+=sorts.size();
        }
      }
      String s = Integer.toString(d);
      for (int i=0; i<sorts.size(); ++i) {
        s+= "\t" + ((double)totalTime[i]/runCount(n))/billion;
      }
      System.out.println ( s);
    }
  }
  public void testOnExchangedInputs(List<RangeSorter> sorts, int n) {
    long totalTime[] = new long[sorts.size()];
    int latin = 0;
    for ( int d=1; d<n; d=(d<10)?(d+1):(d*11/10) ) {
      for (int i=0; i<sorts.size(); ++i) {
        totalTime[i]=0;
      }
      for (int run=0; run<runCount(n); ++run) {
        int input[] = degen.postRandomExchangePermutation(n,d);
        int copy[] = new int[n];
        for (int s = 0; s < sorts.size(); ++ s) {
          int sort = ((s + latin) % (sorts.size())) ;
          RangeSorter sorter = sorts.get(sort);
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          long stopTime  = System.nanoTime();
          totalTime[sort] +=  stopTime - startTime;
        }
        --latin;
        if (latin<0) {
          latin+=sorts.size();
        }
      }
      String s = Integer.toString(d);
      for (int i=0; i<sorts.size(); ++i) {
        s+= "\t" + ((double)totalTime[i]/runCount(n))/billion;
      }
      System.out.println ( s);
    }
  }	
  public void testOnStandardInputs(List<RangeSorter> sorts, int count) {
    System.out.println("Random");		
    testRandomInputs(sorts, count, count+1, true);
    System.out.println("Duplicates");
    testOnDuplicates(sorts, count);
    System.out.println("Ordered");
    testOnOrderedInputs(sorts, count, "second");
    System.out.println("Updated");
    testOnUpdatedInputs(sorts, count);
    System.out.println("Exchanged");
    testOnExchangedInputs(sorts, count);
  }
  public void warmUpSort(RangeSorter sorter, int n, int runs) {
    int input[] = random.randomPermutation(n);
    for (int r=0; r<runs; ++r) {
      int copy[] = new int[n];
      for (int i=0; i<n; ++i) {
        copy[i] = input[i];
      }
      sorter.sortRange(copy, 0, copy.length);
    }
  }
  public void warmUpSorts(List<RangeSorter> sorts, int n, int runs) {
    int smallerN = (int) Math.floor( Math.sqrt(n));
    for (int s=0; s<sorts.size();++s) {
      RangeSorter sorter = sorts.get(s);
      int nHere =  (sorter instanceof QuadraticAverageCase) ? smallerN : n;
      warmUpSort( sorter, nHere, runs);
    }
  }
  public void testSpecificSorts(List<RangeSorter> sorts
    , int start, int stop1, int runs1, int stop2, int runs2) {			
    int sortCount = sorts.size();
    for (int n=start; n<stop2; n=n*11/10) {
      int runs = (n < stop1) ? runs1 : runs2 ;
      int copy[] = new int[n];
      double comparisonsNeeded = (double)n * Math.log(n) / Math.log(2.0) - (double)n / Math.log(2.0);
      double times[] = new double[sorts.size()]; //in nanoseconds
      for (int r = 0; r<runs; ++r) {
        int input[] = random.randomPermutation(n);
        for (int s=0; s<sorts.size();++s) {
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          int sortNo = (s + r) % sortCount;
          RangeSorter sorter = sorts.get(sortNo);
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, n);
          long stopTime = System.nanoTime();					
          times[sortNo] += (double)(stopTime - startTime);
          for (int check=0; check<copy.length; ++check) {
            if (copy[check]!=check) {
              String problem = "Sort " + sortNo + " (" + sorter.toString() + ") "
                     + "has failed to sort " + n + "-element random permutation;\n"
                     + "item at [" + check + "] should be " + check + " but is "
                     + copy[check];              
              System.out.println(problem);
              if (copy.length<50) {
                DumpRangeHelper.dumpRange("output", copy, 0, copy.length);
              }
              throw new SortingFailureException(problem);
            } //check failed
          } //check loop
        } // per sort
      } // per run
      String line = "" + n;
      for (int s=0; s<sorts.size();++s) {
        line +=  "\t" + Math.floor(comparisonsNeeded*runs / times[s] * 1000.0) / 1000.0;			
      }
      System.out.println(line);
    }
  }	
  public void testSpecificSorts(List<RangeSorter> sorts, int start, int stop, int runs) {
    testSpecificSorts( sorts, start, stop, runs, stop, runs);
  }
  public void testSpecificSortsOnOrderedInput(List<RangeSorter> sorts, int start, int stop, int runs, int order) {
    //order 1 in order, -1 in reverse order
    for (int n=start; n<stop; n=n*11/10) {
      int copy[] = new int[n];
      double comparisonsNeeded = (double)n * (Math.log(n) - 1.0) / Math.log(2.0);
      double times[] = new double[sorts.size()]; //in nanoseconds
      for (int r = 0; r<runs; ++r) {
        int input[] = DegenerateInput.identityPermutation(n);
        if (order<0) RangeSortHelper.reverseRange(input, 0, input.length);
        for (int s=0; s<sorts.size();++s) {
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          RangeSorter sorter = sorts.get(s);
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          times[s] += (double)(System.nanoTime() - startTime);
        }
      }
      String line = "" + n;
      for (int s=0; s<sorts.size();++s) {
    	double efficiency = comparisonsNeeded*(double)runs / times[s];
        line +=  "\t" + Math.floor(efficiency * 1000.0) / 1000.0;			
      }
      System.out.println(line);
    }
  }
  public void testSpecificSortsShowPowerbands 
    ( List<RangeSorter> sorts, int start, int stop, int runs) {
    double comparisonsNeededLastTime = 0;
    double lastTimes[] = new double[sorts.size()]; //in nanoseconds
    for (int n=start; n<stop; n=n*11/10) {
      int copy[] = new int[n];
      double comparisonsNeeded = (double)n * (Math.log(n)  - 1.0) / Math.log(2.0);
      double times[] = new double[sorts.size()]; //in nanoseconds
      for (int r = 0; r<runs; ++r) {
        int input[] = random.randomPermutation(n);
        for (int s=0; s<sorts.size();++s) {
          for (int i=0; i<n; ++i) {
            copy[i] = input[i];
          }
          RangeSorter sorter = sorts.get(s);
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          times[s] += (double)(System.nanoTime() - startTime);
        }
      }
      String line = "" + n;
      for (int s=0; s<sorts.size();++s) {
        double efficiency = comparisonsNeeded*runs / times[s];
        line += "\t" + Math.floor( efficiency * 1000.0) / 1000.0;			
      }
      for (int s=0; s<sorts.size();++s) {				
        double deltaComparisons = comparisonsNeeded - comparisonsNeededLastTime;
        double deltaTime        = times[s] - lastTimes[s];
        double efficiency       = deltaComparisons*runs / deltaTime;
        line += "\t" + Math.floor(efficiency* 1000.0) / 1000.0;	
        lastTimes[s] = times[s];
      }
      comparisonsNeededLastTime = comparisonsNeeded;
      System.out.println(line);
    }
  }
  public void testSelectorsAndPartitioners(ArrayList<MultiPivotSelector> selectors
    , ArrayList<MultiPivotPartitioner> partitioners, int[] valuesOfM) {
    (new PartitionerTest()).warmUpMultiPartitioners(partitioners);
    (new SelectorTest()).warmUpSelectors(selectors);
    System.out.println("Partitioning efficiency (Gbps) of Partitioner/Selector combinations");
    String header1 = "\t";
    String header2 = "m";
    for ( Integer m : valuesOfM ) {
      header1 += m;
      for (MultiPivotSelector selector: selectors) {
        header1 += "\t";
        header2 += "\t" + selector.toString();
      }
    }
    System.out.println(header1);
    System.out.println(header2);
    for (MultiPivotPartitioner party: partitioners) {
    String line = party.toString();
    for ( Integer m : valuesOfM ) {
        int cols = selectors.size();
        double times[] = new double[cols];
        double bits[]  = new double[cols];
        double mLog2m  = (Math.log(m) - 1.0) * (double)(m) / Math.log(2);
        int col;
        int runs = 5;
        if (m<1048576) runs = 100; else if (m<=1048576) runs = 25; 
        for (int r = 0; r<runs; ++r) {
          int[] input = random.randomPermutation(m);
          int[] copy  = new int[m];
          col = 0;
          for (MultiPivotSelector selector: selectors) {
            for (int i=0; i<m; ++i) {
              copy[i] = input[i];
            }
            int[] indices    = selector.selectPivotIndices(copy, 0, copy.length);
            long  startTime  = System.nanoTime();
            int[] partitions = party.multiPartitionRange(copy, 0, copy.length, indices);
            long  stopTime   = System.nanoTime();
            times[col] += (stopTime - startTime);
            bits[col]  += mLog2m    - bitsLeftInPartitions(partitions);
            ++col;
          } //selector
        } //r
        for (col=0; col<cols; ++col) {
          line += "\t" + Math.floor(bits[col] / times[col] * 1000.0)/1000.0;
        } //col
      } //m
      System.out.println(line);
    } //party
  }
  public static double bitsLeftInPartitions(int[] partitions) {
    double bits = 0;
    for (int p=0; p<partitions.length-1; p+=2) {
      int partySize = partitions[p+1] - partitions[p];
      if (1<partySize) {
        bits += (Math.log(partySize)-1.0) * (double)(partySize) / Math.log(2.0);
      }
    }
    return bits;
  }
  public void warmUpSort(RangeSorter sorter, int i) {
    warmUpSort(sorter, 1000, 12000);
  }
  public void warmUpSorts(List<RangeSorter> sorts) {
    warmUpSorts(sorts, 1000, 12000);
  }
  public <T> void dumpHeaderLine(String s, List<T> sorts) {
	for ( T i : sorts ) {
      s += "\t" + i.toString();
	}
	System.out.println(s);
  }
  public String shortName(String className) {
    className = className.replace("Partitioner", "");
    className = className.replace("Algorithm", "A");
    className = className.replace("MedianOfMedians", "MOM");
    className = className.replace("QuickSelect", "QS");
    className = className.replace("Element", "");
    className = className.replace("Selector", "");
    className = className.replace("BirdsOfAFeather", "BOF");
    className = className.replace("FloydRivest", "F/R ");
    className = className.replace("Partition", "P");
    className = className.replace("Expander", "X");
    className = className.replace("RotatingPX", "RPX");
    className = className.replace("Positional", "Pos");
    className = className.replace("EqualityLoving", "-EL");
    className = className.replace("Kangaroo", "K");
    className = className.replace("Centripetal", "C");
    className = className.replace("HolierThanThou", "HTT");
    className = className.replace("FlowerArrangement", "Flower");
    className = className.replace("Balanced", "B");
    className = className.replace("InsertionSort", "ISort");
    className = className.replace("IntroSelect", "Intro");
    className = className.replace("KthStatistic", "K");
    className = className.replace("DefaultPivotReselector", "DPR");
    className = className.replace("Default", "D");
    className = className.replace("Pivot", "Pivot");
    className = className.replace("Reselector", "R");
    return className;
  }  
}

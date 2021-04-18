package jsortie.testing.object;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import jsortie.exception.SortingFailureException;
import jsortie.object.quicksort.helper.ObjectPartitionHelper;
import jsortie.object.quicksort.partitioner.kthstatistic.KthStatisticObjectPartitionerBase;
import jsortie.object.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489ObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.algorithm489.DerivativeObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.floydrivest.FloydRivestObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.floydrivest.FridgeObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.floydrivest.QuintaryFridgeObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.floydrivest.SimplifiedFloydRivestObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.mom.MedianOfMediansObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.quickselect.QuickSelectObjectPartitioner;
import jsortie.object.quicksort.partitioner.kthstatistic.remedian.RemedianObjectPartitioner;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;
import jsortie.object.quicksort.selector.MiddleElementObjectSelector;
import jsortie.object.quicksort.selector.NoShitDirtyTukeyObjectSelector;
import jsortie.object.quicksort.selector.DirtySingletonObjectSelector;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Afterthought489SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Algorithm489SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.FairlyCompensatedSampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.FixedSizeSampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.RauhAndArceSampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.ZeroDeltaSampleSelector;

public class ObjectKthStatisticPartitionerTest
  extends ObjectSortTest{  
  @Test  
  public void testKthStatisticPartitionerAverages() {
    Algorithm489ObjectPartitioner<MyInteger> a489 
      = new Algorithm489ObjectPartitioner<MyInteger>();
    FloydRivestObjectPartitioner<MyInteger> fr 
      = new FloydRivestObjectPartitioner<MyInteger>(5.0);
    FridgeObjectPartitioner<MyInteger> fridge
      = new FridgeObjectPartitioner<MyInteger> (true);
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(a489);
    a.add(fr);
    a.add(fridge);
    runCount = 100;
    testAveragesForKthStatisticPartitionersVaryingN(a, 2000000);
  }
  @Test
  public void testKthStatisticPartitionerAverages2() {
    QuickSelectObjectPartitioner<MyInteger> qs
      = new QuickSelectObjectPartitioner<MyInteger>(
          new MiddleElementObjectSelector<MyInteger>());
    QuickSelectObjectPartitioner<MyInteger> qs3
      = new QuickSelectObjectPartitioner<MyInteger>(
          new DirtySingletonObjectSelector<MyInteger>());
/*    QuickSelectObjectPartitioner<MyInteger> qsTukey
      = new QuickSelectObjectPartitioner<MyInteger>(
          new TukeyObjectSelector<MyInteger>());
          */
    QuickSelectObjectPartitioner<MyInteger> qsTukeyClean
      = new QuickSelectObjectPartitioner<MyInteger>(
          new NoShitDirtyTukeyObjectSelector<MyInteger>());
    RemedianObjectPartitioner<MyInteger> rp
      = new RemedianObjectPartitioner<MyInteger>();
    MedianOfMediansObjectPartitioner<MyInteger> mom
      = new MedianOfMediansObjectPartitioner<MyInteger>();
    Algorithm489ObjectPartitioner<MyInteger> a489Fair
      = new Algorithm489ObjectPartitioner<MyInteger>(
          new FairlyCompensatedSampleSelector());
    FloydRivestObjectPartitioner<MyInteger> fr
      = new FloydRivestObjectPartitioner<MyInteger>(5); 
    FridgeObjectPartitioner<MyInteger> fridge
      = new FridgeObjectPartitioner<MyInteger>(true); 
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(qs);
    a.add(qs3);
    a.add(qsTukeyClean);
    a.add(rp);
    a.add(mom);
    a.add(a489Fair);
    a.add(fr);
    a.add(fridge);
    runCount = 1000;
    testAveragesForKthStatisticPartitionersVaryingN(a, 100000);
  }
  @Test
  public void testKthStatisticPartitionerAverages3() {
    DerivativeObjectPartitioner<MyInteger> dp
      = new DerivativeObjectPartitioner<MyInteger>();
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(dp);
    runCount = 100;
    testAveragesForKthStatisticPartitionersVaryingN(a, 100000);
  }
  @Test  
  public void testKthStatisticPartitionerAverages4() {
    Algorithm489ObjectPartitioner<MyInteger> a489 
      = new Algorithm489ObjectPartitioner<MyInteger>();
    Algorithm489ObjectPartitioner<MyInteger> fc 
      = new Algorithm489ObjectPartitioner<MyInteger>
            ( new FairlyCompensatedSampleSelector() );
    Algorithm489ObjectPartitioner<MyInteger> after 
      = new Algorithm489ObjectPartitioner<MyInteger>
            ( new Afterthought489SampleSelector() );
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(a489);
    a.add(fc);
    a.add(after);
    runCount = 100;
    System.out.println("k just off Centre");
    testAveragesForKthStatisticPartitionersVaryingN(a, 0.5, -3, 100000);
    System.out.println("\nkHat=0.25");
    testAveragesForKthStatisticPartitionersVaryingN(a, 0.25, 0, 100000);
  }
  @Test  
  public void testDerivativePartitionerAverages() {
    Algorithm489ObjectPartitioner<MyInteger> a489Afterthought 
      = new Algorithm489ObjectPartitioner<MyInteger>
            ( new Afterthought489SampleSelector() );
    FridgeObjectPartitioner<MyInteger> fridge
      = new FridgeObjectPartitioner<MyInteger>(true); 
    DerivativeObjectPartitioner<MyInteger> dp
      = new DerivativeObjectPartitioner<MyInteger>();
    Algorithm489ObjectPartitioner<MyInteger> z
      = new Algorithm489ObjectPartitioner<MyInteger>
            (new ZeroDeltaSampleSelector());
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(a489Afterthought);
    a.add(fridge);
    a.add(dp);
    a.add(z);
    runCount = 1000;
    System.out.println("Excess comparison counts: start " + (new Date()).toString());
    String[] aNames = new String[]
        { "A489-Afterthought", "Optimistic Fridge Partitioner"
        , "Derivative Partitioner", "Zero Delta Partitioner" };
    double[] kHatValues = { 0.5, 0.75, 0.95 };
    testAveragesForKthStatisticPartitionersVaryingNAndK(a, aNames, 3, 100000, kHatValues, 0, true);
    runCount = 100;
    testAveragesForKthStatisticPartitionersVaryingNAndK(a, null, 103258, 10000000, kHatValues, 0, true);
    testAveragesForKthStatisticPartitionersVaryingNAndK(a, aNames, 4673158, 10000000, kHatValues, 0, true);
    System.out.println("\nExcess comparison counts: stop " + (new Date()).toString() + "\n");
  }
  @Test
  public void testRauhAndArceSampler() {
    RauhAndArceSampleSelector r
      = new RauhAndArceSampleSelector(true);
    KthStatisticSubproblem p 
      = new KthStatisticSubproblem();
    p.start = 0;
    p.stop  = 1001;
    for (p.pivotIndex = 100; p.pivotIndex<p.stop; p.pivotIndex+=100) {
      r.chooseSample(p);
      System.out.println(p.toString());
    }
  }
  @Test
  public void testRauhAndArceAverages() {
    Algorithm489ObjectPartitioner<MyInteger> a489
      = new Algorithm489ObjectPartitioner<MyInteger>
            ( new FairlyCompensatedSampleSelector() );
    KthStatisticObjectPartitioner<MyInteger> rauhAndArce
      = new Algorithm489ObjectPartitioner<MyInteger>
            ( new RauhAndArceSampleSelector(true) );
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(a489);
    a.add(rauhAndArce);
    runCount = 1000;
    double[] kHatValues = new double[] { 0.5, 0.25, 0.05 };
    String[] aNames  = new String[] { "A489", "R&A" }; 
    testAveragesForKthStatisticPartitionersVaryingNAndK
      (a, aNames, 3, 100000, kHatValues, 0, true);
  }
  public void testAveragesForKthStatisticPartitionersVaryingN
    ( ArrayList<KthStatisticObjectPartitioner<MyInteger>> a
    , int maxN) {
    testAveragesForKthStatisticPartitionersVaryingN
      (a, 0.5, 0.0, maxN);
  }
  public void testAveragesForKthStatisticPartitionersVaryingN
    ( ArrayList<KthStatisticObjectPartitioner<MyInteger>> a
    , double kHat, double offset, int maxN) {
    double[] kHatValues = new double[] { kHat };
    String[] aNames = new String[a.size()];
    for (int i=0; i<a.size(); ++i) {
      aNames[i] = a.get(i).toString();
    }
    testAveragesForKthStatisticPartitionersVaryingNAndK
      ( a, aNames, 3, maxN, kHatValues, offset, true );
  }
  public void testAveragesForKthStatisticPartitionersVaryingNAndK
    ( ArrayList<KthStatisticObjectPartitioner<MyInteger>> a
    , String[] aNames, int minN, int maxN
    , double[] kHatValues, double offset
    , boolean  bShowExcess) {
    if ( aNames != null) {
      String header = "n";    
      for (int z=0; z<kHatValues.length; ++z) {
        for (int i=0; i<a.size(); ++i) {
          header += "\t" + aNames[i] + "(kHat=" + kHatValues[z] + ")";
        }
      }
      System.out.println(header);
    }
    int colCount = a.size() * kHatValues.length;
    for (int n=minN; n<maxN; n += ((n<10) ? 1 : (n/10))) {
      double[] counts = new double[colCount];
      for (int z=0; z<kHatValues.length; ++z) {
        int k = (int)Math.floor( (n+1)*kHatValues[z] + offset);
        if (k<1) k=1;
        if (n<k) k=n;
        for (int r=0; r<runCount; ++r) {
          int[] input = random.randomPermutation(n);
          for (int i=0; i<a.size(); ++i) {
            KthStatisticObjectPartitioner<MyInteger> ksop = a.get(i);
            MyInteger[] copy = new MyInteger[n];
            for (int j=0; j<n; ++j) {
              copy[j] = new MyInteger(input[j]);
            }
            MyInteger.setComparisonCount(0);
            ksop.partitionRangeExactly
            ( MyInteger.countingComparator , copy, 0, n, k-1);
            counts[z*a.size() + i] += MyInteger.getComparisonCount();
          }
        }
      }
      String line = "" + n;
      for (int z=0; z<kHatValues.length; ++z) {
        int k = (int)Math.floor( (n+1)*kHatValues[z] + offset);
        if (k<1) k=1;
        if (n<k) k=n;
        for (int i=0; i<a.size(); ++i) {
          double x;
          int kLow = (k+k < n + 1) ? k : (n+1-k);
          if (bShowExcess) {
            x = Math.floor ( counts[i+z*a.size()] 
                             / (double)runCount * 100.0 
                             - (n + kLow)*100.0 +  .5) / 100.0;
          } else {
            x = Math.floor ( counts[i+z*a.size()] 
                             / (double)runCount * 100.0 + .5)
                / 100.0;
          }
          line += "\t" + x;
        } //i
      } //z
      System.out.println(line);
    } //n
  }
  @Test
  public void testVarianceForCMoreThanHalfM() {
    runCount = 1000000;
    int m=7;
    for (int c=4;c<m;++c) {
      double[] sumX = new double[(int)c+1];
      double[] sumOfSquaresOfX = new double[(int)c+1];
      for (int r=0; r<runCount; ++r) {
        int[] sample = random.randomOrderedCombination((int)m, (int)c);
        for (int a=1; a<=c; ++a) {
          int x = sample[a-1] + 1;
          sumX[a] += x;
          sumOfSquaresOfX[a] += x*x;
        }
      }
      for (int a=1; a<=c; ++a) {
        double book = (double)a*(c+1-a)*(double)(m+1)*(m-c)/(double)(c+1)/(double)(c+1)/(double)(c+2);
        double meanX = sumX[a] / runCount;
        double variance = sumOfSquaresOfX[a] / (double) runCount - meanX * meanX;
        System.out.println
          ( "" + a + "\t" + c
          + "\t" + Math.floor(book*1000.0+.5)/1000.0 
          + "\t" + Math.floor(variance*1000.0+.5)/1000.0 );
      }
    }
  }
  @Test
  public void testMickey() {
    int n = 21;
    KthStatisticObjectPartitioner<MyInteger> f
       = new FridgeObjectPartitioner<MyInteger>(1.5);
    int[] input = random.randomPermutation(n);
    for (int k=n/10;k<n;k+=n/5) {
      MyInteger.setComparisonCount(0);
      MyInteger[] copy = new MyInteger[n];
      for (int j=0; j<n; ++j) {
        copy[j] = new MyInteger(input[j]);
      }
      f.partitionRangeExactly(MyInteger.countingComparator , copy, 0, n, k);
      System.out.println("Check: copy[" + (k) + "]==" + copy[k].toString());
      System.out.println(MyInteger.getComparisonCount());
      System.out.println("");
    }
  }
  @Test 
  public void testVaryingAlpha21() {
    int n=21;
    runCount = 5;
    int[] kValues = new int[] { 1, 11, 16, 21 };
    testVaryingAlpha(n, kValues, 0.6, 7, true);
  }
  @Test 
  public void testVaryingAlpha101() {
    int n=101;
    runCount = 10000;
    int[] kValues = new int[] { 1, 11, 26, 51, 76, 91, 101 };
    testVaryingAlpha(n, kValues, 0.6, 7, true);
  }
  @Test 
  public void testVaryingAlpha10001() {
    int n=10001;
    runCount = 100;
    int[] kValues = new int[] { 5001, 7501, 9501};
    testVaryingAlpha(n, kValues, 0.6, 7, true);
  }
  @Test 
  public void testOptimisticFridgePartitioner() {
    int n=1000001;
    runCount = 100;
    int[] kValues = new int[] { 500001 , 750001, 950001, 999001 };
    testVaryingAlpha(n, kValues, 0.6, 7, false);
  }
  @Test 
  public void testVaryingAlpha1000001() {
    int n=1000001;
    runCount = 25;
    int[] kValues = new int[] { 500001 , 750001, 950001, 999001 };
    testVaryingAlpha(n, kValues, 0.6, 7, false);
  }
  private void testVaryingAlpha
    ( int n, int[] kValues
    , double powerStart, double powerStop
    , boolean simplified) {
    String[] sValues;
    if (simplified) {
      sValues = new String[] 
        { "Simplified", "Classic", "Fridge", "Fridge(O)", "Quintary" } ;
    } else {
      sValues = new String[] 
        { "Classic", "Fridge", "Fridge(O)", "Quintary" } ;
    }
    System.out.println
        ( "For n=" + n 
        + " , runCount=" + runCount);
    String line = "Alpha";
    for (String s : sValues ) {
      for (int k : kValues) {
        line += "\t" + s + k;
      }
    }
    ObjectPartitionHelper<MyInteger> checker 
      = new ObjectPartitionHelper<MyInteger>();
    System.out.println(line); //start 0.6
    for ( double a=powerStart; a<=powerStop; a+=0.1) {
      double alpha = Math.floor(Math.pow(2.0, a)*100.0+.5)/100.0;
      FloydRivestObjectPartitioner<MyInteger> fr 
        = new FloydRivestObjectPartitioner<MyInteger>(alpha);
      FridgeObjectPartitioner<MyInteger> fridge
        = new FridgeObjectPartitioner<MyInteger>(alpha, false);
      FridgeObjectPartitioner<MyInteger> fridgeO
        = new FridgeObjectPartitioner<MyInteger>(alpha, true);
      QuintaryFridgeObjectPartitioner<MyInteger> quintary
        = new QuintaryFridgeObjectPartitioner<MyInteger>(alpha);
      
      ArrayList<KthStatisticObjectPartitioner<MyInteger>> parties
        = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
      if (simplified) {
        SimplifiedFloydRivestObjectPartitioner<MyInteger> simple
          = new SimplifiedFloydRivestObjectPartitioner<MyInteger>(alpha);
        parties.add(simple);
      }
      else {
        fr.setUseSafetyNet(true);        
        fridge.setUseSafetyNet(true);
        fridgeO.setUseSafetyNet(true);
        quintary.setUseSafetyNet(true);
      }
      parties.add(fr);
      parties.add(fridge);
      parties.add(fridgeO);
      parties.add(quintary);
      System.out.print(alpha);
      for ( KthStatisticObjectPartitioner<MyInteger> f : parties) {
        for (int k : kValues) {
          MyInteger.setComparisonCount(0);
          for (int r=0; r<runCount; ++r) {
            int[] input = random.randomPermutation(n);
            MyInteger[] copy = new MyInteger[n];
            for (int j=0; j<n; ++j) {
              copy[j] = new MyInteger(input[j]);
            }
            f.partitionRangeExactly
              ( MyInteger.countingComparator , copy, 0, n, k-1);
            if ( copy[k-1].intValue()!=k-1 ) {
              throw new 
                SortingFailureException
                ("\nRun " + r + " with k=" + k 
                + " of " + f.toString() 
                + " failed:\n copy[" + (k-1) + "]"
                + " was " + copy[k-1].toString()); 
            }
            checker.checkPartition
              ( "\n" + f.toString() + "(run " + r + " with k=" + k
              , MyInteger.integerComparator
              , copy, 0, k-1, n);
          }
          double kMin = (k<(n+1)/2) ? k : (n+1-k);
          double x  
            = Math.floor 
              ( MyInteger.getComparisonCount() 
                / (double)runCount * 100.0 
                - (n+kMin)*100.0 + .5
              ) / 100.0;
          System.out.print("\t" + x);
        }
      }
      System.out.println("");
    }
    System.out.print("");
  }
  @Test
  public void testA489SampleSelector() {
    Algorithm489SampleSelector ss
      = new Algorithm489SampleSelector();
    KthStatisticSubproblem prob 
      = new KthStatisticSubproblem();
    prob.start = 0;
    prob.stop  = 1001;
    for (Integer k : new int[] { 1, 11, 500, 501, 502, 991, 1001 }) {
      prob.pivotIndex = k-1;
      ss.chooseSample(prob);
      int c = prob.innerStop - prob.innerStart;
      int a = prob.pivotIndex - prob.innerStart + 1;
      System.out.println(prob.innerStart + ".." + (prob.innerStop-1)
        + " pivot=" + prob.pivotIndex + " a=" + a + " c=" + c);
    }
  }  
  @Test  
  public void testKthStatisticPartitionerResponse() {
    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a
      = getExampleKthStatisticPartitioners();
    runCount = 100000;
    for (int n: new int[] { 101, 1001, 10001 }) {
      System.out.println("Average counts, for " + n + "-item inputs (runCount=" + runCount + ")");;
      System.out.println("k\tQ\tQ3\tQTukey\tQ9\tA489\tArce\tRemedian\tZero\tDerivative\tFridge");
      //Problem: this header needs to match 
      //getExampleKthStatisticPartitioners...
      int step   = n/100;
      int middle = (n+1)/2;
      int kStart = (step==1) ? 1 : (((middle-1) % step) + 1);
      for ( int k = kStart; k<=n; k += step) {
        double[] counts = new double[a.size()];
        for (int r=0; r<runCount; ++r) {
          int[] input = random.randomPermutation(n);
          for (int i=0; i<a.size(); ++i) {
            KthStatisticObjectPartitioner<MyInteger> ksop = a.get(i);
            MyInteger[] copy = new MyInteger[n];
            for (int j=0; j<n; ++j) {
              copy[j] = new MyInteger(input[j]);
            }
            MyInteger.setComparisonCount(0);
            ksop.partitionRangeExactly
            ( MyInteger.countingComparator , copy, 0, n, k-1);
            
            counts[i] += MyInteger.getComparisonCount();
          }
        }
        String line = "" + k;
        for (int i=0; i<a.size(); ++i) {
          double x = Math.floor( counts[i] / (double)runCount * 100.0 + .5) / 100.0;
          line += "\t" + x;
        }
        System.out.println(line);
      }
      runCount/=10;
      System.out.println("");
    }
  }  
  private ArrayList<KthStatisticObjectPartitioner<MyInteger>> 
    getExampleKthStatisticPartitioners() {
    QuickSelectObjectPartitioner<MyInteger> qs
      = new QuickSelectObjectPartitioner<MyInteger>();
    QuickSelectObjectPartitioner<MyInteger> qsMedianOf3
      = new QuickSelectObjectPartitioner<MyInteger>
            (new DirtySingletonObjectSelector<MyInteger>()); 
    QuickSelectObjectPartitioner<MyInteger> qsTukey
      = new QuickSelectObjectPartitioner<MyInteger>
            (new NoShitDirtyTukeyObjectSelector<MyInteger>()); 
    Algorithm489ObjectPartitioner<MyInteger> qsTeethOf9
      = new Algorithm489ObjectPartitioner<MyInteger>
            (new FixedSizeSampleSelector(5,9)); 
    Algorithm489ObjectPartitioner<MyInteger> a489 
      = new Algorithm489ObjectPartitioner<MyInteger>
            (new FairlyCompensatedSampleSelector());
    Algorithm489ObjectPartitioner<MyInteger> arce
      = new Algorithm489ObjectPartitioner<MyInteger>
            (new RauhAndArceSampleSelector(true)); 
    FridgeObjectPartitioner<MyInteger> fr 
      = new FridgeObjectPartitioner<MyInteger>(true);
    RemedianObjectPartitioner<MyInteger> rp
      = new RemedianObjectPartitioner<MyInteger>();
    Algorithm489ObjectPartitioner<MyInteger> zp
      = new Algorithm489ObjectPartitioner<MyInteger>
            (new ZeroDeltaSampleSelector());
    DerivativeObjectPartitioner<MyInteger> dp
      = new DerivativeObjectPartitioner<MyInteger>();

    ArrayList<KthStatisticObjectPartitioner<MyInteger>> a 
      = new ArrayList<KthStatisticObjectPartitioner<MyInteger>>();
    a.add(qs);
    a.add(qsMedianOf3);
    a.add(qsTukey);
    a.add(qsTeethOf9);
    a.add(a489);
    a.add(arce);
    a.add(rp);
    a.add(zp);
    a.add(dp);
    a.add(fr);
    return a;
  }
  @Test
  public void testFloydRivestHorror() {
    runCount = 1000;
    double[] alphas = new double[] 
      { -6.0, 2.0, 4.32, 8.0, 18.6624 };
    System.out.print("n\t3n/2");
    for (double alpha : alphas) {
      if (0<alpha) {
        System.out.print("\tA" + alpha + "\tP" + alpha);
      } else {
        System.out.print("\tFridge" + (-alpha));
      }
    }
    System.out.println("");
    for (int n=10; n<50000; n=n*11/10) {
      System.out.print("" + n + "\t" + 3*n/2 );
      double[] counts = new double[alphas.length];
      for (int r=0; r<runCount; ++r) {
        int[] input = random.randomPermutation(n);
        for ( int i=0; i<alphas.length; ++i) {
          KthStatisticObjectPartitionerBase<MyInteger> fr
            = (alphas[i]<0)
            ? new FridgeObjectPartitioner<MyInteger>
                  (-alphas[i], true)
            : new FloydRivestObjectPartitioner<MyInteger>
                  (alphas[i]);
          MyInteger.setComparisonCount(0);
          MyInteger[] copy = new MyInteger[n];
          for (int j=0; j<n; ++j) {
            copy[j] = new MyInteger(input[j]);
          }
          fr.partitionRangeExactly
          ( MyInteger.countingComparator , copy, 0, n, (n-1)/2);
          counts[i] += MyInteger.getComparisonCount();
        }
      }      
      for (int i=0; i<alphas.length; ++i) {
        double avg = counts[i] / (double)runCount;
        double prediction = predictFR( n, (n+1)/2, alphas[i]);
        System.out.print( t2(avg) );
        if (0<alphas[i]) {
          System.out.print( t2(prediction) );
        }
      }
      System.out.println("");
    }
  }
  public String t2(double x) {
    return "\t" + Math.floor(x*100.0 + .5) / 100.0;
  }
  public double predictFR(double m, double k, double alpha) {
    if (m<2.0)  return 0;
    if (m<6.0)  return m+k; //ish.
    if (m+1<k+k) {
      k = m+ 1 - k;
    }
    double c = Math.floor((m+1) / alpha) - 1;
    if (c<2.0) c=2.0;
    double delta = Math.sqrt(Math.log(m)*m*(m-c)/(c+1))*(c+1)/(m+1);
    double t  = k*(c+1)/(m+1);
    double a1 = t - delta;
    double a2 = t + delta;
    if (a1<1) a1=1;
    if (c<a2) a2=c;
    double x = (alpha-1)/alpha*(m+k); 
    double r = (a2-a1)*(m+1)/(c+1); //remainder
    return predictFR(c, (int)Math.floor(t+.5), alpha ) 
      + x + predictFR(r, r/2, alpha); 
  }
}

package jsortie.testing;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Test;

import jsortie.helper.DumpRangeHelper;
import jsortie.janitors.CoprimeGapSequenceHelper;
import jsortie.object.bucket.DynamicArrayBucket;

public class CombSortTest extends SortTest {
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }
  @Test
  public void testBubblyCombsort() {
    int runCount = 100;
    double[] ratios = new double[] 
        { 1.35, 1.34, 1.33, 1.32, 1.31
        , 1.30, 1.29, 1.28, 1.27, 1.26 };
    String header = "n";
    for (int x = 0; x < ratios.length; ++x) {
      header += "\t" + ratios[x];
    }
    System.out.println(header);
    for ( int count = 100; count < 100000000
        ; count = count * 21 / 20) {
      if (count>10000000) {
        runCount = 1;
      }
      String line = "" + count;
      for (int x = 0; x < ratios.length; ++x) {
        double g = ratios[x];
        int[] gaps = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
        int lastGapIndex = 0;
        while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] * 2 < count) {
          ++lastGapIndex;
        }
        double wpe = 0; // sum of per-run worst-case +ve positional errors
        //double tape = 0; // total absolute positional error
        for (int r = 0; r < runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          int gapIndex = lastGapIndex;
          for (; 0 <= gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            int i = 0;
            int j = gap;
            for (; j < count; ++i, ++j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
          }
          int wpeHere = 0;
          for (int i = 0; i < count; ++i) {
            int d = i - vArray[i];
            //tape += (d < 0) ? -d : d;
            if (wpeHere < d) {
              wpeHere = d;
            }
          }
          wpe += wpeHere;
        }
        line += f5(wpe / (double) runCount); 
      }
      System.out.println(line);
    }
  }
  @Test
  public void testVanillaCombsort() {
    int runCount = 25;
    double[] ratios = new double[] { /* 2.0, 1.8, 1.66, 1.5, */ 1.4, 1.39, 1.38, 1.37, 1.36, 1.35,
        1.34 /* , 1.33, 1.32, 1.31, 1.3, 1.26, 1.21 */ };
    String header = "n";
    for (int x = 0; x < ratios.length; ++x) {
      header += "\t" + ratios[x];
    }
    System.out.println(header);
    for ( int count = 100; count < 100000000
        ; count = count * 21 / 20) {
      runCount = 100;
      if (10000 < count)
        runCount = 5;
      if (1000000 < count)
        runCount = 1;
      String line = "" + count;
      for (int x = 0; x < ratios.length; ++x) {
        double g = ratios[x];
        int[] gaps = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
        int lastGapIndex = 0;
        while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] * 2 < count) {
          ++lastGapIndex;
        }
        //double wpe = 0; // sum of per-run worst-case +ve positional errors
        double tape = 0; // total absolute positional error
        for (int r = 0; r < runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          int gapIndex = lastGapIndex;
          for (; 0 <= gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            int i = 0;
            int j = gap;
            for (; j < count; ++i, ++j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
          }
          int wpeHere = 0;
          for (int i = 0; i < count; ++i) {
            int d = i - vArray[i];
            tape += (d < 0) ? -d : d;
            if (wpeHere < d) {
              wpeHere = d;
            }
          }
          //wpe += wpeHere;
        }
        line += f5(tape / (double) runCount / (double) count); 
      }
      System.out.println(line);
    }
  }
  @Test
  public void testAlternatingCombsort() {
    int runCount = 25;
    double[] ratios = new double[] { /* 2.0, 1.8, 1.66, 1.5, */ 1.5, 1.49, 1.48, 1.47, 1.46, 1.45, 1.44, 1.43, 1.42,
        1.41, 1.4, 1.39, 1.38, 1.37, 1.36, 1.35, 1.34,
        1.33, /* 1.32, 1.31, 1.30, 1.26, 1.21 */ };
    String header = "n";
    for (int x = 0; x < ratios.length; ++x) {
      header += "\t" + ratios[x];
    }
    System.out.println(header);
    for (int count = 100; count < 100000000; count = count * 21 / 20) {
      runCount = 25;
      if (10000 < count)
        runCount = 5;
      if (1000000 < count)
        runCount = 1;
      String line = "" + count;
      for (int x = 0; x < ratios.length; ++x) {
        double g = ratios[x];
        int[] gaps = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
        int lastGapIndex = 0;
        while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] * 2 < count) {
          ++lastGapIndex;
        }
        //double wpe = 0; // sum of per-run worst-case +ve positional errors
        double tape = 0; // total absolute positional error
        for (int r = 0; r < runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          int gapIndex = lastGapIndex;
          for (; 0 <= gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            int i = 0;
            int j = gap;
            for (; j < count; ++i, ++j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
            --gapIndex;
            if (gapIndex < 0)
              continue;
            gap = gaps[gapIndex];
            j = count - 1;
            i = j - gap;
            for (; 0 <= i; --i, --j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
          }
          int wpeHere = 0;
          for (int i = 0; i < count; ++i) {
            int d = i - vArray[i];
            tape += (d < 0) ? -d : d;
            if (wpeHere < d) {
              wpeHere = d;
            }
          }
          //wpe += wpeHere;
        }
        line += f5(tape / (double) runCount / (double) count);
        // + f5(wpe / (double)runCount );
      }
      System.out.println(line);
    }
  }
  @Test
  public void testStapledAlternatingCombsort() {
    int runCount = 25;
    double[] ratios = new double[] { /* 2.0, 1.8, 1.66, 1.5, */ 1.5, 1.49, 1.48, 1.47, 1.46, 1.45, 1.44, 1.43, 1.42,
        1.41, 1.4, 1.39, 1.38, 1.37, 1.36, 1.35, 1.34,
        1.33, /* 1.32, 1.31, 1.30, 1.26, 1.21 */ };
    String header = "n";
    for (int x = 0; x < ratios.length; ++x) {
      header += "\t" + ratios[x];
    }
    System.out.println(header);
    for (int count = 100; count < 100000000; count = count * 21 / 20) {
      runCount = 25;
      if (10000 < count)
        runCount = 5;
      if (1000000 < count)
        runCount = 1;
      String line = "" + count;
      for (int x = 0; x < ratios.length; ++x) {
        double g = ratios[x];
        int[] gaps = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
        int lastGapIndex = 0;
        while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] * 2 < count) {
          ++lastGapIndex;
        }
        double stapling = 0; // overhead cost of the two "stapling" passes
        //double wpe = 0; // sum of per-run worst-case +ve positional errors
        double tape = 0; // total absolute positional error
        for (int r = 0; r < runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          int gapIndex = lastGapIndex;
          int stapleIndex = lastGapIndex / 3;
          for (; 0 <= gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            if (gapIndex == stapleIndex || gapIndex == stapleIndex + 1) {
              stapling += sortSlicesOfRange(vArray, 0, vArray.length, gap) - vArray.length + gap;
              if (gapIndex == 0)
                break;
              gap = gaps[--gapIndex];
            }
            int i = 0;
            int j = gap;
            for (; j < count; ++i, ++j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
            --gapIndex;
            if (gapIndex < 0)
              break;
            gap = gaps[gapIndex];
            if (gapIndex == stapleIndex || gapIndex == stapleIndex + 1) {
              stapling += sortSlicesOfRange(vArray, 0, vArray.length, gap) - vArray.length + gap;
              if (gapIndex == 0)
                break;
              gap = gaps[--gapIndex];
            }
            j = count - 1;
            i = j - gap;
            for (; 0 <= i; --i, --j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
          }
          int wpeHere = 0;
          for (int i = 0; i < count; ++i) {
            int d = i - vArray[i];
            tape += (d < 0) ? -d : d;
            if (wpeHere < d) {
              wpeHere = d;
            }
          }
          //wpe += wpeHere;
        }
        line += f5((stapling + tape) / (double) runCount / (double) count);
      }
      System.out.println(line);
    }
  }
  public double sortSlicesOfRange(int[] vArray, int start, int stop, int gap) {
    int i = start + gap;
    double comparisons = 0;
    for (; i < stop; ++i) {
      int v = vArray[i];
      int h = i - gap;
      for (; start <= h; h -= gap) {
        ++comparisons;
        if (vArray[h] <= v) {
          break;
        }
        vArray[h + gap] = vArray[h];
      }
      vArray[h + gap] = v;
    }
    return comparisons;
  }

  @Test
  public void testShakerCombsort() {
    double[] ratios = new double[] { 2.1, 2.05, 2.02, 2.01, 1.998, 1.93, 1.92 };
    String header = "n";
    for (int x = 0; x < ratios.length; ++x) {
      header += "\t" + ratios[x];
    }
    System.out.println(header);
    for (int count = 100; count < 100000000; count = count * 21 / 20) {
      int runCount = 25;
      if (10000 < count)
        runCount = 1;
      if (1000000 < count)
        runCount = 1;
      String line = "" + count;
      for (int x = 0; x < ratios.length; ++x) {
        double g = ratios[x];
        int[] gaps = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
        int lastGapIndex = 0;
        while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] * 2 < count) {
          ++lastGapIndex;
        }
        //double wpe = 0; // sum of per-run worst-case +ve positional errors
        double tape = 0; // total absolute positional error
        for (int r = 0; r < runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          int gapIndex = lastGapIndex;
          for (; 0 <= gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            int i = 0;
            int j = gap;
            for (; j < count; ++i, ++j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
            j = count - 1;
            i = j - gap;
            for (; 0 <= i; --i, --j) {
              if (vArray[j] < vArray[i]) {
                int vTemp = vArray[i];
                vArray[i] = vArray[j];
                vArray[j] = vTemp;
              }
            }
          }
          int wpeHere = 0;
          for (int i = 0; i < count; ++i) {
            int d = i - vArray[i];
            tape += (d < 0) ? -d : d;
            if (wpeHere < d) {
              wpeHere = d;
            }
          }
          //wpe += wpeHere;
        }
        line += f5(tape / (double) runCount / (double) count);
        // + f5(wpe / (double)runCount );
      }
      System.out.println(line);
    }
  }
  @Test
  public void testHalfBrickCombsort() {
    int runCount = 25;
    double[] ratios = new double[] { 1.14, 1.13, 1.12, 1.11, 1.1, 1.09, 1.08 };
    String header = "n";
    for (int x = 0; x < ratios.length; ++x) {
      header += "\t" + ratios[x];
    }
    System.out.println(header);
    for (int count = 100; count < 100000000; count = count * 21 / 20) {
      runCount = 25;
      if (10000 < count)
        runCount = 5;
      if (1000000 < count)
        runCount = 1;
      String line = "" + count;
      for (int x = 0; x < ratios.length; ++x) {
        double g = ratios[x];
        int[] gaps = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
        int lastGapIndex = 0;
        while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] < count) {
          ++lastGapIndex;
        }
        //double wpe = 0; // sum of per-run worst-case +ve positional errors
        double tape = 0; // total absolute positional error
        for (int r = 0; r < runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          int gapIndex = lastGapIndex;
          for (; 0 <= gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            int i = 0;
            int j = gap;
            for (; j < count; i += gap, j += gap) {
              int j2 = j + gap;
              if (count < j2)
                j2 = count;
              for (; j < j2; ++i, ++j) {
                if (vArray[j] < vArray[i]) {
                  int vTemp = vArray[i];
                  vArray[i] = vArray[j];
                  vArray[j] = vTemp;
                }
              }
            }
            --gapIndex;
            if (gapIndex < 0)
              continue;
            gap = gaps[gapIndex];
            j = count - 1;
            i = j - gap;
            for (; 0 <= i; i -= gap, j -= gap) {
              int i2 = i - gap;
              if (i2 < 0)
                i2 = -1;
              for (; i2 < i; i--, j--) {
                if (vArray[j] < vArray[i]) {
                  int vTemp = vArray[i];
                  vArray[i] = vArray[j];
                  vArray[j] = vTemp;
                }
              }
            }
          }
          int wpeHere = 0;
          for (int i = 0; i < count; ++i) {
            int d = i - vArray[i];
            tape += (d < 0) ? -d : d;
            if (wpeHere < d) {
              wpeHere = d;
            }
          }
          //wpe += wpeHere;
        }
        line += f5(tape / (double) runCount / (double) count);
        // + f5(wpe / (double)runCount );
      }
      System.out.println(line);
    }
  }
  @Test
  public void testVanillaAPEPerPass() {
    //APE = Average Positional Error
    //WPE = Worst Positional Error
    
    //1.33 .... 0.33
    int    count = 50000000;
    double g     = 1.38;
    int[]  gaps  = CoprimeGapSequenceHelper
                   .coPrimeGapSequence(g);
    int lastGapIndex = 0;
    while ( lastGapIndex + 1 < gaps.length 
            && gaps[lastGapIndex + 1] * 2 < count) {
      ++lastGapIndex;
    }
    DumpRangeHelper.dumpRange("gaps", gaps, 0, lastGapIndex+1);
    for (int r=0; r<100; ++r) {
    int[] vArray = random.randomPermutation(count);
    int gapIndex = lastGapIndex;
    int p = 1;
    for (; 0 < gapIndex; --gapIndex) {
      int gap = gaps[gapIndex];
      int i = 0;
      int j = gap;
      for (; j < count; ++i, ++j) {
        if (vArray[j] < vArray[i]) {
          int vTemp = vArray[i];
          vArray[i] = vArray[j];
          vArray[j] = vTemp;
        }
      }
      int wpe = 0;
      //int wpeAt = 0;
      double tape = 0;
      for (i = 0; i < count; ++i) {
        int d = i - vArray[i];
        tape += (d < 0) ? -d : d;
        if (wpe < d) {
          wpe = d;
          //wpeAt = i;
        }
      }
      if (r==0 || gap == 1)
      {
      System.out.println(p + "\t" + gap + 
          f5(tape / (double) count) + 
          f5(tape / count / gap) + 
          f5(wpe) /*+ "\t" + wpeAt + f5((double)wpe/count)*/);
      }      
      ++p;
    }
    }
  }
  @Test
  public void testAlternatingAPEPerPass() {
    //APE = Average Positional Error
    //WPE = Worst Positional Error
    
    //1.33 .... 0.33
    int    count = 1000000;
    int    runCount = 100;
    for (double g     = 1.45; g<=1.6; g+=0.01) {
    int[]  gaps  = CoprimeGapSequenceHelper
                   .coPrimeGapSequence(g);
    int lastGapIndex = 0;
    while ( lastGapIndex + 1 < gaps.length 
            && gaps[lastGapIndex + 1] * 2 < count) {
      ++lastGapIndex;
    }
    System.out.println("g=" + g);
    DumpRangeHelper.dumpRange("gaps", gaps, 0, lastGapIndex+1);
    double[] tape = new double[lastGapIndex+1];
    for (int r=0; r<runCount; ++r) {
      int[] vArray = random.randomPermutation(count);
      int gapIndex = lastGapIndex;
      for (; 0 < gapIndex; --gapIndex) {
        int gap = gaps[gapIndex];
        if ((gapIndex & 1) == 1) {
          int i = 0;
          int j = gap;
          for (; j < count; ++i, ++j) {
            if (vArray[j] < vArray[i]) {
              int vTemp = vArray[i];
              vArray[i] = vArray[j];
              vArray[j] = vTemp;
            }
          }
        } else {
          int j = count - 1;
          int i = j - gap;
          for (; 0 <= i; --i, --j) {
            if (vArray[j] < vArray[i]) {
              int vTemp = vArray[i];
              vArray[i] = vArray[j];
              vArray[j] = vTemp;
            }
          }
        }
        int wpe = 0;
        //int wpeAt = 0;
        for (int i = 0; i < count; ++i) {
          int d = i - vArray[i];
          tape[gapIndex] += (d < 0) ? -d : d;
          if (wpe < d) {
            wpe = d;
            //wpeAt = i;
          }
        }
      }
    }
    for (int gapIndex=lastGapIndex; 0<gapIndex; --gapIndex) {
      int gap = gaps[gapIndex];
      double ape = tape[gapIndex] / (double)count / (double)runCount;
      System.out.println( "" + gap + "\t" + ape / (double)gap);
    }
    System.out.println("");
    }
  }
  @Test
  public void testGaps() {
    int p = 5;
    for (int k = 120; k < 200; ++k) {
      double g = k / (double) 100.0;
      double[] powers = new double[p];
      powers[0] = 1.0 / g;
      for (int r = 1; r < p; ++r) {
        powers[r] = powers[r - 1] / g;
      }
      DynamicArrayBucket<Double> b 
        = new DynamicArrayBucket<Double>(100);
      addSumsLessThanOne(powers, 0, 0, b);
      int c = b.size();
      Double[] x = new Double[c];
      b.emit(x, 0);
      double[] y = new double[c];
      for (int i = 0; i < c; ++i) {
        y[i] = x[i].doubleValue();
      }
      Arrays.sort(y);
      double minGap = 1.0;
      int count = 1;
      for (int i = 1; i < y.length; ++i) {
        if (y[i - 1] < y[i]) {
          ++count;
          double gap = y[i] - y[i - 1];
          if (gap < minGap) {
            minGap = gap;
          }
        }
      }
      System.out.println("" + g + "\t" + count + "\t" + 
      Math.floor(minGap * 1000000.0) / 1000000.0);
    }
  }
  @Test
  public void getSourceElementCount() {
  }
  private void addSumsLessThanOne
      ( double[] powers, int i, double lo
      , DynamicArrayBucket<Double> b) {
    boolean more = (i + 1 < powers.length);
    for (double x = lo; x < 1.0; x += powers[i]) {
      b.append(new Double(x));
      if (more) {
        addSumsLessThanOne(powers, i + 1, x, b);
      }
    }
  }
  @Test
  public void testFindCForG() {
    double g = 1.3;
    int    r = 8;
    double[] h = new double[r];
    h[0] = 1/g;
    //String line = "" + h[0];
    for (int i=1; i<r; ++i) {
      h[i] = h[i-1]/g;
      //line += ", " + h[i];
    }
    //System.out.println(line);
    TreeSet<Double> map = new TreeSet<Double>();
    TreeSet<Double> all = new TreeSet<Double>();
    double k = 1;
    map.add(new Double(k));
    for (int i = r - 1 ; i>=0; --i) {
      TreeSet<Double> newMap = new TreeSet<Double>();
      for (double d : map) {
        if ( d <= h[i]) {
          newMap.add(new Double(d));
        } else if ( d + h[i] < 1.0 ) {
          newMap.add(d+h[i]); //Turtle
        } else {
          for (double p = d; p>0; p-=h[i]) {
            newMap.add(new Double(p));
          }
        }
      }
      all.addAll(newMap);
      map = newMap;
    }
    System.out.println("c=" +map.size());
    double s = 0;
    double hi = h[r-1];
    for (int i=r;i<1000;++i) {
      hi/=g; 
      s+=hi;
    }
    System.out.println("sum(remaining Hi)=" + s);
    System.out.println("q=" + (k-s-0.5));
    double dPrev = 0;
    double minGap = 1.0;
    for (double d : map) {
      if (d-dPrev<minGap) minGap=d-dPrev;
      dPrev = d;
    }
    System.out.println("mingap=" + minGap);
  }
}

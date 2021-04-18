package jsortie.testing;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.Test;

import jsortie.janitors.CoprimeGapSequenceHelper;
import jsortie.janitors.insertion.ShellSort;
import jsortie.testing.object.MyInteger;

public class ShellSortTest extends SortTest {
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }
  @Test
  public void testPopularSequences() {
    int sCount = 6;
    int runCount = 25;
    for (int count=100; count<10000000; count=count*5/4) {
      String line = "" + count;
      double[] comparisons = new double[sCount];
      for (int r=0; r<runCount; ++r) {
        int[] vArray = random.randomPermutation(count);
        for (int s=0; s<sCount; ++s) {
          int[] gaps = popularSequence(s, count);
          int[] vCopy = Arrays.copyOf(vArray, vArray.length);
          comparisons[s] += getComparisonCount(vCopy, gaps);
        }
      }
      for (int s=0; s<sCount; ++s) {
        line += f5(comparisons[s]); 
      }
      System.out.println(line);
    }
  }
  private double getComparisonCount(int[] vArray, int[] gaps) {
    double comparisons  = 0;
    int count = vArray.length;
    int lastGapIndex = 0;
    while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] * 3 < count) {
      ++lastGapIndex;
    }
    for (int gapIndex = lastGapIndex; 0 <= gapIndex; --gapIndex) {
      int gap = gaps[gapIndex];
      int i = gap;
      for (; i < count; ++i) {
        int v = vArray[i];
        int h = i - gap;
        for (; 0 <= h; h -= gap) {
          comparisons += 1;
          if (vArray[h] <= v) {
            break;
          }
          vArray[h + gap] = vArray[h];
        }
        vArray[h + gap] = v;
      }
    }
    return comparisons;
  }
  public int[] popularSequence(int s, int n) {
    //Only for n<=10 million.
    switch (s) {
      case 0:  return new int[] { 1,3,7,21,48,112,336,861,1968,4592,13776,33936,86961,198768
                                , 463792, 1391376, 3402672, 8382192 };
      case 1:  return new int[] { 1,8,23,77,281,1073,16577,65921,262913,1050113,4197377 };
      case 2:  return new int[] { 1,5,19,41,109,209,505,929,2161,3905,8929,16001,36289,64769
                                , 146305, 260609, 587521, 1045505, 2354689, 4188161 };
      case 3:  return GonnetBaezaYatesSequence(n);
      case 4:  return new int[] { 1,4,9,20,46,103,233,525,1182,2660,5985,13467,30301
                                , 68178, 153401, 345152, 776591, 1747331, 3931496 };
      default: return new int[] { 1,4,10,23,57,132,301,701,1750,3937,8859,19932
                                , 44847, 100905, 227036, 510831, 1149369, 2586080, 5818680 };
    }
  }
  public int[] GonnetBaezaYatesSequence(int n) {
    return GBY2(n*5/11, n);
  }
  public int[] GBY2(int a, int b) {
    while (1<a && 1<CoprimeGapSequenceHelper.gcd(a, b)) {
      --a;
    }
    if (a<2) return new int[] {};
    int[] x= GBY2(a*5/11,a);
    int[] y = new int[ x.length + 1];
    for (int i=0; i<x.length; ++i) {
      y[i] = x[i];
    }
    y[x.length] = a;
    return y;
  }
	@Test
	public void testGrowthRatios() {
		for (int gg = 110; gg <= 1000; gg += 1) {
			double g = gg / 100.0;
			String line = "" + Math.floor(g * 100.0 + .5) / 100.0;
			for (int count = 1000; count <= 100000000; count *= 10) {
				double nLogn = count * Math.log(count);
				int runCount = 10000000 / count;
				if (runCount<1) runCount=1;
				int gaps[] = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
				int lastGapIndex = 0;
				while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] < count) {
					++lastGapIndex;
				}
				double comparisons = 0;
				for (int r = 0; r < runCount; ++r) {
					int[] vArray = random.randomPermutation(count);
					for (int gapIndex = lastGapIndex; 0 <= gapIndex; --gapIndex) {
						int gap = gaps[gapIndex];
						int i = gap;
						for (; i < count; ++i) {
							int v = vArray[i];
							int h = i - gap;
							for (; 0 <= h; h -= gap) {
								comparisons += 1;
								if (vArray[h] <= v) {
									break;
								}
								vArray[h + gap] = vArray[h];
							}
							vArray[h + gap] = v;
						}
					}
				}
				line += f5((double) comparisons / nLogn / (double) runCount);
			}
			System.out.println(line);
		}
	}
  @Test
  public void testDistribution() {
    int[] gaps = new int[] { 1,4,9,20,46,103,233,525,1182,2660,5985,13467,30301
                           , 68178, 153401, 345152, 776591, 1747331, 3931496 };
    ShellSort s = new ShellSort();
    int count = 1000000;
    int[] vArray = random.randomPermutation(count);
    for (int i=gaps.length-1;i>6;--i) {
      s.sortSlicesOfRange(vArray, 0, count, gaps[i]);
    }
    int[] scatter = new int[count*2];
    for (int i=0; i<count; ++i) {
      int d = vArray[i] - i;
      ++scatter[count+d];
    }
    int grain = 10;
    for (int d=-3000; d<3000; d+=grain) {
      int t=0;
      for (int d2=d; d2<d+grain; ++d2) {
        t += scatter[count+d2];
      }
      System.out.println("" + d + "\t" + t);
    }
  }
  @Test
  public void testTwoPassShellSorts() {
    int count = 1000;
    int runCount = 100;
    for (int x = 2; x < 40; ++x) {
      int comparisons = 0;
      for (int r = 0; r < runCount; ++r) {
        int[] vArray = random.randomPermutation(count);
        for (int gap = x; 1 <= gap; gap /= x) {
          int i = gap;
          for (; i < count; ++i) {
            int v = vArray[i];
            int h = i - gap;
            for (; 0 <= h; h -= gap) {
              ++comparisons;
              if (vArray[h] <= v) {
                break;
              }
              vArray[h + gap] = vArray[h];
            }
            vArray[h + gap] = v;
          }
        }
      }
      System.out.println("" + x + "\t" + comparisons / (double) count);
    }
  }
  @Test
  public void testThreePassShellSorts() {
    int count=1000;
    int runCount=1000;
    StringBuilder full = new StringBuilder();
    for (int x=5;x<50;x+=1) {
      String fullLine = "" + x;
      String partialLine = fullLine;
      for (int y=x+1;y<x+101;y+=1) {
        int[] gaps = new int[] { 1, x, y };
        int comparisons = 0;
        for (int r=0; r<runCount; ++r) {
          int[] vArray = random.randomPermutation(count);
          for (int gapIndex=2; 0<=gapIndex; --gapIndex) {
            int gap = gaps[gapIndex];
            int i=gap;
            for (; i<count; ++i) {
              int v = vArray[i];
              int h = i-gap;
              for (; 0<=h; h-=gap) {
                ++comparisons;
                if ( vArray[h]<=v) {
                  break;
                }
                vArray[h+gap] = vArray[h];
              }
              vArray[h+gap] = v;
            }
          }
        }
        String result = "" + Math.floor(comparisons / (double)count / (double)runCount * 100.0 + .5)/100.0;
        fullLine += "\t" + result; 
        if (1<CoprimeGapSequenceHelper.gcd(x, y)) {
          result="";
        }
        partialLine += "\t" + result;
      }
      System.out.println(partialLine);
      full.append(fullLine + "\n");
    }
    System.out.print("\n" + full.toString());
  }
  public int cByXToTheY(double c, double x, double y) {
    return (int)Math.floor(c*Math.exp(Math.log(x)*y)+.5);
  }
  @Test
  public void testComparisonsPerPass() {
    int count=1000;
    int runCount=1000;
    /*
    for (int x=0; x<3; ++x) {
      int[] gaps = null;
      if (x==0) gaps = new int[] { 1, 4,  25 }; //Knuth etc. suggested
      if (x==1) gaps = new int[] { 1, 7,  39 }; //James's choice (balances out)
      if (x==2) gaps = new int[] { 1, 5,  32 }; //Knuth etc. no justification
      generatePassCountLine(count,runCount,gaps,true);
    }
    System.out.println("");
    */
    for (count=1000; count <= 100000000; count=count*5/4 ) {
      runCount = 400;
      if (100000<runCount)  runCount=100;
      if (1000000<runCount) runCount=25;
      /*
      generatePassCountLine(count, runCount, new int[] 
        { 1, cByXToTheY(1, 16/Math.sqrt(8.0)*count/Math.PI, 1/3.0) }, true);
      generatePassCountLine(count, runCount, new int[] 
        { 1, cByXToTheY(1, 16*count/Math.PI, 1/3.0) }, true);
      generatePassCountLine(count, runCount, new int[] 
        { 1, cByXToTheY(1, 16*Math.sqrt(8.0)*count/Math.PI, 1/3.0) }, true);
      generatePassCountLine(count, 5, new int[] 
        { 1, cByXToTheY(1, count, 0.2), cByXToTheY(0.8, count, 7.0/15.0) }, true); //Knuth
      */
      generatePassCountLine(count, 5, new int[] 
        { 1, cByXToTheY(1, count, 0.25), cByXToTheY(1, count, 0.5) }, true, false); //Knuth 2 : Hypothetical
      generatePassCountLine(count, 5, new int[] 
        { 1, cByXToTheY(0.4, count, 1.0/3.0), cByXToTheY(0.5, count, 5.0/9.0) }, true, false); //James Hypothetical
      /*
       generatePassCountLine(count, 5, new int[] 
        { 1, cByXToTheY(1, count, 0.25), cByXToTheY(1, count, 0.5) }, true);
      generatePassCountLine(count, 5, new int[] 
        { 1, cByXToTheY(0.4, count, 1/3.0), cByXToTheY(0.6, count, 5.0/9.0) }, true);
        */
    }
  }
  @Test 
  public void testBlahsky() {
    int count=1000000;
    for (double w=.20; w<=0.5; w+=0.05) {
      System.out.println("w="+w);
      for (double x=w+0.01; x<0.8; x+=0.01) {
        generatePassCountLine(count, 5, new int[] 
          { 1, cByXToTheY(1, count, w), cByXToTheY(1, count, x) }, true, false);
      }
      System.out.println("");
    }
  }
  @Test
  public void testSecondPass() {
    double s=0;
    for (int a=1; a<100; ++a) s+=Math.sqrt(a*(100.0-a)/10000.0);
    System.out.println("" + s);
    generatePassCountLine(10000, 100000, new int[] { 1, 21, 100 }, true, true );
  }
  @Test
  public void testGOfRootN() {
    System.out.println("n\th1\th2\th3\tC1\tC2\tC3\tCt");
    for (int n=928143;n<100000000;n=n*5/4) {
      //int runCount = (n<1000000) ? 25 : ( (n<10000000) ? 5 : 1);
      generatePassCountLine(n, 25, new int[]
        { 1, cByXToTheY(1,  n, 0.25), cByXToTheY(1, n, 0.5) }, true, false);
    }
  }
  @Test
  public void testGOf2() {
    double g = 2;
    int gaps[] = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
    generatePassCountLine(1000000, 1, gaps, true, true );
    gaps[0]=2;
    gaps[1]=5;
    for (int i=2; i<gaps.length; ++i) {
      gaps[i] = gaps[i-1]*2;
      boolean stuffed = false;
      do {
        ++gaps[i];
        stuffed = false;
        for (int h=0;h<i;++h) {
          if (1<CoprimeGapSequenceHelper.gcd(gaps[i],gaps[h])) {
            stuffed = true;
          }
        }
      } while ( stuffed);
    }
    generatePassCountLine(1000000, 1, gaps, true, true );
  }
  public void generatePassCountLine(int count, int runCount, int[] gaps
    , boolean showIncrements, boolean showPredictions) {
    int passCount = gaps.length;
    double[] comparisons= new double[passCount];
    boolean boned;
    do {
      boned = false;
      for (int i=2; i<passCount; ++i) {
        if (1<CoprimeGapSequenceHelper.gcd(gaps[i-1], gaps[i])) {
          ++gaps[i];
          boned = true;
        }
      }
    } while (boned);
    //Don't want any gaps that share factors with predecessors
    for (int r=0; r<runCount; ++r) {
      int[] vArray = random.randomPermutation(count);
      for (int gapIndex=passCount-1; 0<=gapIndex; --gapIndex) {
        int gap = gaps[gapIndex];
        int i=gap;
        for (; i<count; ++i) {
          int v = vArray[i];
          int h = i-gap;
          for (; 0<=h; h-=gap) {
            ++comparisons[gapIndex];
            if ( vArray[h]<=v) {
              break;
            }
            vArray[h+gap] = vArray[h];
          }
          vArray[h+gap] = v;
        }
      }
    }
    String line = "" + count;
    if (showIncrements) {
      for (int i=passCount-1; 0<=i; --i) {
        line += "\t" + gaps[i];
      }
      if (showPredictions) {
        line += "\tactual";
      }
    }
    double t=0;
    for (int i=passCount-1; 0<=i; --i) {
      line += f5(comparisons[i]/runCount/count);
      t += comparisons[i]/runCount/count;
    }
    line += f5(t);
    if (showPredictions) {
      if (passCount<4) {
        line += "\tpredict";
        line += f5(count / 4.0 / gaps[passCount-1]);
        line += f5(Math.sqrt(Math.PI*(count+1)*(gaps[passCount-1]-1))/8.0/gaps[passCount-2]);
        if (passCount==3) {
          line += f5(Math.sqrt(3.0*(gaps[passCount-1]-1)*(gaps[passCount-2]-1)/2.0/Math.PI)/gaps[passCount-3]);
        }
      }
    }
    System.out.println(line);
  }
  public void generatePassCountLine(int count, int runCount, double g) {
    int gaps[] = CoprimeGapSequenceHelper.coPrimeGapSequence(g);
    int lastGapIndex = 0;
    while (lastGapIndex + 1 < gaps.length && gaps[lastGapIndex + 1] < count) {
      ++lastGapIndex;
    }
    gaps = Arrays.copyOf(gaps, lastGapIndex+2);
    generatePassCountLine(count, runCount, gaps, false, false);
  }
  @Test
  public void testInterestingG() {
    generatePassCountLine(10000000,5,(1+Math.sqrt(5.0))/2);
    generatePassCountLine(10000000,5,1.94);
    generatePassCountLine(10000000,5,2);
    generatePassCountLine(10000000,5,2.25);
    generatePassCountLine(10000000,5,2.78);
    generatePassCountLine(10000000,5,3.0);
  }
  @Test
  public void testBleh() {
    for (int n = 1; n <= 100; ++n) {
      double s = 0;
      for (int k = 1; k <= n; ++k) {
        s += Math.sqrt(k * (n - k));
      }
      System.out.println("" + n + "\t" + s + "\t" + 3.1415926 * n * n / 8);
    }
  }
  @Test
  public void testThingy() {
    for (int g=5;g<1000;g=g*5/4) {
      int h= g*9/4;
      /*for (int h=g*9/4;h<=g*9/4;++h)*/ {
        while (1<CoprimeGapSequenceHelper.gcd(g, h)) { ++h; }
        if (1==CoprimeGapSequenceHelper.gcd(g, h)) {
          int s = (int)Math.floor(Math.sqrt((g-1)*(h-1)));
          MyInteger f;
          TreeSet<MyInteger> list = new TreeSet<MyInteger>(MyInteger.integerComparator);
          list.add(f=new MyInteger(0));
          for (int i=0;i<s;++i) {
            f = list.first();
            //System.out.print("" + f.intValue() + " ");
            list.remove(f);
            list.add(new MyInteger(f.intValue()+g));
            list.add(new MyInteger(f.intValue()+h));
          }
          double d = f.intValue();
          System.out.println("" + g + "\t" + h + "\t" + s + "\t" + d + "\t"
            + Math.log(d)/Math.log(s*s));
        }
      }
    }
  }
}

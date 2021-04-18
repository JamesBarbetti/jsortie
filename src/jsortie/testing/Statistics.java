package jsortie.testing;

import jsortie.quicksort.selector.clean.CleanRightHandedSelector;
import jsortie.quicksort.governor.traditional.QuicksortBestOf3;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;

public class Statistics 
{
  public static void samplingHistogram() {	
    CleanRightHandedSelector asym   = new CleanRightHandedSelector(false);
    CleanRemedianSelector    median = new CleanRemedianSelector(false); 
    QuicksortBestOf3 qsb3 = new QuicksortBestOf3();
    int bars     = 100;
    int count[]  = new int[bars];
    int count2[] = new int[bars];
    for (int s=3; s<10000; s*=3) { //sample size 
      for (int b=0; b<bars;++b) {
        count2[b]=count[b]=0;
      }
      int vArray[] = new int[s];
      for (int r=0; r<5000; ++r) { //run
        for (int i=0; i<s; ++i) {
          vArray[i] = (int) (Math.floor(Math.random() * bars));
        }
        ++count[vArray[median.selectCandidateFromRange(vArray, 0, vArray.length)]];	
        qsb3.sortRange(vArray, 0, vArray.length);
        ++count2[vArray[vArray.length/2]];					
      }
      String x = "k=";
      x = x + s;
      for (int b=0; b<bars;++b) {
        x = x + "\t" + count[b];
      }
      System.out.println ( x);			
    }
    for (int s=4; s<10000; s*=3) { //sample size
      for (int b=0; b<bars;++b) {
        count2[b]=count[b]=0;
      }
      int vArray[] = new int[s];
      for (int r=0; r<5000; ++r) { //run
        for (int i=0; i<s; ++i) {
          vArray[i] = (int) (Math.floor(Math.random() * bars));
        }
        ++count[vArray[asym.selectCandidateFromRange(vArray, 0, vArray.length)]];	
        qsb3.sortRange(vArray, 0, vArray.length);
        ++count2[vArray[vArray.length/6]];					
      }
      String x = "k=";
      x = x + s;
      for (int b=0; b<bars;++b) {
        x = x + "\t" + count[b];
      }
      System.out.println ( x);			
    }
  }
}

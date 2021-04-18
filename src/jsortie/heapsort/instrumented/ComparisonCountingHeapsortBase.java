package jsortie.heapsort.instrumented;

import jsortie.RangeSorter;
import jsortie.heapsort.HeapsortBase;

public abstract 
  class ComparisonCountingHeapsortBase 
  extends HeapsortBase
  implements RangeSorter {
  public double constructionComparisonCount;
  public double constructionLiftCount;
  public double constructionLiftedItemCount;
  public double[] sourcePositionToLiftCount;
  
  public double extractionComparisonCount;
  public double extractionLiftCount;
  public double extractionLiftedItemCount;
  public void zeroComparisonCount() {
    constructionComparisonCount = 0;
    constructionLiftCount       = 0;
    constructionLiftedItemCount = 0;
    extractionComparisonCount   = 0;
    extractionLiftCount         = 0;
    extractionLiftedItemCount   = 0;
  }
  public double getComparisonCount() {
    return constructionComparisonCount + extractionComparisonCount;
  }
  public double getExtractionComparisonCount() {
    return extractionComparisonCount;
  }
  public double getExtractionLiftedItemCount() {
    return extractionLiftedItemCount;
  }
  public double getExtractionLiftCount() {
    return extractionLiftCount;
  }
  public double getConstructionLiftedItemCount() {
    return constructionLiftedItemCount;
  }
  public double getConstructionLiftCount() {
    return constructionLiftCount;
  }
  public double[] getExtractionLiftCountVector() {
    return sourcePositionToLiftCount;
  }  
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (start+1<stop) {
      if (sourcePositionToLiftCount==null) {
        sourcePositionToLiftCount = new double[stop];
      } else if (sourcePositionToLiftCount.length<stop) {
        double[] oldPos = sourcePositionToLiftCount;
        sourcePositionToLiftCount = new double[stop];
        for (int i=0; i<oldPos.length; ++i) {
          sourcePositionToLiftCount[i] = oldPos[i];
        }
      }
      constructHeap(vArray, start, stop);
      extractFromHeap(vArray, start, stop);
    }
  }

}

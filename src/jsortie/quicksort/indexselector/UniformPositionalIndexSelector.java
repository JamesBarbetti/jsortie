package jsortie.quicksort.indexselector;

public class UniformPositionalIndexSelector 
  implements IndexSelector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] selectIndices
    ( int start, int stop, int sampleSize) {
    int[] sample;
    double step = (double)(stop-start+1) 
                / (double)(sampleSize+1);
    if (step<1.0) {
      if (start<stop) {
        sample = new int[stop-start];
        for (int i=start; i<stop; ++i) {
          sample[i-start]=i;
        }
      } else {
        sample = new int[] { start };
      }
    } else {
      sample = new int[sampleSize];
      double position = start - .5;
      for (int i=0; i<sampleSize; ++i) {
        position += step;
        sample[i] 
          = (int)Math.floor(position);
      }
    }
    return sample;
  }  
}

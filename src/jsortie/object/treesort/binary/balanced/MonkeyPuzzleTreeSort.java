package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class MonkeyPuzzleTreeSort<T>
  implements ObjectRangeSorter<T> {
  boolean tweaked;
  public String toString() {
    return this.getClass().getSimpleName()
        + ( tweaked ? "(tweaked)" : "(untweaked)");
  }
  public MonkeyPuzzleTreeSort
    ( boolean useTweakedVersion) {
    this.tweaked    = useTweakedVersion;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    MonkeyPuzzleBucket<T> bucket = tweaked
      ? new MonkeyPuzzleBucketTweaked<T>(comparator)
      : new MonkeyPuzzleBucket<T>(comparator);
    for (int scan=start;scan<stop;++scan) {
      bucket.append(vArray[scan]);
    }
    bucket.emit(vArray, start);
  }
  protected double minTPL(int count) {
    return (count<2) 
      ? 0 
      : ((count-1) + minTPL(count/2) + minTPL(count-count/2-1));
  }
}

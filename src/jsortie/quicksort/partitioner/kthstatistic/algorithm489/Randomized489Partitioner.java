package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.RandomSampleCollector;

public class Randomized489Partitioner 
  extends IntroSelect489Partitioner {
  public Randomized489Partitioner() {
    collector = new RandomSampleCollector();
  }
}

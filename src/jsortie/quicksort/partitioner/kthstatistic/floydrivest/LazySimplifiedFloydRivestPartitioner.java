package jsortie.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;

public class LazySimplifiedFloydRivestPartitioner
  extends FloydRivestPartitionerBase {
  protected ShiftHelper shifty 
    = new ShiftHelper();
  public LazySimplifiedFloydRivestPartitioner() {
    super ();
  }
  public LazySimplifiedFloydRivestPartitioner
    ( PartitionExpander expanderOnLeft
    , PartitionExpander expanderOnRight) {
    super(expanderOnLeft, expanderOnRight);
  }
  public LazySimplifiedFloydRivestPartitioner
    ( SampleCollector collectorToUse
    , PartitionExpander expanderOnLeft
    , PartitionExpander expanderOnRight) {
    super ( collectorToUse 
          , expanderOnLeft
          , expanderOnRight);
  }
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int targetIndex ) {
    int timeToLive  = getTimeToLive(stop-start);
    if (isFoldingWanted) {
      fold(vArray, start, stop);
      timeToLive -= (stop-start)/2;
    }
    int whatsLeft 
      = partition 
        ( vArray, start
        , start, targetIndex
        , stop, stop, 0.0
        , timeToLive, true);
    if (whatsLeft<0 && useSafetyNet) {
      lastResort.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
    }
    int comparisonsDone = timeToLive - whatsLeft;
    comparisonCount += comparisonsDone; 
    //doesn't count the comparisons done by the 
    //safety net!
  }
  protected int partition 
    ( int[] vArray
    , int outerStart, int start, int k
    , int stop, int outerStop 
    , double d, int timeToLive
    , boolean outward) {
    int m = stop - start; 
    for ( ; janitorThreshold<m; m=stop-start ) {
      int    c = sizer.getSampleSize(m, 2);
      while ( c+c>=m) {
        c /= 2;
      }
      if (c<2) {
        c=2;
      }
      int sampleStart 
        = ps.getSampleStart(start, stop, k, c);
      int sampleStop = sampleStart + c;
      timeToLive    -= collectSample
          ( vArray, start, stop
          , sampleStart, sampleStop);
      if (timeToLive<0 && useSafetyNet) {
        return timeToLive;
      }
      int    jPrime1;
      int    jPrime2;
      if (2<c) {
        double t     = (double)(k+1-start) 
                     * (double)(c+1)
                     / (double)(m+1);
        double delta = ps.getDelta(d, m, c, t);
        double j1    = sampleStart + t - 1 - delta;
        double j2    = sampleStart + t - 1 + delta;
        jPrime1      
          = ps.fixLowerSampleTarget
            ( sampleStart, t, t,j1, j2, sampleStop );
        jPrime2
          = ps.fixUpperSampleTarget
            ( sampleStart, t, t, j1, j2, sampleStop );
      } else {
        jPrime1 = sampleStart;
        jPrime2 = sampleStart + 1;
      }
      int x
        = jPrime2-jPrime1-1; 
       //# items in middle partition in sample
      int    split1;
      int    split2;
      double d2 = (d==0) 
                ? Math.sqrt(Math.log(m)) : d;
      if (jPrime1-sampleStart<=sampleStop-jPrime2) {
        //1st pivot closer
        timeToLive = 
          partition 
          ( vArray,  sampleStart, sampleStart
          , jPrime1, sampleStop,  sampleStop
          , d2, timeToLive, outward);
        if (timeToLive<0 && useSafetyNet) {
          return timeToLive;
        }
        shifty.moveBackElementsToFront
        ( vArray, start, sampleStart, jPrime1+1 ); 
        //sample v1 and to left, all the way left
        shifty.moveFrontElementsToBack
        ( vArray, jPrime1+1, sampleStop, stop );
        //sample right of v1, all the way right
        int innerStart = jPrime1+start-sampleStart;
        int innerStop  = jPrime1+1+stop-sampleStop;
        split1 
          = (outward ? rightExpander : leftExpander)
            .expandPartition
            ( vArray, innerStart, innerStart
            , innerStart, innerStart+1, innerStop);
        timeToLive -= (innerStop-innerStart-1);
        if ( k == split1 ) {
          return timeToLive;
        } else if ( k < split1 ) {
          split2 = split1;
        } else {
          int shift  = stop - sampleStop;
          timeToLive 
            = partition 
              ( vArray, jPrime1 + 1 + shift
              , jPrime1 + 1 + shift
              , jPrime2 + shift, stop, stop
              , d2, timeToLive, outward);
          if (timeToLive<0 && useSafetyNet) {
            return timeToLive;
          }
          shifty.moveBackElementsToFront
            ( vArray, split1+1
            , jPrime1 + 1 + shift
            , jPrime2 + shift);
          innerStart = split1 + x + 1;
          innerStop 
            = jPrime2 + stop - sampleStop + 1;
          split2 
            = (outward ? leftExpander : rightExpander)
              .expandPartition
              ( vArray, innerStart, innerStop-1
              , innerStop-1, innerStop, innerStop);
          timeToLive -= (innerStop - innerStart -1);
          if ( k== split2 ) {
            return timeToLive;
          }
        }
      } else {
        timeToLive 
          = partition 
            ( vArray, sampleStart, sampleStart
            , jPrime2, sampleStop, sampleStop
            , d2, timeToLive, outward);
        if (timeToLive<0 && useSafetyNet) {
          return timeToLive;
        }
        shifty.moveBackElementsToFront
          ( vArray, start,   sampleStart, jPrime2); 
          //sample left of v2 all the way left
        shifty.moveFrontElementsToBack
          ( vArray, jPrime2, sampleStop,  stop);
          //sample v2 and to right, all the way right
        int innerStart 
          = jPrime2 + start - sampleStart;
        int innerStop  
          = jPrime2 + 1 + stop  - sampleStop;
        split2 
          = (outward ? leftExpander : rightExpander)
            .expandPartition
              ( vArray, innerStart, innerStop-1
              , innerStop-1, innerStop, innerStop);
        timeToLive -= (innerStop-innerStart-1);
        if ( k == split2 ) {
          return timeToLive;
        } else if ( split2 < k) {
          split1 = split2;
        } else {
          timeToLive 
            = partition
              ( vArray, start, start
              , jPrime1 + start - sampleStart
              , jPrime2 + start - sampleStart
              , jPrime2 + start - sampleStart
              , d2, timeToLive, outward);
          if (timeToLive<0 && useSafetyNet) {
            return timeToLive;
          }
          shifty.moveFrontElementsToBack
            ( vArray
            , jPrime1 + 1 + start - sampleStart
            , jPrime2 + start - sampleStart
            , split2); 
            //move sample middle right
          innerStart 
            = jPrime1 + start - sampleStart;
          innerStop = split2 - x;
          split1 
            = (outward ? rightExpander : leftExpander)
              .expandPartition
              ( vArray, innerStart, innerStart
              , innerStart, innerStart+1, innerStop);
          timeToLive -= (innerStop-innerStart-1);
          if ( k==split1) {
            return timeToLive;
          }
        }
      }
      if (quintary) {
        if ( k < split1 ) {
          split1 
            = quinny.moveEqualOrGreaterToRight
              ( vArray, start
              , split1+1, vArray[split1] );
          if ( split1 <= k ) {
            return timeToLive;
          }
        } else if ( k!= split1 ) {
          if ( k < split2 ) {
            split1 
              = quinny.moveEqualOrLessToLeft
                ( vArray, split1 + 1
                , split2, vArray[split1] );
            if ( k < split1 ) {
              return timeToLive;
            }
          }
        }
        if ( split2 < k ) {
          split2 = quinny.moveEqualOrLessToLeft
            ( vArray, split2+1
            , stop, vArray[split2] );
          if ( k < split2 ) {
            return timeToLive;
          }
        } else if ( split2 != k ) {
          if ( split1 < k ) {
            split2  
              = quinny.moveEqualOrGreaterToRight
                ( vArray, split1+1
                , split2, vArray[split2] );
            if ( split2 <= k ) {
              return timeToLive;
            }
          }
        } 
      }
      if (k<split1) {
        --timeToLive;
        if ( outerStart<start 
             && vArray[start-1]==vArray[split1]) {
          return timeToLive;
        }
        stop = split1;
      } else if (k<split2) {
        --timeToLive;
        if ( vArray[split1]==vArray[split2]) {
          return timeToLive;
        }
        start = split1+1;
        stop  = split2;
      } else {
        start = split2+1;
        --timeToLive;
        if ( stop<outerStop 
              && vArray[split2]==vArray[stop]) {
          return timeToLive;
        }
      }      
      if (timeToLive<0 && useSafetyNet) {
        return timeToLive;
      }
      d=0;
      outward = !outward;
    }
    if (1<m) {
      timeToLive -= m*(m-1)/2;
      janitor.partitionRangeExactly
        ( vArray, start, stop, k );
    }
    return timeToLive;
  }
} 


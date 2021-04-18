package jsortie.object.quicksort.partitioner.kthstatistic.floydrivest;

import java.util.Comparator;

import jsortie.object.quicksort.expander.PartitionObjectExpander;

public class SimplifiedFloydRivestObjectPartitioner<T>
  extends FloydRivestObjectPartitionerBase<T> {
  public SimplifiedFloydRivestObjectPartitioner(double alpha) {
    super(alpha);
  }
  protected int partition 
    ( Comparator<? super T> comparator
    , T[] vArray, int outerStart
    , int start, int k
    , int stop, int outerStop 
    , double d, int timeToLive
    , boolean outward) {
    int m = stop - start; 
    for ( ; janitorThreshold<m; m=stop-start ) {
      int    c = sizer.getSampleSize(m, 2);
      if (c<2) {
        c=2;
      }
      //In general, we'd usually like to have (c%3)==2.
      int sampleStart 
        = ps.getSampleStart(start, stop, k, c);
      int sampleStop = sampleStart + c;
      timeToLive    -= collectSample
          ( comparator, vArray, start, stop
          , sampleStart, sampleStop);
      if (useSafetyNet && timeToLive<0) {
        return timeToLive;
      }
      int    jPrime1;
      int    jPrime2;
      if (2<c) {
        double t     = (double)(k-start+1) * (double)(c+1)
                       / (double)(m+1) - 1;
        double delta = ps.getDelta(d, m, c, t);
        double j1    = sampleStart + t- delta;
        double j2    = sampleStart + t + delta;
        jPrime1 = ps.fixLowerSampleTarget
                  ( sampleStart, t, t, j1, j2, sampleStop );
        jPrime2 = ps.fixUpperSampleTarget
                  ( sampleStart, t, t, j1, j2, sampleStop );
      } else {
        jPrime1 = sampleStart;
        jPrime2 = sampleStart+c-1;
      }
      int x = jPrime2-jPrime1-1; 
              //# items in middle partition in sample
              //note: could be -1
      PartitionObjectExpander<T> lx 
        = outward ? rightExpander : leftExpander;
      PartitionObjectExpander<T> rx 
        =  outward ? leftExpander : rightExpander;
      int    split1;
      int    split2;
      double d2 = (d==0) ? Math.sqrt(Math.log(m)) : d;
      if (jPrime1==jPrime2) {
        split1 
          = expandWithOnePivot 
            ( comparator, vArray
            , start, sampleStart, jPrime1
            , sampleStop, stop, lx, rx);
        split2 = split1;
      } else if (jPrime1-sampleStart<=sampleStop-jPrime2) {
        //1st pivot closer to the sample median
        timeToLive
          = partition 
            ( comparator, vArray, sampleStart, sampleStart
            , jPrime1, sampleStop,  sampleStop
            , d2, timeToLive, outward);
        if ( useSafetyNet && timeToLive < 0 ) {
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
        split1 = lx.expandPartition
                 ( comparator, vArray, innerStart, innerStart
                 , innerStart, innerStart+1, innerStop);
        timeToLive -= (innerStop-innerStart-1);
        if ( k == split1 ) {
          return timeToLive;
        } else if ( beLazy && k < split1 ) {
          split2 = split1;
        } else {
          int shift  = stop - sampleStop;
          timeToLive = partition 
                       ( comparator, vArray, jPrime1 + 1 + shift
                       , jPrime1 + 1 + shift
                       , jPrime2 + shift, stop, stop
                       , d2, timeToLive, outward);
          if ( useSafetyNet && timeToLive < 0 ) {
            return timeToLive;
          }
          shifty.moveBackElementsToFront
            ( vArray, split1+1, jPrime1 + 1 + shift
            , jPrime2 + shift);
          innerStart = split1 + x + 1;
          innerStop  = jPrime2 + stop - sampleStop + 1;
          if (innerStart<start) {
            System.out.println("screwed");
          }
          split2 = rx.expandPartition
                   ( comparator, vArray, innerStart, innerStop-1
                   , innerStop-1, innerStop, innerStop);
          timeToLive -= (innerStop - innerStart -1);
          if ( k== split2 ) {
            return timeToLive;
          }
        }
      } else {
        timeToLive = partition 
                     ( comparator, vArray, sampleStart, sampleStart
                     , jPrime2, sampleStop, sampleStop
                     , d2, timeToLive, outward);
        if ( useSafetyNet && timeToLive < 0 ) {
          return timeToLive;
        }
        shifty.moveBackElementsToFront
          ( vArray, start,   sampleStart, jPrime2); 
          //sample left of v2 all the way left
        shifty.moveFrontElementsToBack
          ( vArray, jPrime2, sampleStop,  stop);
          //sample v2 and to right, all the way right
        int innerStart = jPrime2 +     start - sampleStart;
        int innerStop  = jPrime2 + 1 + stop  - sampleStop;
        split2 = rx.expandPartition
                 ( comparator, vArray, innerStart, innerStop-1
                 , innerStop-1, innerStop, innerStop);
        timeToLive -= (innerStop-innerStart-1);
        if ( k == split2 ) {
          return timeToLive;
        } else if ( beLazy && split2 < k) {
          split1 = split2;
        } else {
          timeToLive = partition
                       ( comparator, vArray, start, start
                       , jPrime1 + start - sampleStart
                       , jPrime2 + start - sampleStart
                       , jPrime2 + start - sampleStart
                       , d2, timeToLive, outward);
          if ( useSafetyNet && timeToLive < 0 ) {
            return timeToLive;
          }
          shifty.moveFrontElementsToBack
            ( vArray, jPrime1 + 1 + start - sampleStart
            , jPrime2 + start - sampleStart, split2); 
            //move sample middle right
          innerStart = jPrime1 + start - sampleStart;
          innerStop  = split2 - x;
          split1 = lx.expandPartition
                   ( comparator, vArray, innerStart, innerStart
                   , innerStart, innerStart+1, innerStop);
          timeToLive -= (innerStop-innerStart-1);
          if ( k==split1) {
            return timeToLive;
          }
        }
        d=0;
      }
      if (quintary) {
        if ( k < split1 ) {
          split1 
            = quinny.moveEqualOrGreaterToRight
              ( comparator, vArray, start
              , split1+1, vArray[split1] );
          if ( split1 <= k ) {
            return timeToLive;
          }
        } else if ( k!= split1 ) {
          if ( k < split2 ) {
            split1 
              = quinny.moveEqualOrLessToLeft
                ( comparator, vArray
                , split1 + 1, split2, vArray[split1] );
            if ( k < split1 ) {
              return timeToLive;
            }
          }
        }
        if ( split2 < k ) {
          split2 = quinny.moveEqualOrLessToLeft
            ( comparator, vArray, split2+1
            , stop, vArray[split2] );
          if ( k < split2 ) {
            return timeToLive;
          }
        } else if ( split2 != k ) {
          if ( split1 < k ) {
            split2  
              = quinny.moveEqualOrGreaterToRight
                ( comparator, vArray, split1+1
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
        if (k==split1) {
          return timeToLive;
        }
        if ( split1<split2 ) { 
         --timeToLive;
          if ( vArray[split1]==vArray[split2]) {
            return timeToLive;
          }
        }
        start = split1+1;
        stop  = split2;
        outward = !outward;
      } else {
        start = split2+1;
        if ( stop<outerStop ) {
          --timeToLive;
          if ( vArray[split2]==vArray[stop]) {
            return timeToLive;
          }
        }
      }
      if ( useSafetyNet && timeToLive < 0 ) {
        return timeToLive;
      }
    }
    if (1<m) {
      timeToLive -= m*(m-1)/2;
      janitor.partitionRangeExactly
        ( comparator, vArray, start, stop, k );
    }
    return timeToLive;
  }
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int targetIndex ) {
    int timeToLive  = getTimeToLive(stop-start);
    if (isFolding) {
      fold(comparator, vArray, start, stop);
      timeToLive -= (stop-start)/2;
    }
    int whatsLeft 
      = partition 
        ( comparator, vArray, start
        , start, targetIndex, stop, stop
        , 0.0, timeToLive, true);
    if (useSafetyNet && whatsLeft<0) {
      lastResort.partitionRangeExactly
      ( comparator, vArray, start, stop
      , targetIndex );
    }
    int comparisonsDone = timeToLive - whatsLeft;
    comparisonCount += comparisonsDone; 
    //doesn't count the comparisons done by the 
    //safety net!
  }
}

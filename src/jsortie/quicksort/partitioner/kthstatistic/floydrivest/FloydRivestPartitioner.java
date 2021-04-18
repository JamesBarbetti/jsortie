package jsortie.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;

public class FloydRivestPartitioner 
  extends FloydRivestPartitionerBase {
  FancierEgalitarianPartitionerHelper quinny
    = new FancierEgalitarianPartitionerHelper();
    //Only used when quintary == true
  //boolean traced = true;
  public FloydRivestPartitioner() {
  }
  public FloydRivestPartitioner(double a) {
    alpha = a;
  }
  public FloydRivestPartitioner
    ( SampleCollector sc
    , PartitionExpander lx
    , PartitionExpander rx) {
    super(sc, lx, rx);
  }
  public void setQuintary(boolean b) {
    quintary = b;
  }
  protected int doublePartition
    ( int[] vArray, int outerStart, int start
    , int kLeft, int kCentral, int kRight 
    , int stop,  int outerStop 
    , double d,  int timeToLive
    , boolean outward) {
    for ( int m = stop - start
        ; janitorThreshold < m
        ; m = stop - start
        , d = Math.sqrt(Math.log(m)) ) {
      if ( outerStart<start
           && stop<outerStop) {
        --timeToLive;
        if (useSafetyNet && timeToLive<0) {
          return timeToLive;
        }
        if ( vArray[start-1] == vArray[stop] ) {
          //Every item under consideration must 
          //already be equal, since they're all 
          //>=vArray[start-1] and all <=vArray[stop].
          return timeToLive;
        }
      }
      int    c = sizer.getSampleSize(m, 2);
      if (c<2) {
        c=2;
      }
      int sampleStart 
        = ps.getSampleStart(start, stop, kCentral, c);
      int sampleStop = sampleStart + c;
      timeToLive    -= collectSample
        ( vArray, start, stop
        , sampleStart, sampleStop);
      if ( useSafetyNet && timeToLive < 0 ) {
        return timeToLive;
      }
      int split1;
      int split2;
      int jCentre;
      if (2<c) {
        double t      = (double)(kCentral-start+1)
                      * (double)(c+1)
                      / (double)(m+1) - 1;
        double delta   = ps.getDelta(d, m, c, t);
        double a1 = t- delta;
        double a2 = t + delta;
        double t1 = t + (double)(kLeft-kCentral)/(c+1)/(m+1);
        double t2 = t + (double)(kRight-kCentral)/(c+1)/(m+1);
        jCentre = (int) Math.floor(sampleStart + t +.5);
        split1      = ps.fixLowerSampleTarget
                  ( sampleStart, sampleStart+t1, sampleStart+t2
                  , sampleStart+a1, sampleStart+a2, sampleStop );
        split2      = ps.fixUpperSampleTarget
                  ( sampleStart, sampleStart+t1, sampleStart+t2
                  , sampleStart+a1, sampleStart+a2, sampleStop );
        if ( jCentre<split1 || split2<jCentre ) {
          jCentre = split1 + (split2-split1)/2;
        }
      } else {
        split1  = sampleStart;
        split2  = sampleStart+c-1;
        jCentre = sampleStart;
      }
      double d2 = (d==0) ? Math.sqrt(Math.log(m)) : d;
      timeToLive
        = doublePartition 
          ( vArray, sampleStart, sampleStart
          , split1, jCentre, split2, sampleStop,  sampleStop
          , d2, timeToLive, outward);
      if ( useSafetyNet && timeToLive < 0 ) {
        return timeToLive;
      }
      PartitionExpander lx 
        = outward ? rightExpander : leftExpander;
      PartitionExpander rx 
        =  outward ? leftExpander : rightExpander;
      if (split1==split2) {
        split1 
          = expandWithOnePivot 
            ( vArray
            , start, sampleStart, split1
            , sampleStop, stop
            , lx, rx);
        split2 = split1;
      } else if (sampleStop-split2<=split1-sampleStart) {
        //1st pivot closer to the sample median
        int vRight = vArray[split2]; //move second pivot aside
        vArray[split2] = vArray[sampleStop-1];
        vArray[sampleStop-1] = vArray[stop-1];
        vArray[stop-1] = vRight;
        split1 
          = lx.expandPartition
            ( vArray, start, sampleStart
            , split1, sampleStop-1, stop-1);
        timeToLive -= stop-start-sampleStop+sampleStart-2;
        if ( useSafetyNet && timeToLive < 0 ) {
          return timeToLive;
        }
        if (beLazy && kRight<=split1) {
          split2 = split1;
        } else {
          split2 
            = rx.expandPartition
              ( vArray, split1+1, stop-1
              , stop-1, stop, stop);
          timeToLive -= stop-split1-2;
        }
      } else {
        //2nd pivot closer to the sample median
        int vLeft = vArray[split1]; //move 1st pivot aside
        vArray[split1] = vArray[sampleStart];
        vArray[sampleStart] = vArray[start];
        vArray[start] = vLeft;
        split2
          = rx.expandPartition
            ( vArray, start+1, sampleStart+1
            , split2, sampleStop, stop);
        timeToLive -= stop-start-sampleStop+sampleStart-2;
        if ( useSafetyNet && timeToLive < 0 ) {
          return timeToLive;
        }
        if (beLazy && split2<=kLeft) {
          split1 = split2;
        } else {
          split1 
            = lx.expandPartition
              ( vArray, start, start
              , start, start+1, split2);
          timeToLive -= split2-start-1;
        }
      }
      if ( useSafetyNet && timeToLive < 0 ) {
        return timeToLive;
      }
      int leftStop    = split1;
      int middleStart = split1+1;
      int middleStop  = split2;
      int rightStart  = split2+1;
      if (quintary) {
        int vLeft  = vArray[split1];
        int vRight = vArray[split2]; 
        if (kRight <= leftStop) {
          leftStop 
            = quinny.moveEqualOrGreaterToRight
              ( vArray, start, split1, vLeft);
        }
        if ( split1 < split2 ) {
          if ( vLeft < vRight ) {
            if ( ( middleStart <= kRight && kRight < middleStop )
                 || ( middleStart <= kLeft && kLeft < middleStop ) ) {
              middleStart
                = quinny.moveEqualOrLessToLeft
                  ( vArray, middleStart, middleStop, vLeft);
            }
            if ( ( middleStart <= kRight && kRight < middleStop )
                 || ( middleStart <= kLeft && kLeft < middleStop ) ) {
              middleStop
                = quinny.moveEqualOrGreaterToRight
                  ( vArray, middleStart, middleStop, vRight);
            }
          } else {
            //All items between vArray[split1]
            //and vArray[split2] inclusive must
            //be equal, so middle partition is empty.
            middleStart = split1;
            middleStop  = split1;
          }
        }
        if ( rightStart <= kLeft) {
          rightStart
            = quinny.moveEqualOrLessToLeft
              ( vArray, split2+1, stop, vRight);
        }
      }
      int leftPartition = 1;
      if (middleStart<=kLeft) {
        //neither kLeft, nor kRight in partition#1,
        if (rightStart<=kLeft) {
          //both kLeft and kRight in partition#3.
          leftPartition = 3;
          start = rightStart;
        } else {
          leftPartition = 2;
          start = middleStart;
        }
      }
      int rightPartition = 3;
      if (kRight<middleStop) {
        //neither kLeft, nor kRight in partition #3
        if (kRight<leftStop) {
          //both kLeft and kRight in partition#1
          rightPartition = 1;
          stop = leftStop;
        } else {
          rightPartition = 2;
          stop = middleStop;
        }
      }
      if (kLeft<start) {
        //Got kLeft! Yay!
        if (kRight<start) {
          //Got kRight too! Double yay!
          return timeToLive;
        } else {
          //Now we're after just one pivot
          kCentral = kLeft = kRight;
          continue;
        }
      }
      if (stop<=kRight) {
        //got kRight! Yay!
        if (stop<=kLeft) {
          //Got kLeft too! Double yay!
          return timeToLive;
        } else {
          //Now we're after just one pivot
          kCentral = kRight = kLeft;
          continue;
        }
      }
      if (leftPartition==rightPartition) {
        //If leftPartition==rightPartition, then
        //both start and stop are already been set 
        //appropriately for that partition, and
        //we can continue searching for kLeft and kRight.
        //
        //In the surviving partition, we suppose k
        //is in the middle. But since start and stop
        //have "moved inward" (and we don't know how much
        //by), we don't know whether kLeft and kRight
        //are both still bracketed (by kCentral and d).
        //So, we shift our aim, aiming for whichever of
        //them is closer to the median.
        //
        int middle = start + (stop-start)/2;
        kCentral 
          = ( middle-kLeft < kRight-middle )
          ? kLeft : kRight;
        outward = !outward;
      } else {
        //kLeft and kRight are in different partitions
        //Three cases: {1,2}, {1,3}, or {2,3}
        if (leftPartition==1) {
          if (rightPartition==2) {
            //{1,2}
            if (leftStop-start<middleStop-middleStart) {
              //Partition #1 is smaller than #2
              timeToLive 
                = doublePartition
                  ( vArray , outerStart, start
                  , kLeft, kLeft, kLeft
                  , leftStop, outerStop
                  , 0, timeToLive, outward);
              kCentral = kLeft = kRight;
              start   = middleStart;
              stop    = middleStop;
              outward = !outward;
            } else {
              //Partition #2 is smaller than #1
              timeToLive 
                = doublePartition
                  ( vArray , outerStart, middleStart
                  , kRight, kRight, kRight
                  , middleStop, outerStop
                  , 0, timeToLive, !outward);
              kCentral = kRight = kLeft;
              stop = leftStop;
            }
          } else {
            //{1,3}
            if (leftStop-start<stop-rightStart) {
              //Partition #1 is smaller than #3
              timeToLive 
                = doublePartition
                ( vArray, outerStart, start
                , kLeft, kLeft, kLeft
                , leftStop, outerStop
                , 0, timeToLive, outward);
              //Now, find kRight in partition #3
              kCentral = kLeft = kRight;
              start    = rightStart;
            } else {
              //Partition #3 is smaller than #1
              timeToLive 
                = doublePartition
                  ( vArray, outerStart, rightStart
                  , kRight, kRight, kRight
                  , stop, outerStop
                  , 0, timeToLive, outward);
              //Now, find kLeft in partition #1
              kCentral = kRight = kLeft;
              stop     = leftStop;
            }
          }
        } else {
          //{2,3}
            if (middleStop-middleStart<stop-rightStart) {
              //Partition #2 is smaller than #3
              timeToLive 
                = doublePartition
                  ( vArray, outerStart, middleStart
                  , kLeft, kLeft, kLeft
                  , middleStop, outerStop
                  , 0, timeToLive, !outward);
              //Now... find kRight in partition #3
              kCentral = kLeft = kRight;
              start    = rightStart;
            } else {
              //Partition #3 is smaller than #2
              timeToLive 
                = doublePartition
                  ( vArray, outerStart, rightStart
                  , kRight, kRight, kRight
                  , stop, outerStop
                  , 0, timeToLive, outward);
              //Now... find kLeft in partition #2
              kCentral = kRight = kLeft;
              start   = middleStart;
              stop    = middleStop;
              outward = !outward;
            }
        }
        //We recursed for the smaller partition.
        //Did we run out of time?!
        if ( useSafetyNet && timeToLive < 0 ) {
          return timeToLive;
        }
        //We're only looking for one pivot now,
        //So no more telescoping stuff
      }
      m = stop - start;
      d = Math.sqrt(Math.log(m)); 
    }
    if (start+1<stop) {
      if (start<=kLeft && kLeft<stop) {
        int m = stop - start;
        timeToLive -= m*(m-1)/2;
        janitor.partitionRangeExactly
          ( vArray, start, stop, kLeft );
        start = kLeft + 1;
      }
      if (start<=kRight && kRight<stop) {
        int m = stop - start;
        timeToLive -= m*(m-1)/2;
        janitor.partitionRangeExactly
          ( vArray, start, stop, kRight );
        stop = kRight;
      }
    }
    return timeToLive;
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop 
    , int targetIndex) {
    int timeToLive  = getTimeToLive(stop-start);
    if (isFolding) {
      fold(vArray, start, stop);
      timeToLive -= (stop-start)/2;
    }
    int whatsLeft 
      = doublePartition 
        ( vArray, start, start
        , targetIndex, targetIndex, targetIndex
        , stop, stop, 0, timeToLive, true );
    if (whatsLeft<0 && useSafetyNet) {
      lastResort.partitionRangeExactly
      ( vArray, start, stop
      , targetIndex );
    }    
    int comparisonsDone = timeToLive - whatsLeft;
    comparisonCount += comparisonsDone; 
    //doesn't count the comparisons done by the 
    //safety net!
  }  
}

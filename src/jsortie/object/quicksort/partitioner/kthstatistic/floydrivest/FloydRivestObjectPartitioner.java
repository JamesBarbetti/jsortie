package jsortie.object.quicksort.partitioner.kthstatistic.floydrivest;

import java.util.Comparator;

import jsortie.helper.DumpRangeHelper;
import jsortie.object.quicksort.expander.PartitionObjectExpander;

public class FloydRivestObjectPartitioner<T>
  extends FloydRivestObjectPartitionerBase<T> {
  public FloydRivestObjectPartitioner() {
    super ();
  }
  public FloydRivestObjectPartitioner(double a) {
    super (a);
  }
  public FloydRivestObjectPartitioner
    ( PartitionObjectExpander<T> expanderOnLeft
    , PartitionObjectExpander<T> expanderOnRight) {
    super(expanderOnLeft, expanderOnRight);
  }
  public void setQuintary(boolean b) {
    quintary = b;
  }
  protected int doublePartition
    ( Comparator<? super T> comparator
    , T[] vArray, int outerStart, int start
    ,  int kLeft, int kCentral, int kRight 
    , int stop,   int outerStop 
    , double d,   int timeToLive
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
        if ( comparator.compare
              ( vArray[start-1], vArray[stop] ) == 0 ) {
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
      if (traced) {
        System.out.println
        ( "dp: In [" + start + ".." + (stop-1) + "]," 
        + " searching for " + kLeft+ " and " + kRight 
        + " around " + kCentral 
        + " with d=" + (Math.floor(d*100.0+.5) / 100.0)
        + "; m=" + m + "; c=" + c 
        + "; sample subrange will be [" + sampleStart 
        + ".." + (sampleStop-1) + "]");
      }
      timeToLive    -= collectSample
        ( comparator, vArray, start, stop
        , sampleStart, sampleStop);
      if ( useSafetyNet && timeToLive < 0 ) {
        return timeToLive;
      }
      int j1;
      int j2;
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
        j1      = ps.fixLowerSampleTarget
                  ( sampleStart, sampleStart+t1, sampleStart+t2
                  , sampleStart+a1, sampleStart+a2, sampleStop );
        j2      = ps.fixUpperSampleTarget
                  ( sampleStart, sampleStart+t1, sampleStart+t2
                  , sampleStart+a1, sampleStart+a2, sampleStop );
        if ( jCentre<j1 || j2<jCentre ) {
          jCentre = j1 + (j2-j1)/2;
        }
      } else {
        j1      = sampleStart;
        j2      = sampleStart+c-1;
        jCentre = sampleStart;
      }
      double d2 = (d==0) ? Math.sqrt(Math.log(m)) : d;
      timeToLive
        = doublePartition 
          ( comparator, vArray, sampleStart, sampleStart
          , j1, jCentre, j2, sampleStop,  sampleStop
          , d2, timeToLive, outward);
      if (traced) {
        System.out.println
        ( "dp: Three-way of [" + sampleStart + ".." + (sampleStop-1) + "]" 
        + " done, to get [" + j1 + "]=" + vArray[j1].toString()
        + " and [" + j2 + "]=" + vArray[j2].toString() 
        + " to partition [" + start + ".." + (stop-1) + "]");
        if (2<sampleStop-sampleStart && sampleStop-sampleStart<20) {
          DumpRangeHelper.dumpRange
            ( "dp: Sample was ", vArray, sampleStart, sampleStop );
        }
      }
      if ( useSafetyNet && timeToLive < 0 ) {
        return timeToLive;
      }
      int x = j2-j1-1; 
          //# items in middle partition in sample
      PartitionObjectExpander<T> lx 
        = outward ? rightExpander : leftExpander;
      PartitionObjectExpander<T> rx 
        =  outward ? leftExpander : rightExpander;
      int split1;
      int split2;
      if (j1==j2) {
        split1 
          = expandWithOnePivot 
            ( comparator, vArray
            , start, sampleStart, j1
            , sampleStop, stop
            , lx, rx);
        split2 = split1;
        if (traced) {
          System.out.println
          ( "dp: Expanded [" + sampleStart + ".." + (sampleStop-1) + "]"
          + " out to [" + start +  ".." + (stop-1) + "]"
          + " with one pivot, to get [" + split1+ "]=" + vArray[split1].toString());
          if (2<stop-start && stop-start<20) {
            DumpRangeHelper.dumpRange
            ( "dp: Range is now ", vArray, start, stop );
          }
        }
      } else if (sampleStop-j2<=j1-sampleStart) {
        //1st pivot closer to the sample median
        shifty.moveBackElementsToFront
        ( vArray, start, sampleStart, j1+1 ); 
        //sample v1 and to left, all the way left
        shifty.moveFrontElementsToBack
        ( vArray, j1+1, sampleStop, stop );
        //sample right of v1, all the way right
        int innerStart = j1+start-sampleStart;
        int innerStop  = j1+1+stop-sampleStop;        
        split1 = lx.expandPartition
                 ( comparator, vArray, innerStart, innerStart
                 , innerStart, innerStart+1, innerStop);
        timeToLive -= (innerStop-innerStart-1);
        if ( useSafetyNet && timeToLive < 0 ) {
          return timeToLive;
        }
        int shift  = stop - sampleStop;
        shifty.moveBackElementsToFront
          ( vArray, split1+1, j1 + 1 + shift
          , j2 + shift);
        if (beLazy && kRight<=innerStart) {
          split2 = split1;
        } else {
          innerStart = split1 + x + 1;
          innerStop  = j2 + stop - sampleStop + 1;
          split2 = rx.expandPartition
                 ( comparator, vArray, innerStart, innerStop-1
                 , innerStop-1, innerStop, innerStop);
          timeToLive -= (innerStop - innerStart -1);
        }
      } else {
        shifty.moveBackElementsToFront
          ( vArray, start,   sampleStart, j2); 
          //sample left of v2 all the way left
        shifty.moveFrontElementsToBack
          ( vArray, j2, sampleStop,  stop);
          //sample v2 and to right, all the way right
        int innerStart = j2 +     start - sampleStart;
        int innerStop  = j2 + 1 + stop  - sampleStop;
        split2 = rx.expandPartition
                 ( comparator, vArray, innerStart, innerStop-1
                 , innerStop-1, innerStop, innerStop);
        timeToLive -= (innerStop-innerStart-1);
        shifty.moveFrontElementsToBack
          ( vArray, j1 + 1 + start - sampleStart
          , j2 + start - sampleStart, split2); 
          //move sample middle right
        if (beLazy && innerStop<=kLeft) {
          split1 = split2;
        } else {
          innerStart = j1 + start - sampleStart;
          innerStop  = split2 - x;
          split1 = lx.expandPartition
                   ( comparator, vArray, innerStart, innerStart
                   , innerStart, innerStart+1, innerStop);
          timeToLive -= (innerStop-innerStart-1);
        }
      }
      int leftStop    = split1;
      int middleStart = split1+1;
      int middleStop  = split2;
      int rightStart  = split2+1;
      if (quintary) {
        T vLeft  = vArray[split1];
        T vRight = vArray[split2]; 
        if (kRight <= leftStop) {
          leftStop 
            = quinny.moveEqualOrGreaterToRight
              ( comparator, vArray
              , start, split1, vLeft);
        }
        if ( split1 < split2 ) {
          if ( comparator.compare 
               ( vLeft, vRight ) < 0 ) {
            if ( ( middleStart <= kRight && kRight < middleStop )
                 || ( middleStart <= kLeft && kLeft < middleStop ) ) {
              middleStart
                = quinny.moveEqualOrLessToLeft
                  ( comparator, vArray
                  , middleStart, middleStop, vLeft);
            }
            if ( ( middleStart <= kRight && kRight < middleStop )
                 || ( middleStart <= kLeft && kLeft < middleStop ) ) {
              middleStop
                = quinny.moveEqualOrGreaterToRight
                  ( comparator, vArray
                  , middleStart, middleStop, vRight);
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
              ( comparator, vArray
              , split2+1, stop, vRight);
        }
      }
      if (traced) {
        System.out.println
          ( "dp: In [" + start + ".." + (stop-1) + "]," 
          + " splits were at [" + split1 + "] and [" + split2 + "],"
          + " values were " + vArray[split1].toString() 
          + " and " + vArray[split2].toString());
        if (2<stop-start && stop-start<20) {
          DumpRangeHelper.dumpRange
            ( "dp: Range was ", vArray, start, stop );
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
        if (traced) {
          System.out.println
            ("dp: bumped up start; range is now"
            + " [" + start + ".." + (stop-1) + "],"
            + " k1=" + kLeft + ", k2=" + kRight);
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
        if (traced) {
          System.out.println
            ("dp: bumped down stop; range is now"
            + " [" + start + ".." + (stop-1) + "],"
            + " k1=" + kLeft + ", k2=" + kRight);
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
        if (traced) {
          System.out.println
          ( "dp: L=" + leftPartition + "=R, will now "
          + " work on [" + start + ".." + (stop-1) + "]"
          + " to find [" + kLeft + "]"
          + " and [" + kRight + "]");
        }
        outward = !outward;
      } else {
        //kLeft and kRight are in different partitions
        //Three cases: {1,2}, {1,3}, or {2,3}
        if (leftPartition==1) {
          if (rightPartition==2) {
            //{1,2}
            if (leftStop-start<middleStop-middleStart) {
              if (traced) {
                System.out.println( "dp: case 1<2");
              }
              //Partition #1 is smaller than #2
              timeToLive = doublePartition
                ( comparator, vArray
                , outerStart, start
                , kLeft, kLeft, kLeft
                , leftStop, outerStop
                , 0, timeToLive, outward);
              kCentral = kLeft = kRight;
              start   = middleStart;
              stop    = middleStop;
              outward = !outward;
            } else {
              if (traced) {
                System.out.println( "dp: case 2<1");
              }
              //Partition #2 is smaller than #1
              timeToLive = doublePartition
                ( comparator, vArray
                , outerStart, middleStart
                , kRight, kRight, kRight
                , middleStop, outerStop
                , 0, timeToLive, !outward);
              kCentral = kRight = kLeft;
              stop = leftStop;
            }
          } else {
            //{1,3}
            if (leftStop-start<stop-rightStart) {
              if (traced) {
                System.out.println
                  ( "dp: case 1<3, partition#1 is"
                  + " [" + start + ".." + (leftStop-1) + "]");
              }
              //Partition #1 is smaller than #3
              timeToLive = doublePartition
                ( comparator, vArray
                , outerStart, start
                , kLeft, kLeft, kLeft
                , leftStop, outerStop
                , 0, timeToLive, outward);
              //Now, find kRight in partition #3
              kCentral = kLeft = kRight;
              start    = rightStart;
            } else {
              if (traced) {
                System.out.println
                  ( "dp: case 3<1, partition#3 is "
                  + " [" + rightStart + ".." + (stop-1) + "]");
              }
              //Partition #3 is smaller than #1
              timeToLive = doublePartition
                ( comparator, vArray
                , outerStart, rightStart
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
              if (traced) {
                System.out.println( "dp: case 2<3");
              }
              //Partition #2 is smaller than #3
              timeToLive = doublePartition
                ( comparator, vArray
                , outerStart, middleStart
                , kLeft, kLeft, kLeft
                , middleStop, outerStop
                , 0, timeToLive, !outward);
              //Now... find kRight in partition #3
              kCentral = kLeft = kRight;
              start    = rightStart;
            } else {
              if (traced) {
                System.out.println
                  ( "dp: case 3<2, partition #3 is"
                  + " [" + (middleStart) + ".." + (middleStop-1) + "]");
              }
              //Partition #3 is smaller than #2
              timeToLive = doublePartition
                ( comparator, vArray
                , outerStart, rightStart
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
    int m = stop-start;
    if (1<m) {
      timeToLive -= m*(m-1)/2;
      janitor.partitionRangeExactly
        ( comparator, vArray, start, stop, kLeft );
      start = kLeft+1;
      if (start<=kRight) {
        m = stop - start;
        timeToLive -= m*(m-1)/2;
        janitor.partitionRangeExactly
          ( comparator, vArray, start, stop, kRight );
      }
    }
    return timeToLive;
  }
  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop 
    , int targetIndex) {
    if (traced) {
      System.out.println
        ( "\ndp: Finding [" + targetIndex + "]" );
    }
    int timeToLive  = getTimeToLive(stop-start);
    if (isFolding) {
      fold(comparator, vArray, start, stop);
      timeToLive -= (stop-start)/2;
    }
    int whatsLeft 
      = doublePartition 
      ( comparator, vArray
      , start, start
      , targetIndex, targetIndex, targetIndex
      , stop, stop
      , 0, timeToLive
      , true );
    if (whatsLeft<0 && useSafetyNet) {
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

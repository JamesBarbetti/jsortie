package jsortie.helper;

public class RotationHelper {
  public int gcd(int a, int b) {
    return (b==0) ? a : (b<a ? gcd(b,a%b) : gcd(a,b%a));
  }

	public void rotateRangeLeft(int[] vArray, int rangeStart
    , int rangeStop, int distance) {
    //
    //Assumes:
    // 1. [] vArray is not null
    // 2. count is not negative
    // 3. indices in the range start..(count-1) 
    //    are all valid indices into [] vArray
    //
		int count = rangeStop-rangeStart;
    if (distance<0) {
      rotateRangeRight(vArray, rangeStart, rangeStop, -distance);
    } else if (distance==1) {
      rotateRangeLeftByOne(vArray, rangeStart, rangeStop);
    } else if (count<2) {
      return;
    } else {
      distance = (count<=distance) 
                 ? (distance%count) 
                 : distance;
      if (distance!=0) {
        int g = gcd(count, distance);
        int v;
        for (int s=0;s<g;s++) {
          v = vArray[rangeStart + s];
          int i = s;
          int h = ((count<=s+distance)
                  ? (s+distance-count)
                  : (s+distance));
          while (h!=s) {
            vArray[rangeStart + i ] 
              = vArray[rangeStart + h];
            i=h;
            h+=distance;
            if (count<=h) h-=count;
          }
          vArray[rangeStart + i]=v;
        }
      }
    }
  }
  public void rotateRangeLeftByOne
    ( int[] vArray, int rangeStart, int rangeStop) {
    //
    //Assumes:
    // 1. [] vArray is not null
    // 2. count is not negative
    // 3. indices in the range start..(count-1) 
    //    are all valid indices into [] vArray
    //
  	int i=rangeStart+1;
    if (i<rangeStop) {
      int v    = vArray[rangeStart];
      for (; i<rangeStop; ++i) {
        vArray[i-1] = vArray[i];
      }
      vArray[rangeStart-1] = v;
    }
  }
  public void rotateRangeRight
    ( int[] vArray, int rangeStart
    , int rangeStop, int distance) {
    //
    //Assumes:
    // 1. [] vArray is not null
    // 2. count is not negative
    // 3. indices in the range start..(count-1)
    //    are all valid indices into [] vArray
    //
    int count = rangeStop-rangeStart;
    if (distance<0) {
      rotateRangeLeft ( vArray, rangeStart
                      , rangeStop, -distance);
    } else if (distance==1) {
      rotateRangeRightByOne
        ( vArray, rangeStart, rangeStop );
    } else if (distance<=0 || count<2) {
      return;
    } else {
      if (count<=distance) {
        distance=distance%count;
      }
      if (distance!=0) {
        int g = gcd(count, distance);
        int v;
        for (int s=0;s<g;s++) {
          v = vArray[rangeStart + s];
          int i=s;
          for (int  h=s-distance+count;h!=s;) {
            vArray[rangeStart + i]
              = vArray[rangeStart + h]; 
            i=h;
            h-=distance;
            if (h<0) h+=count;
          }
          vArray[rangeStart + i]=v;
        }
      }
    }
  }
  public void rotateRangeRightByOne(int[] vArray
    , int rangeStart, int rangeStop) {		
    //
    //Assumes:
    // 1. [] vArray is not null
    // 2. count is not negative
    // 3. indices in the range start..(count-1)
    //    are all valid indices into [] vArray
    //
    int i = rangeStop - 1;
    if (rangeStart<i) {
      int v = vArray[i];
      for (; i>rangeStart; --i) {
        vArray[i] = vArray[i-1];
      }
      vArray[rangeStart] = v;
    }
  }
}

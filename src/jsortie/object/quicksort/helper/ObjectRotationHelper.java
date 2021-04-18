package jsortie.object.quicksort.helper;

import jsortie.helper.RotationHelper;

public class ObjectRotationHelper<T> {
  protected RotationHelper integerRotationHelper = new RotationHelper();
	
  public void rotateRangeLeft(T[] vArray, int rangeStart, int rangeStop, int distance) {
  	int count = rangeStop - rangeStart;
    if (distance<0) {
      rotateRangeRight(vArray, rangeStart, rangeStop, -distance);
    } else if (distance==1) {
      rotateRangeLeftByOne(vArray, rangeStart, rangeStop);
    } else if (count<2) {
      return;
    } else {
      if (count<=distance) distance=distance%count;
      if (distance!=0) {
        int g = integerRotationHelper.gcd(count, distance);
        T   v;
        for (int s=0;s<g;s++) {
          v = vArray[rangeStart + s];
          int i=s;
          for (int h=((count<=s+distance) ? (s+distance-count) : (s+distance)) ;h!=s;) {
            vArray[rangeStart + i ]=vArray[rangeStart + h];
            i=h;
            h+=distance;
            if (count<=h) h-=count;
          }
          vArray[rangeStart + i]=v;
        }
      }
    }
  }
  private void rotateRangeLeftByOne(T[] vArray, int rangeStart, int rangeStop) {
  	int i = rangeStart+1;
    if (i<rangeStop) {
      T   v    = vArray[rangeStart];
      for (; i<rangeStop; ++i) {
        vArray[i-1]  = vArray[i];
      }
      vArray[rangeStop-1] = v;
    }
  }

  public void rotateRangeRight(T[] vArray, int rangeStart, int rangeStop, int distance) {
  	int count = rangeStop-rangeStart;
    if (distance<0) {
      rotateRangeLeft(vArray, rangeStart, rangeStop, -distance);
    } else if (distance==1) {
      rotateRangeRightByOne(vArray, rangeStart, rangeStop);
    } else if (distance<=0 || count<2) {
      return;
    } else {
      if (count<=distance) {
        distance=distance%count;
      }
      if (distance!=0) {
        int g = integerRotationHelper.gcd(count, distance);
        T   v;
        for (int s=0;s<g;s++) {
          v = vArray[rangeStart + s];
          int i=s;
          for (int  h=s-distance+count;h!=s;) {
            vArray[rangeStart + i] = vArray[rangeStart + h]; 
            i=h;
            h-=distance;
            if (h<0) h+=count;
          }
          vArray[rangeStart + i]=v;
        }
      }
    }
  }

  private void rotateRangeRightByOne(T[] vArray, int rangeStart, int rangeStop) {
  	int i = rangeStop-1;
    if (rangeStart<i) {
      T v = vArray[i];
      for (; i>rangeStart; --i) {
        vArray[i] = vArray[i-1];
      }
      vArray[rangeStart] = v;
    }
  }
}

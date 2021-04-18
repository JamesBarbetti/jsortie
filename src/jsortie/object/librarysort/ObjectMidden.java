package jsortie.object.librarysort;

import java.util.Comparator;

import jsortie.exception.SortingFailureException;

public class ObjectMidden<T> 
  extends ObjectBookshelf<T> {
  public ObjectMidden
    ( Comparator<? super T> comparator
    , int count, double ratio ) {
    super(comparator, count, ratio);
  }
  public void insert(T v, int prov) {
    int m = 0;
    if (0<count) {
      while ( m<currentCapacity 
              && vWorkArea[m]!=null ) {
        int bump = ( compare ( v,vWorkArea[m],prov
                             , provenance[m])<0) ? 1 : 2;
        m += m + bump;
      }
      if (currentCapacity<=m) {
        //some re-balancing and shuffling will be needed
        if ((m&1)==1) { 
          //left child, so v was less than parent of [m]
          m = insertOnLeft(v, prov, (m-1)/2);
        } else {
          m = insertOnRight(v, prov, (m-1)/2);
        }
      }
    }
    vWorkArea[m]  = v;
    provenance[m] = prov;
    ++count;
    if ((count&(count+1))==0) {
      redistribute();
    }
  }
  private int insertOnLeft
    ( T v, int prov, int i ) {
    int x;
    int h=pred(i); 
    for (; h!=-1; h=pred(h)) {
      if (vWorkArea[h] == null ) {
        //v belongs in this empty slot
        return h;
      } else if ( 0 < compare ( v, vWorkArea[h]
                              , prov, provenance[h])) {
        //v belongs in place slot h, but it is occupied; so:
        //find another slot, g, prior to h.
        int g=pred(h);
        for (;g!=-1;g=pred(g)) {
          if (vWorkArea[g] == null ) {
            //empty slot found, shuffle items
            //between g inclusive and h exclusive left by one
            x=g;
            i=h;
            do {
              h = succ(g);
              vWorkArea[g]  = vWorkArea[h];
              provenance[g] = provenance[h];
              g = h;
            } while (i!=g);
            climb(x);
            return i;
          }
        }
        int j; //there's no empty slot before h. 
        //find nearest empty slot after i; there will be one
        for ( j=succ(i)
            ; vWorkArea[j]!=null; j=succ(j));
        x=j;
        //move items to right so we can 
        //put v to the right of slot h
        h = succ(h);
        do {
          i = pred(j);
          vWorkArea[j]  = vWorkArea[i];
          provenance[j] = provenance[i];
          j = i;
        } while (j!=h);       
        climb(x);
        return i;
      }
    }
    //v compares less than everything else
    for (i=succ(i); vWorkArea[i]!=null; i=succ(i));
    x = i;
    for (int j=pred(i); j!=-1; i=j,j=pred(j)) {
      vWorkArea[i]  = vWorkArea[j];
      provenance[i] = provenance[j];
    }
    climb(x);
    return i;
  }
  private int insertOnRight(T v, int prov, int i) {
    int x;
    for (int j=succ(i); j!=-1; j=succ(j)) {
      if (vWorkArea[j] == null ) {
        //v belongs in this empty slot
        return j;
      } else if ( compare(v, vWorkArea[j], prov, provenance[j]) < 0 ) {
        //v belongs in place slot j, but it is occupied
        for (int k=succ(j);k!=-1;k=succ(k)) {
          if (vWorkArea[k] == null ) {
            //empty slot found, shuffle items
            //between j inclusive and k exclusive right by one
            x = k;
            i = j;
            do {
              j                = pred(k);
              vWorkArea  [ k ] = vWorkArea[j];
              provenance [ k ] = provenance[j];
              k                = j;
            } while (i!=k);
            climb(x);
            return i;
          }
        }
        int h; //there's no empty slot after j. 
        //find nearest empty slot before i    	
        for (h=pred(i); vWorkArea[h]!=null; h=pred(h));
        x = h;
        //move items back so we can put v in slot j
        j = pred(j);
        do {
          i             = succ(h);
          vWorkArea[h]  = vWorkArea[i];
          provenance[h] = provenance[i];
          h             = i;
        } while (i!=j);
        climb(x);
        return i;
      }
    }
    //v compares greater than everything else
    for (i=pred(i); vWorkArea[i]!=null; i=pred(i));
    x = i;
    for (int j=succ(i); j!=-1; i=j,j=succ(j)) {
      vWorkArea[i]  = vWorkArea[j];
      provenance[i] = provenance[j];
    }
    climb(x);
    return i;
  }
  private void climb(int m) {
    int parent = (m-1)/2;
    if ( 0<m && vWorkArea[parent] == null ) {
      T   v = vWorkArea[m];
      int p = provenance[m];
      do {
        vWorkArea[m]  = null;
        m = parent;
        parent = (m-1)/2;
      } while ( 0<m && vWorkArea[parent] == null );
      vWorkArea[m]  = v;
      provenance[m] = p;
    }
  }  
  //===================
  //Rebalancing methods
  //===================
  @Override
  protected void redistribute() {	  
    //Note: this is a super-lazy implementation 
    //that wastes a lot of memory. If we didn't 
    //*know* count to be 1 less than a power of 2 
    //this would all be rather more complicated 
    //(we'd need a different scratchCapacity).
    int   scratchCapacity = count;
    @SuppressWarnings("unchecked")
    T[]   vScratch        = (T[]) new Object [ scratchCapacity ];
    int[] iScratch        =       new int    [ scratchCapacity ];
    spreadOut( firstAtOrBelow(0), count, vScratch, iScratch, 0);
    for (int i=0; i<scratchCapacity; ++i) {
      vWorkArea[i]  = vScratch[i];
      provenance[i] = iScratch[i];
    }
    fractionalCapacity = (count+count+2) * capacityRatio - 1;
    currentCapacity = (int) Math.floor ( fractionalCapacity );
  }
  private int spreadOut 
    ( int r, int c, T[] vScratch  
    , int[] iScratch, int w) {
    while (0<c) {
      r      = spreadOut(r, c/2, vScratch, iScratch, w+w+1);
      c -= c/2;
      while (r!=-1 && vWorkArea[r]==null) {
        r = succ(r);
      }
      if (r==-1) {
        //uh-oh.  Something went missing!
        String message = "spreadOut failed, with w=" + w + ", c=" + c;
        throw new SortingFailureException(message);
      }      
      vScratch[w]  = vWorkArea[r];
      iScratch[w]  = provenance[r];
      vWorkArea[r] = null;
      --c;
      w += w + 2;
      r = succ(r);
    }
    return r;
  }
  //==============
  //Output methods
  //==============
  @Override
  public int emit(T[] vDestArray, int w) {
    return emitBelow(0, vDestArray, w);
  }
  private int emitBelow(int i, T[] vDestArray, int w) {
    while ( i < currentCapacity && vWorkArea[i] != null ) {
      int h = i;
      i = i+i+1;
      w = emitBelow( i, vDestArray, w ); //recurse on left
      vDestArray[w] = vWorkArea[h];
      ++w;
      ++i; //tail on right (i=h+h+2)
    }
    return w;
  }  
  //=====================================================================
  //Predecessor and successor functions (the downside of using a Midden!)  
  //=====================================================================
  protected int succ(int i) {
	if (i+i+2<currentCapacity) {
      return firstAtOrBelow(i+i+2); 
	}
    if ((i&1)==1) { /* left child */ 
      return ((i-1)/2);
    }
    do { /* while right child, go up */
      if (i==0) return (-1);
      i = (i-1) / 2;
    } while ((i&1)==0);
    return ((i-1) / 2);
  }
  protected int pred(int i) {
    if (i+i+1<currentCapacity) {
      return lastAtOrBelow(i+i+1);
    }
    if ((i&1)==0) { /* right child */
      return (i-1)/2; /* parent */
    }
    do { /* while left child, go up */
      i = (i-1) / 2;
    } while ((i&1)==1);
    if (i==0) {
      return -1;
    }
    return (i-1) / 2; 
  } 
  private int firstAtOrBelow(int i) {
    while (i+i+1 < currentCapacity) {
      i+=i+1;
    }
    return i;
  }
  private int lastAtOrBelow(int i) {
    while (i+i+2 < currentCapacity) {
      i+=i+2;
    }
    return i;
  }
}

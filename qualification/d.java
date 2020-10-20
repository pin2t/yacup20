import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.*;
import org.json.simple.*; 
import org.json.simple.parser.*; 

public class d {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        int n = scanner.nextInt(), m = scanner.nextInt();
        out.print("{\"offers\":[");
        int written = 0;
        JSONParser parser = new JSONParser();
        while (scanner.hasNext())
            System.out.println(scanner.nextLine());
        for (int l = 0; l < n; l++) {
            JSONObject feed = (JSONObject)parser.parse(scanner.nextLine());
            JSONArray offers = (JSONArray)feed.get("offers");
            for (Object offer: offers) {
                if (written > 0) out.print(",");
                written++;
                ((JSONObject)offer).writeJSONString(new PrintWriter(System.out));
                if (written == m) break;
            }
        }
        out.print("]}");
        out.close();
    }
}

class Scanner {
    private BufferedInputStream in;
 
    int c;
 
    boolean atBeginningOfLine;
 
    public Scanner(InputStream stream) {
       in = new BufferedInputStream(stream);
       try {
          atBeginningOfLine = true;
          c  = (char)in.read();
       } catch (IOException e) {
          c  = -1;
       }
    }
 
    public boolean hasNext() {
       if (!atBeginningOfLine) 
          throw new Error("hasNext only works "+
          "after a call to nextLine");
       return c != -1;
    }
 
    public String next() {
       StringBuffer sb = new StringBuffer();
       atBeginningOfLine = false;
       try {
          while (c <= ' ') {
             c = in.read();
          } 
          while (c > ' ') {
             sb.append((char)c);
             c = in.read();
          }
       } catch (IOException e) {
          c = -1;
          return "";
       }
       return sb.toString();
    }
 
    public String nextLine() {
       StringBuffer sb = new StringBuffer();
       atBeginningOfLine = true;
       try {
          while (c != '\n') {
             sb.append((char)c);
             c = in.read();
          }
          c = in.read();
       } catch (IOException e) {
          c = -1;
          return "";
       }
       return sb.toString();   
    }
 
    public int nextInt() {
       String s = next();
       try {
          return Integer.parseInt(s);
       } catch (NumberFormatException e) {
          return 0; //throw new Error("Malformed number " + s);
       }
    }
 
    public double nextDouble() {
       return new Double(next());
    }
 
    public long nextLong() {
       return Long.parseLong(next());
    } 
 
    public void useLocale(int l) {}
 }    

 class IntIntMap3 {
    private static final int FREE_KEY = 1000000001;
 
    public static final int NO_VALUE = 0;
 
    /** Keys */
    private long[] m_keys;
    /** Values */
    private long[] m_values;
 
    /** Do we have 'free' key in the map? */
    private boolean m_hasFreeKey;
    /** Value of 'free' key */
    private long m_freeValue;
 
    /** Fill factor, must be between (0 and 1) */
    private final float m_fillFactor;
    /** We will resize a map once it reaches this size */
    private int m_threshold;
    /** Current map size */
    private int m_size;
    /** Mask to calculate the original position */
    private int m_mask;
 
    public static long nextPowerOfTwo(long x) {
       if (x == 0)
          return 1;
       x--;
       x |= x >> 1;
       x |= x >> 2;
       x |= x >> 4;
       x |= x >> 8;
       x |= x >> 16;
       return (x | x >> 32) + 1;
    }
 
    public static int arraySize(final int expected, final float f) {
       final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / f)));
       if (s > (1 << 30))
          throw new IllegalArgumentException(
                "Too large (" + expected + " expected elements with load factor " + f + ")");
       return (int) s;
    }
 
    private static final int INT_PHI = 0x9E3779B9;
 
    public static int phiMix(final long x) {
       final long h = x * INT_PHI;
       return (int) (h ^ (h >> 16));
    }
 
    public IntIntMap3(final int size, final float fillFactor) {
       if (fillFactor <= 0 || fillFactor >= 1)
          throw new IllegalArgumentException("FillFactor must be in (0, 1)");
       if (size <= 0)
          throw new IllegalArgumentException("Size must be positive!");
       final int capacity = arraySize(size, fillFactor);
       m_mask = capacity - 1;
       m_fillFactor = fillFactor;
       this.alloc(capacity);
       m_threshold = (int) (capacity * fillFactor);
    }
 
    private void alloc(final int capacity) {
       this.m_keys = new long[capacity];
       this.m_values = new long[capacity];
       for (int i = 0; i < capacity; i++)
          this.m_keys[i] = FREE_KEY;
    }
 
    public long get(final long key) {
       if (key == FREE_KEY)
          return m_hasFreeKey ? m_freeValue : NO_VALUE;
 
       final int idx = getReadIndex(key);
       return idx != -1 ? m_values[idx] : NO_VALUE;
    }
 
    public long put(final long key, final long value) {
       if (key == FREE_KEY) {
          final long ret = m_freeValue;
          if (!m_hasFreeKey)
             ++m_size;
          m_hasFreeKey = true;
          m_freeValue = value;
          return ret;
       }
 
       int idx = getPutIndex(key);
       if (idx < 0) { // no insertion point? Should not happen...
          rehash(m_keys.length * 2);
          idx = getPutIndex(key);
       }
       final long prev = m_values[idx];
       if (m_keys[idx] != key) {
          m_keys[idx] = key;
          m_values[idx] = value;
          ++m_size;
          if (m_size >= m_threshold)
             rehash(m_keys.length * 2);
       } else // it means used cell with our key
       {
          assert m_keys[idx] == key;
          m_values[idx] = value;
       }
       return prev;
    }
 
    public long remove(final long key) {
       if (key == FREE_KEY) {
          if (!m_hasFreeKey)
             return NO_VALUE;
          m_hasFreeKey = false;
          final long ret = m_freeValue;
          m_freeValue = NO_VALUE;
          --m_size;
          return ret;
       }
 
       int idx = getReadIndex(key);
       if (idx == -1)
          return NO_VALUE;
 
       final long res = m_values[idx];
       m_values[idx] = NO_VALUE;
       shiftKeys(idx);
       --m_size;
       return res;
    }
 
    public int size() {
       return m_size;
    }
 
    public long[] values() {
       long[] values = new long[m_size];
       int i = 0;
       for (int j = m_keys.length; j-- > 0;) {
          if (m_keys[j] != FREE_KEY)
             values[i++] = m_values[j];
       }
       return values;
    }
 
    private void rehash(final int newCapacity) {
       m_threshold = (int) (newCapacity * m_fillFactor);
       m_mask = newCapacity - 1;
       final int oldCapacity = m_keys.length;
       final long[] oldKeys = m_keys;
       final long[] oldValues = m_values;
       this.alloc(newCapacity);
       m_size = m_hasFreeKey ? 1 : 0;
       for (int i = oldCapacity; i-- > 0;) {
          if (oldKeys[i] != FREE_KEY)
             put(oldKeys[i], oldValues[i]);
       }
    }
 
    private long shiftKeys(int pos) {
       // Shift entries with the same hash.
       int last, slot;
       long k;
       final long[] keys = this.m_keys;
       while (true) {
          last = pos;
          pos = getNextIndex(pos);
          while (true) {
             if ((k = keys[pos]) == FREE_KEY) {
                keys[last] = FREE_KEY;
                m_values[last] = NO_VALUE;
                return last;
             }
             slot = getStartIndex(k); // calculate the starting slot for the current key
             if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos)
                break;
             pos = getNextIndex(pos);
          }
          keys[last] = k;
          m_values[last] = m_values[pos];
       }
    }
 
    /**
     * Find key position in the map.
     * 
     * @param key Key to look for
     * @return Key position or -1 if not found
     */
    private int getReadIndex(final long key) {
       int idx = getStartIndex(key);
       if (m_keys[idx] == key) // we check FREE prior to this call
          return idx;
       if (m_keys[idx] == FREE_KEY) // end of chain already
          return -1;
       final int startIdx = idx;
       while ((idx = getNextIndex(idx)) != startIdx) {
          if (m_keys[idx] == FREE_KEY)
             return -1;
          if (m_keys[idx] == key)
             return idx;
       }
       return -1;
    }
 
    /**
     * Find an index of a cell which should be updated by 'put' operation. It can
     * be: 1) a cell with a given key 2) first free cell in the chain
     * 
     * @param key Key to look for
     * @return Index of a cell to be updated by a 'put' operation
     */
    private int getPutIndex(final long key) {
       final int readIdx = getReadIndex(key);
       if (readIdx >= 0)
          return readIdx;
       // key not found, find insertion point
       final int startIdx = getStartIndex(key);
       if (m_keys[startIdx] == FREE_KEY)
          return startIdx;
       int idx = startIdx;
       while (m_keys[idx] != FREE_KEY) {
          idx = getNextIndex(idx);
          if (idx == startIdx)
             return -1;
       }
       return idx;
    }
 
    private int getStartIndex(final long key) {
       return phiMix(key) & m_mask;
    }
 
    private int getNextIndex(final int currentIndex) {
       return (currentIndex + 1) & m_mask;
    }
 
    public void print() {
       System.out.print("[size:" + this.size());
       for (int i = 0; i < this.m_keys.length; i++) 
          if (this.m_keys[i] != FREE_KEY) 
             System.out.print(" " + this.m_keys[i] + ":" + this.m_values[i]);
       System.out.println("]");
    }
 }
 
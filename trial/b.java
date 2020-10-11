import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class b {
    public static void main(String[] args) throws Exception {
        b.Scanner scanner = new b.Scanner(System.in);
        long n = scanner.nextLong();
        long x = scanner.nextLong();
        long k = scanner.nextLong();
        UniqueAlarms unique = new UniqueAlarms(x, n);
        for (int i = 0; i < n; i++)
            unique.merge(scanner.nextLong());
        long times[] = unique.alarms();
         long r = 1_000_000_000L * 1_000_000_000L, l = 0;
         while (l <= r) {
            long m = (l + r) / 2;
            if (rings(times, m, x) < k)
               l = m + 1;
            else
               r = m - 1;
         }
         if (rings(times, l, x) == k)
            System.out.println(l);
         else if (rings(times, r + 1, x) == k)
            System.out.println(r + 1);
         else 
            System.out.println(r);
    }

    static long rings(long times[], long t, long x) {
      long r = 0;
      for (long tt: times) 
         if (tt <= t)
            r += Math.max((t - tt) / x, 0) + 1;
      return r;
   }
   
   static class UniqueAlarms {
      static final int BUCKETS_CNT = 100;
      private final Bucket[] buckets;
      private final long period;

      UniqueAlarms(long period, long alarmsCnt) {
         this.buckets = new Bucket[BUCKETS_CNT];
         for (int i = 0; i < BUCKETS_CNT; i++) 
            this.buckets[i] = new Bucket(alarmsCnt);
         this.period = period;
      }

      int size() {
         int c = 0;
         for (Bucket b: this.buckets) c += b.size();
         return c;
      }

      void merge(long val) {
         long r = val % this.period;
         this.buckets[(int)(r % (long)BUCKETS_CNT)].merge(r, val);
      }

      long[] alarms() {
         long[] alarms = new long[this.size()];
         int i = 0;
         for (Bucket b: this.buckets)
            i += b.copyValues(alarms, i);
         return alarms;   
      }
   }

   static class Bucket {
      private final long[] keys;
      private final long[] vals;
      private int n;

      Bucket(long capacity) {
         this.keys = new long[(int)capacity];
         this.vals = new long[(int)capacity];
         this.n = 0;
      }

      void merge(long key, long val) {
         int i = Arrays.binarySearch(this.keys, 0, this.n, key);
         if (i < 0) {
            int ii = -(i + 1); // insertion index
            System.arraycopy(this.keys, ii, this.keys, ii + 1, this.n - ii);
            System.arraycopy(this.vals, ii, this.vals, ii + 1, this.n - ii);
            this.keys[ii] = key;
            this.vals[ii] = val;
            this.n++;
         } else if (val < this.vals[i]) {
            this.keys[i] = key;
            this.vals[i] = val;
         }
      }

      int size() { return n; }

      long[] values() { 
         long[] values = new long[this.n];
         for (int i = 0; i < n; i++) values[i] = this.vals[i];
         return values; 
      }

      int copyValues(long[] to, int index) {
         for (int i = 0; i < this.n; i++) to[index + i] = this.vals[i];
         return this.n; 
      }
   }

    static class Scanner {
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
}
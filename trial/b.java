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
        Map<Long, Long> dedup = new HashMap<>();
        for (int i = 0; i < n; i++) {
            long t = scanner.nextLong();
            dedup.merge(t % x, t, (old, tt) -> { 
               if (tt < old) return tt; else return old;  
            });
        }    
        long times[] = new long[dedup.size()];
        int i = 0;
        for (Map.Entry<Long, Long> e: dedup.entrySet())
           times[i++] = e.getValue();
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
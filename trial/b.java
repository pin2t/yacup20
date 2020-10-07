import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class b {
    public static void main(String[] args) throws Exception {
        b.Scanner scanner = new b.Scanner(System.in);
        long n = scanner.nextLong();
        long x = scanner.nextLong();
        long k = scanner.nextLong();
        Alarm alarms[] = new Alarm[(int)n];
        for (int i = 0; i < n; i++)
            alarms[i] = new Alarm(scanner.nextLong());
        Arrays.sort(alarms);
        Alarm a = new Alarm(0);
        int cycle = 0, i = 0;
        while (i < k) {
            a = alarms[0];
            int j = 0;
            while (j < n && alarms[j].compareTo(a) == 0) {
                alarms[j].next(x, cycle);
                j++;
            }
            int shift = j;
            while (j < n && alarms[j - 1].compareTo(alarms[j]) > 0) {
                Alarm aa = alarms[j];
                System.arraycopy(alarms, j - shift, alarms, j - shift + 1, shift);
                alarms[j - shift] = aa;
                j++;
            }
            if (Alarm.isCycleComplete((int)n)) {
               long ncycles = k / (i + 1) - 1;
               if (ncycles > 0) {
                  cycle += ncycles;
                  for (Alarm alarm: alarms) alarm.multiply(ncycles, cycle);
                  i *= ncycles;
                  Alarm.cycled = 0;
                  a = alarms[0];
               } else 
                  i++;
            } else
               i++;
        }
        System.out.println(a.time);
    }

    static class Alarm implements Comparable<Alarm> {
        static int cycled = 0;
        long time;
        int cycle;
        
        Alarm(long t) {
            this.time = t;
            this.cycle = 0;
        }

        void next(long x, int c) {
            this.time += x;
            if (this.cycle != c)
               Alarm.cycled++;
            this.cycle = c; 
        }

        void multiply(long n, int c) {
            this.time *= n;
            this.cycle = c; 
        }

        static boolean isCycleComplete(int n) {
            return Alarm.cycled == n;
        }

        @Override
        public int compareTo(Alarm a) {
            return Long.compare(this.time, a.time);
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
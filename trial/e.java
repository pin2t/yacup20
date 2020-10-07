import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class e {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<HashSet<Integer>> clusters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int a = scanner.nextInt(), b = scanner.nextInt();
            boolean found = false;
            for (HashSet<Integer> cluster: clusters)
                if (cluster.contains(a) || cluster.contains(b)) {
                    found = true;
                    cluster.add(a); 
                    cluster.add(b);
                    break;
                }
            if (!found) {
                HashSet<Integer> c = new HashSet<>();
                c.add(a);
                c.add(b);
                clusters.add(c);
            }
        }
        int q = scanner.nextInt();
        for (int i = 0; i < q; i++) {
            List<Integer> result = new ArrayList<>();
            int x = scanner.nextInt(); 
            boolean found = false;
            HashSet<Integer> cluster = new HashSet<>();
            for (HashSet<Integer> c: clusters)
                if (c.contains(x)) {
                    cluster = c;
                    found = true;
                }
            int k = scanner.nextInt();
            for (int j = 0; j < k; j++) {
                int y = scanner.nextInt();
                if (found && cluster.contains(y))
                    result.add(y);
            }
            System.out.print(result.size());
            for (Integer s: result) System.out.print(" " + s);
            System.out.println();
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
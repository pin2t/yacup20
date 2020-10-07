import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class c {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new c.Scanner(System.in);
        int k = scanner.nextInt(), n = scanner.nextInt();
        int petya = 0, vasya = 0;
        String win = "";
        for (int i = 0; i < n; i++) {
            int val = scanner.nextInt();
            if (val % 15 != 0) {
                if (val % 3 == 0)
                    petya++;
                else if (val % 5 == 0)    
                    vasya++;
            }
            if (petya == k && win.isEmpty())
                win = "Petya";
            if (vasya == k && win.isEmpty())
                win = "Vasya";
        }
        if (win.isEmpty())
            win = vasya == petya ? "Draw" : (vasya > petya ? "Vasya" : "Petya");
        System.out.println(win);

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
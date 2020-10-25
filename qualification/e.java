import java.util.*;
import java.io.*;


public class e {
    static boolean important[];

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), k = scanner.nextInt();
        int s = scanner.nextInt();
        e.important = new boolean[k];
        String rows[][] = new String[n][k];
        for (int i = 0; i < k; i++) important[i] = false;
        for (int i = 0; i < s; i++)
            important[scanner.nextInt() - 1] = true;
        for (int i = 0; i < n; i++) 
            rows[i] = scanner.nextRow(k);
        int similar = 0;
        for (int i = 0; i < n - 1; i++)
            for (int j = i + 1; j < n; j++)
                if (e.similar(rows[i], rows[j]))
                    similar++;
        System.out.println(similar);
    }    

    static void printRow(String[] row, PrintStream to) {
        to.print('[');
        int i = 0;
        for (String s: row) {
            if (i++ > 0) to.print(',');
            to.printf("'%s'", s);
        }
        to.print(']');
    }

    /** function similar assumes both arrays are the same length  */
    static boolean similar(String[] row1, String[] row2) {
        boolean similar = false;
        boolean eq = true;
        for (int i = 0; i < row1.length; i++) {
            if (!row1[i].isEmpty() &&  row1[i].equals(row2[i]) 
                && (!e.important[i] || !row1[i].isEmpty() || !row2[i].isEmpty()))
                similar = true;
            if (!row1[i].isEmpty() && !row2[i].isEmpty() && !important[i] && 
                !row1[i].equals(row2[i])) eq = false;
        }
        return similar && eq;
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
          while (c == '\n' || c == '\r') 
              c = in.read();
          while (c != '\n' && c != -1) {
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
 
    public String[] nextRow(int columns) {
        String[] row = new String[columns];
        String s = this.nextLine();
        int col = 0, pos = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == (char)9) {
                row[col++] = pos == i ? "" : s.substring(pos, i);
                pos = i + 1;
                if (col >= row.length)
                    throw new RuntimeException("line '" + s + "' contains more than " + columns + " columns");
            } 
        }
        while (col < row.length)
            row[col++] = "";
        return row;
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

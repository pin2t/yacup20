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
        HashSet<Long> skus = new HashSet<>();
        outer:
        for (int l = 0; l < n; l++) {
            JSONObject feed = (JSONObject)parser.parse(scanner.nextLine());
            JSONArray offers = (JSONArray)feed.get("offers");
            for (Object o: offers) {
                JSONObject offer = (JSONObject)o;
                Long sku = (Long)offer.get("market_sku");
                if (!skus.contains(sku)) {
                    skus.add(sku);
                    if (written++ > 0) out.print(",");
                    offer.writeJSONString(out);
                    if (written == m) break outer;
                }
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

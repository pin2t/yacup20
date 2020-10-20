import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;
import java.io.*;

public class a {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String typ = scanner.next();
        LocalDate start = LocalDate.parse(scanner.next());
        LocalDate end = LocalDate.parse(scanner.next());
        ArrayList<Interval> intervals = new ArrayList<>();
        Interval i = new Interval(typ, start);
        while (!i.include(end)) {
            intervals.add(i);
            i = i.next();
        }
        intervals.add(i.adjust(end));
        System.out.println(intervals.size());
        for (Interval ii: intervals) {
            ii.print(System.out);
            System.out.println();
        }
    }
}

class Interval {
    String typ;
    LocalDate start, end;

    Interval(String t, LocalDate s) {
        this.typ = t;
        this.start = s;
        switch (t) {
            case "WEEK":
                this.end = s.with(ChronoField.DAY_OF_WEEK, 7);
                break;
            case "MONTH":
                this.end = s.withDayOfMonth(s.lengthOfMonth());
                break;
            case "QUARTER":
                this.end = s.withMonth((s.getMonthValue() / 3) * 3 + 3);
                this.end = this.end.withDayOfMonth(this.end.lengthOfMonth());
                break;
            case "YEAR":
                this.end = s.withDayOfYear(s.lengthOfYear());
                break;
            case "LAST_SUNDAY_OF_YEAR":
                this.end = s.withDayOfYear(s.lengthOfYear());
                while (this.end.getDayOfWeek() != DayOfWeek.SATURDAY) 
                    this.end = this.end.minusDays(1);
                break;
        }
    }

    private Interval(String t, LocalDate s, LocalDate e) {
        this.typ = t;
        this.start = s;
        this.end = e;
    }

    void print(PrintStream stream) {
        stream.print(this.start + " " + this.end);
    }

    Interval next() {
        LocalDate snew = this.end.plusDays(1), enew = this.end;
        switch (typ) {
            case "WEEK":
                enew = this.end.plusWeeks(1);
                break;
            case "MONTH": 
                enew = this.end.withDayOfMonth(1).plusMonths(1);
                enew = enew.withDayOfMonth(enew.lengthOfMonth());
                break;
            case "QUARTER": 
                enew = this.end.withDayOfMonth(1).plusMonths(3);
                enew = enew.withDayOfMonth(enew.lengthOfMonth());
                break;
            case "YEAR": 
                enew = this.end.withDayOfYear(1).plusYears(1);
                enew = enew.withDayOfYear(enew.lengthOfYear());
                break;
            case "LAST_SUNDAY_OF_YEAR":
                enew = this.end.plusYears(1);
                enew = enew.withDayOfYear(enew.lengthOfYear());
                while (enew.getDayOfWeek() != DayOfWeek.SATURDAY) enew = enew.minusDays(1);
                break;
        }
        return new Interval(this.typ, snew, enew);
    }

    boolean include(LocalDate d) {
        return (this.start.equals(d) || this.start.isBefore(d)) &&
               (this.end.equals(d) || this.end.isAfter(d));
    }

    Interval adjust(LocalDate e) {
        return new Interval(this.typ, this.start, e);
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

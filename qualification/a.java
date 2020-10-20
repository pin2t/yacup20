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
        switch (typ) {
            case "WEEK": {
                LocalDate n = start.with(ChronoField.DAY_OF_WEEK, 7);
                while (n.isBefore(end)) {
                    intervals.add(new Interval(start, n));
                    start = n.plusDays(1);
                    n = n.plusWeeks(1);
                }
                intervals.add(new Interval(start, end));
                break;
            }
            case "MONTH": {
                LocalDate n = start.withDayOfMonth(start.lengthOfMonth());
                while (n.isBefore(end)) {
                    intervals.add(new Interval(start, n));
                    start = n.plusDays(1);
                    n = n.withDayOfMonth(1).plusMonths(1);
                    n = n.withDayOfMonth(n.lengthOfMonth());
                }
                intervals.add(new Interval(start, end));
                break;
            }
            case "QUARTER": {
                LocalDate n = start.withMonth((start.getMonthValue() / 3) * 3 + 3);
                n = n.withDayOfMonth(n.lengthOfMonth());
                while (n.isBefore(end)) {
                    intervals.add(new Interval(start, n));
                    start = n.plusDays(1);
                    n = n.withDayOfMonth(1).plusMonths(3);
                    n = n.withDayOfMonth(n.lengthOfMonth());
                }
                intervals.add(new Interval(start, end));
                break;
            }
            case "YEAR": {
                LocalDate n = start.withDayOfYear(start.lengthOfYear());
                while (n.isBefore(end)) {
                    intervals.add(new Interval(start, n));
                    start = n.plusDays(1);
                    n = n.withDayOfYear(1).plusYears(1);
                    n = n.withDayOfYear(n.lengthOfYear());
                }
                intervals.add(new Interval(start, end));
                break;
            }
            case "LAST_SUNDAY_OF_YEAR":{
                LocalDate n = start.withDayOfYear(start.lengthOfYear());
                while (n.getDayOfWeek() != DayOfWeek.SATURDAY) n = n.minusDays(1);
                while (n.isBefore(end)) {
                    intervals.add(new Interval(start, n));
                    start = n.plusDays(1);
                    n = n.plusYears(1);
                    n = n.withDayOfYear(n.lengthOfYear());
                    while (n.getDayOfWeek() != DayOfWeek.SATURDAY) n = n.minusDays(1);
                }
                intervals.add(new Interval(start, end));
                break;
            }
        }
        System.out.println(intervals.size());
        for (Interval i: intervals) {
            i.print(System.out);
            System.out.println();
        }
    }

    static class Interval {
        LocalDate start, end;
        Interval(LocalDate s, LocalDate e) {
            this.start = s;
            this.end = e;
        }

        void print(PrintStream stream) {
            stream.print(this.start + " " + this.end);
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
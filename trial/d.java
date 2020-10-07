import java.io.BufferedReader;
import java.io.InputStreamReader;

public class d {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        int l = 1, r = n;
        while (l <= r) {
            int m = (l + r) / 2;
            System.out.println(m); System.out.flush();
            int answer = Integer.parseInt(reader.readLine());
            if (answer == 1)
                l = m + 1;
            else 
                r = m - 1;
        }
        System.out.printf("! %d\n", l); System.out.flush();
    }
}
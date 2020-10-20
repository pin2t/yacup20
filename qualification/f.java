import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

import org.json.simple.*;  

public class f {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String u = reader.readLine();
        int port  = Integer.parseInt(reader.readLine());
        long a = Integer.parseInt(reader.readLine());
        long b = Integer.parseInt(reader.readLine());
        URL url = new URL("http", u.substring(7), port, String.format("?a=%d&b=%d", a, b));
        URLConnection connection = url.openConnection();
        connection.connect();
        try (InputStream is = connection.getInputStream()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1)
                out.write(buffer, 0, length);
            JSONArray arr = (JSONArray)JSONValue.parse(out.toString("UTF-8"));
            int[] aa = new int[arr.size()];
            for (int i = 0; i < arr.size(); i++) 
                aa[i] = Integer.parseInt(arr[i].toString());
            Arrays.sort(aa);
            for (int i: aa)
                if (i > 0)
                    System.out.println(i);    
        }
    }
}

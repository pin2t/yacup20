import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

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
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            is.transferTo(buffer);
            JSONArray arr = (JSONArray)JSONValue.parse(new String(buffer.toByteArray()));
            long sum = 0;
            for (Object n: arr) sum += ((Long)n).longValue();
            System.out.println(sum);    
        }
    }
}

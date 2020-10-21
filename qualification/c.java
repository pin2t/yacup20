import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sun.net.httpserver.*;

public class c {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 7777);
        HttpServer server = HttpServer.create(addr, 1000);
        server.createContext("/ping", new PingHandler());
        server.createContext("/validatePhoneNumber", new PhoneNumberHandler());
        server.createContext("/shutdown", new ShutdownHandler(server));
        server.setExecutor(Executors.newWorkStealingPool());
        System.out.println("Listening on " + addr.getHostName() + ":" + addr.getPort());
        server.start();
    }
}

class QueryParameters extends HashMap<String, String> {
    /** @todo Add support for multiple values with the same key */
    QueryParameters(final String query) {
        if (query == null) return;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? this.decode(pair.substring(0, idx)) : pair;
            String value = idx > 0 && pair.length() > idx + 1
                    ? this.decode(pair.substring(idx + 1)) : null;
            if (!this.containsKey(key))
                this.put(key, value);
        }
    }

    private String decode(final String val) {
        try {
            return URLDecoder.decode(val, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

/** 
 * Helper class to wrap verbose HttpExchange methods with 
 * little convinient ones
 */
class Exchange {
    HttpExchange exchange;

    Exchange(HttpExchange e) {
        this.exchange = e;
    }

    void response(int code, String body) throws IOException {
        byte[] b = body.getBytes();
        this.exchange.sendResponseHeaders(code, b.length);
        try (OutputStream os = this.exchange.getResponseBody()) {
            if (b.length > 0)
                os.write(b);
        }
    }

    QueryParameters params() {
        return new QueryParameters(this.exchange.getRequestURI().getRawQuery());
    }

    void headerValue(String name, String val) {
        this.exchange.getResponseHeaders().add(name, val);
    }
}

/**
 * Helper class to wrap HttpExchange with local Exchange class
 */
class AppHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.handle(new Exchange(exchange));
    }

    void handle(Exchange exchange) throws IOException {
        exchange.response(404, "");
    }
}

class PingHandler extends AppHandler {
    @Override
    void handle(Exchange e) throws IOException {
        e.response(200, "");
    }
}

class ShutdownHandler extends AppHandler {
    final HttpServer srv;

    ShutdownHandler(HttpServer s) {
        this.srv = s;
    }

    @Override
    void handle(Exchange e) throws IOException {
        e.response(200, "");
        this.srv.stop(0);
    }
}

/** 
 * @todo fix ugly JSON generation by string concatenation
 */
class PhoneNumberHandler extends AppHandler {
    private static String statusFalse = "{\"status\": false}";

    @Override
    void handle(Exchange e) throws IOException {
        QueryParameters query = e.params();
        e.headerValue("Content-Type", "application/json");
        if (query.containsKey("phone_number")) {
            String pn = query.get("phone_number");
            StringBuilder builder = new StringBuilder();
            for (char c: pn.toCharArray()) 
                if (c >= '0' && c <= '9' || c == '+')
                    builder.append(c);
            String norm = builder.toString();
            if (norm.startsWith("+7"))
                norm = "8" + norm.substring(2);   
            if (norm.length() != 11 || (!norm.startsWith("8982") 
                && !norm.startsWith("8988") && !norm.startsWith("8912") 
                && !norm.startsWith("8934")))
                e.response(200, PhoneNumberHandler.statusFalse);
            else    
                e.response(200, String.join("\n", "{",
                    "\"normalized\": \"+7-" + norm.substring(1, 4) + "-" 
                        + norm.substring(4, 7) + "-" + norm.substring(7) + "\",",
                    "\"status\": true",
                  "}"));
        } else 
            e.response(400, "");
    }
}
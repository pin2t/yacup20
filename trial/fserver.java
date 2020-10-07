/**
* The MIT License (MIT)
*
* Copyright (c) 2020 Ilya Pokolev
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sun.net.httpserver.*;

public class fserver {
    public static void main(String[] args) throws IOException, InterruptedException {
        InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 7777);
        HttpServer server = HttpServer.create(addr, 1000);
        server.createContext("/", new AppHandler());
        server.setExecutor(Executors.newWorkStealingPool());
        System.out.println("Listen on " + addr.getHostName() + ":" + addr.getPort());
        server.start();
    }
}

class AppHandler implements HttpHandler {
    private final String result = String.join("\n", 
            "[",  
            "8,",  
            "6,",  
            "-2,",  
            "2,",  
            "    4,",  
            "    17,",  
            "    256,",  
            "    1024,",  
            "   -17,",  
            "    -19",  
            " ]    "    
            );
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.handle(new Exchange(exchange));
    }

    void handle(Exchange exchange) throws IOException {
        exchange.response(200, result);
    }

    /** 
     * Usefull wrapper around JDK HttpExchange with convinient methods for manipulation requests and responses
     */
    static class Exchange {
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
   }
}
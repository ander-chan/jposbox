/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.takepos.jposbox;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import static java.lang.System.out;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Windows10
 */
public class Web {

    String printername = "";
    HttpsServer server = null;

    public int StartServer(int port, String printer_name) {
        printername = printer_name;
        try {
            //server = HttpServer.create(new InetSocketAddress(port), 0);
            server = build(port);
        } catch (IOException ex) {
final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
              JOptionPane.showMessageDialog(null, ex.getMessage(), "Error",
        JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Web.class.getName()).log(Level.SEVERE, null, ex);
            return port;
        } catch (Exception ex) {
            Logger.getLogger(Web.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("Server started at port " + port);
        server.createContext("/hw_proxy/hello", new Hello());
        server.createContext("/hw_proxy/handshake", new Handshake());
        server.createContext("/hw_proxy/status_json", new StatusJson());
   
        //Dolibarr
       // server.createContext("/print", new PrintReceipt());
      
        server.createContext("/scale", new WeightScale());
         server.createContext("/print", new PrintReceipt());
        
        //End Dolibarr
        server.setExecutor(null); // creates a default executor
        server.start();
        //System.out.println(server.getHttpsConfigurator().getSSLContext().getProtocol());
        return 0;
    }

    public void StopServer() {
        // Stop server
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    private HttpsServer build(int port) throws Exception {

        // Set up the socket address
        InetSocketAddress address = new InetSocketAddress( port);
        System.out.println(address);
        // Initialise the HTTPS server
        HttpsServer httpsServer = HttpsServer.create(address, 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // Initialise the keystore
        char[] password = "josejose".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("cert/identifik.com.co.p12");
        ks.load(fis, password);

        // Set up the key manager factory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        // Set up the trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        // Set up the HTTPS context and parameters
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {

                // Initialise the SSL context
                SSLContext c ;
                try {
                    c = SSLContext.getDefault();
                    SSLEngine engine = c.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // Get the default parameters
                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);
                } catch (NoSuchAlgorithmException ex) {

                    Logger.getLogger(Web.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        return httpsServer;
    }

    static class Hello implements HttpHandler {

        @Override
        public void handle(HttpExchange t) throws IOException {
            out.println(t.getRequestURI());
            t.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            t.getResponseHeaders().set("Content-Type", "text/plain");
            t.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
            String response = "ping";
            t.sendResponseHeaders(200, response.length());
            try (OutputStream os = t.getResponseBody()) {
                os.write(response.getBytes());
            }
            out.println("Respponse: " + response);
        }
    }

    public class Handshake implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            // parse request
            out.println("/hw_proxy/handshake");
            he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            he.getResponseHeaders().set("Content-Type", "application/json");
            he.getResponseHeaders().set("Access-Control-Allow-Methods", "POST");
            he.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Debug-Mode");
            Map<String, Object> parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);

            // send response
            String response = "";
            for (String key : parameters.keySet()) {
                response += key + " = " + parameters.get(key) + "\n";
            }
            response = response.replace("{\"jsonrpc\":\"2.0\",\"method\":\"call\",\"params\":{},\"id\":", "");
            response = response.replace("} = null", "");
            response = "{\"jsonrpc\": \"2.0\", \"id\": " + response + ", \"result\": \"true\"}";
            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                out.print("Response::" + response);
                os.write(response.getBytes());
            }
        }
    }

    public class StatusJson implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            // parse request
            out.println("/hw_proxy/status_json");
            he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            he.getResponseHeaders().set("Content-Type", "application/json");
            he.getResponseHeaders().set("Access-Control-Allow-Methods", "POST");
            he.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Debug-Mode");
            Map<String, Object> parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
            // send response
            String response = "";
            for (String key : parameters.keySet()) {
                response += key + " = " + parameters.get(key) + "\n";
            }
            response = response.replace("{\"jsonrpc\":\"2.0\",\"method\":\"call\",\"params\":{},\"id\":", "");
            response = response.replace("} = null", "");
            response = "{\"jsonrpc\": \"2.0\", \"id\": " + response + ", \"result\": {\"scale\": {\"status\": \"disconnected\", \"messages\": [\"No RS-232 device found\"]}, \"scanner\": {\"status\": \"error\", \"messages\": [\"[Errno 2] No such file or directory: '/dev/input/by-id/'\"]}, \"escpos\": {\"status\": \"connected\", \"messages\": [\"Connected to TakePOS Printer\"]}}}";
            he.sendResponseHeaders(200, response.length());
            try (OutputStream os = he.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

   
    public Map<String, String> getParamMap(String query) {
        // query is null if not provided (e.g. localhost/path )
        // query is empty if '?' is supplied (e.g. localhost/path? )
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }

        return Stream.of(query.split("&"))
                .filter(s -> !s.isEmpty())
                .map(kv -> kv.split("=", 2))
                .collect(Collectors.toMap(x -> x[0], x -> x[1]));

    }
      public class PrintReceipt implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            try {
                Map<String, String> paramMap = getParamMap(he.getRequestURI().getQuery());
                out.println("(っ◕‿◕)っ Data received");
                he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                he.getResponseHeaders().set("Content-Type", "application/json");
                he.getResponseHeaders().set("Access-Control-Allow-Methods", "POST");
                he.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Debug-Mode");
                Map<String, Object> parameters = new HashMap<>();
                InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String text = br.readLine();
                if (text != null && text.equals("opendrawer")) {
                    PosPrinter P = new PosPrinter();
                   // P.html = false;
                    final byte[] openCD = {27, 112, 0, 100, 120};
                    String s = new String(openCD);
                    P.add(s);
                    P.print(PosBoxFrame.ComboPrinter1.getSelectedItem().toString(), 1, "7");
                }
                if (text != null) {
                    PosPrinter P = new PosPrinter();
                   // P.html = true;
                   // System.out.println(text=text.replace("invoice=",""));
                    text=text.replace("invoice=","");
                    System.out.print("🖨️💰💸");
                    System.out.println(" Imprimendo nueva factura");
                    // text=URLDecoder.decode(text, "UTF-8");
                    byte[] decoded = Base64.decodeBase64(text.getBytes());
                    //System.out.println(new String(decoded, "UTF-8") + "\n");
                    final String invoiceTxt = new String(decoded, "UTF-8");
                    System.out.println(invoiceTxt);
                    /*System.out.println("----------------------------");
                     final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
                    for (String line : invoiceTxt.split("\\r?\\n")) {
                        byte[] bytes = line.getBytes();
                        char[] hexChars = new char[bytes.length * 2];
                        for (int j = 0; j < bytes.length; j++) {
                            int v = bytes[j] & 0xFF;
                            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
                        }
                        System.out.println(hexChars);
                    }
                    */
                    P.P=invoiceTxt;
                    String printerName = PosBoxFrame.ComboPrinter1.getSelectedItem().toString();
                   if(paramMap != null && paramMap.get("printer") != null){
                    switch (paramMap.get("printer")) {
                         case "2":
                             printerName = PosBoxFrame.ComboPrinter2.getSelectedItem().toString();
                             break;
                         case "3":
                             printerName = PosBoxFrame.ComboPrinter3.getSelectedItem().toString();
                             break;
                         default:
                            printerName = PosBoxFrame.ComboPrinter1.getSelectedItem().toString();
                     }
                   }
                    P.print(printerName, 1, "7");
                }
                String response = "";
                he.sendResponseHeaders(200, response.length());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
      public class WeightScale implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            try {
                Map<String, String> paramMap = getParamMap(he.getRequestURI().getQuery());
                out.println("(っ◕‿◕)っ Data received");
                he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                he.getResponseHeaders().set("Content-Type", "application/json");
                he.getResponseHeaders().set("Access-Control-Allow-Methods", "POST");
                he.getResponseHeaders().set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Debug-Mode");
                Map<String, Object> parameters = new HashMap<>();
                InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String text = br.readLine();
                if (text != null && text.equals("opendrawer")) {
                  
                }
                if (text != null) {
                  
                }
                String response = "";
                he.sendResponseHeaders(200, response.length());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

 

    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }
                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}

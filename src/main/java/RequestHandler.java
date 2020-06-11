import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.*;

public class RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = splitQuery(httpExchange.getRequestURI().getRawQuery());
        String respText = null;

        if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
            String name = params.getOrDefault("name", null);
            String id = params.getOrDefault("id", null);

            try {
                respText = getProduct(id, name);
                if ( respText != null ) {
                    httpExchange.sendResponseHeaders(200, respText.getBytes().length);
                } else {
                    respText =  String.format("Product not found");
                    httpExchange.sendResponseHeaders(404, respText.getBytes().length);
                }
            } catch (IOException e) {
                respText =  String.format("Failed GET method: " + e.getMessage());
                httpExchange.sendResponseHeaders(400, respText.getBytes().length);
                e.printStackTrace();
            }

        } else if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {

            InputStream inputStream = httpExchange.getRequestBody();
            String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            try {
                createProduct(requestBody);
                respText =  String.format("Executed POST method");
                httpExchange.sendResponseHeaders(201, respText.getBytes().length);
            } catch (IOException e) {
                respText =  String.format("Failed POST method: " + e.getMessage());
                httpExchange.sendResponseHeaders(400, respText.getBytes().length);
                e.printStackTrace();
            }

        } else if (httpExchange.getRequestMethod().equalsIgnoreCase("PUT")) {

            InputStream inputStream = httpExchange.getRequestBody();
            String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));

            try {
                updateProduct(requestBody);
                respText =  String.format("Executed PUT method");
                httpExchange.sendResponseHeaders(201, respText.getBytes().length);
            } catch (IOException e) {
                respText =  String.format("Failed PUT method: " + e.getMessage());
                httpExchange.sendResponseHeaders(400, respText.getBytes().length);
                e.printStackTrace();
            }

        } else if (httpExchange.getRequestMethod().equalsIgnoreCase("DELETE")) {

            String id = params.getOrDefault("id", null);
            if (id != null ) {
                deleteProduct(id);
                respText =  String.format("Executed DELETE method");
                httpExchange.sendResponseHeaders(200, respText.getBytes().length);
            } else {
                respText =  String.format("Failed DELETE method: id is null");
                httpExchange.sendResponseHeaders(400, respText.getBytes().length);
            }


        } else {
            httpExchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
        }

        methodResponse(respText, httpExchange);
        httpExchange.close();
    }

    private Map<String, String> splitQuery(String query) {
        if (query == null || "".equals(query)) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(toMap(s -> decode(s[0]), s-> decode(s[1])));

    }

    private String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }

    private void createProduct(String jsonProductString) throws IOException {
        Product product = new ObjectMapper().readValue(jsonProductString, Product.class);
        ProductRepo.getInstance().addProduct(product);
    }

    private void updateProduct(String jsonProductString) throws IOException {
        Product product = new ObjectMapper().readValue(jsonProductString, Product.class);
        if ( product.getId() == null )
            throw new IOException("prouct Id is missing");
        ProductRepo.getInstance().updateProduct(product);
    }

    private String getProduct(String id, String name) throws IOException {
        List<Product> products = new ArrayList<Product>();
        if ( id != null ) {
            Product product = ProductRepo.getInstance().getProductById(id);
            if (product != null ){
                products.add(product);
            }
        } else if (name != null){
            products = ProductRepo.getInstance().getProductByName(name);
        } else {
            //get all
            products = ProductRepo.getInstance().getAll();
        }

        if (products.size() == 0 )
            return null;

        String s = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(products);
        return s;
    }

    private void deleteProduct(String id) {
        ProductRepo.getInstance().deleteProductById(id);
    }

    private void methodResponse(String response, HttpExchange httpExchange) throws IOException {
        OutputStream output = httpExchange.getResponseBody();
        output.write(response.getBytes());
        output.flush();
    }

}

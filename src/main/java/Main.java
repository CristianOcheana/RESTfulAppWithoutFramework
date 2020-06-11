import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static void main(String[] args){

        int serverPort = 8081;
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(serverPort), 50);
        } catch (IOException e) {
            e.printStackTrace();
        }
       server.createContext("/api/product", new RequestHandler()).setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return ( user.equals("user1") && pwd.equals("user1") ) ||
                        ( user.equals("user2") && pwd.equals("user2") ) ||
                        ( user.equals("user3") && pwd.equals("user3") );
            }
        });

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();

    }
}

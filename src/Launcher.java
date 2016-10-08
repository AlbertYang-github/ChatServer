import java.io.IOException;

/**
 * Created by Yohann on 2016/10/8.
 */
public class Launcher {
    public static void main(String[] args) throws IOException {
        ConnPool connPool = new ConnPool();
        try {
            new ServerThread(20000, connPool).start();
            new HeartThread(connPool).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

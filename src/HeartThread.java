import java.io.IOException;

/**
 * Created by Yohann on 2016/10/8.
 */
public class HeartThread extends Thread {

    private ConnPool connPool;

    public HeartThread(ConnPool connPool) {
        this.connPool = connPool;
    }
    @Override
    public void run() {
        try {
            connPool.startHeart();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

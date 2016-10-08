import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Yohann on 2016/10/8.
 */
public class ConnPool {
    //保存需要维护的通道
    private Set<SocketChannel> channelSet;
    private Iterator<SocketChannel> ite;

    public ConnPool() {
        channelSet = new HashSet<>();
    }

    /**
     * 添加套接字通道
     *
     * @param sc
     */
    public void add(SocketChannel sc) {
        channelSet.add(sc);
    }

    /**
     * 发送心跳包
     */
    public void startHeart() throws IOException, InterruptedException {
        ByteBuffer buf = ByteBuffer.allocate(10);
        while (true) {
            ite = channelSet.iterator();
            while (ite.hasNext()) {
                System.out.println("集合中有连接");
                SocketChannel sc = ite.next();
                buf.put("heart".getBytes());
                buf.flip();
                sc.write(buf);
                buf.clear();
            }
            //发送心跳包间隔为5s
            Thread.sleep(5 * 1000);
            System.out.println("发送心跳包");
        }
    }
}

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Yohann on 2016/9/16.
 */
public class ServerThread extends Thread {
    private final ConnPool connPool;
    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public ServerThread(int port, ConnPool connPool) throws IOException {
        this.port = port;
        //创建serverSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        //创建多路复用器
        selector = Selector.open();
        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //注册到多路复用器
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //创建连接池
        this.connPool = connPool;
    }

    @Override
    public void run() {
        System.out.println("服务端启动成功...");
        //轮询注册在多路复用器上的通道事件
        while (true) {
            try {
                //当注册事件就绪时就返回，否则一直阻塞
                selector.select();
                //迭代selector就绪事件
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    //获取就绪通道对应的key
                    SelectionKey key = it.next();
                    //处理新接入的请求
                    if (key.isAcceptable()) {
                        //将socketChannel放入连接池中 (统一管理)
                        System.out.println("处理连接请求");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        connPool.add(socketChannel);
                    }
                    //处理读事件
                    if (key.isReadable()) {
                        //解析消息，对消息进行转发
                    }
                    //删除已处理的key
                    it.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

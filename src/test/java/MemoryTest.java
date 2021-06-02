import com.monk.genericaccess.manager.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MemoryTest {

    private static final BlockingQueue<Connection> queue = new ArrayBlockingQueue<>(15);
    private static final List<Connection> total = new ArrayList<>();

    static {
        for (int i=0;i<10;i++) {
            try{
                Connection conn = ConnectionFactory.createConnection();
                assert conn != null;
                queue.put(conn);
                total.add(conn);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test
    void alloc() throws InterruptedException {
        List<Connection> list = new ArrayList<>();


        for (int i=0;i<8;i++) {
            list.add(queue.take());
        }

        total.forEach(c -> {
            try {
                c.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        list.forEach(c -> {
            try {
                System.out.println(c.isClosed());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }
}

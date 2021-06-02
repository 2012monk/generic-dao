import com.monk.genericaccess.manager.ConnectionManager;
import com.monk.genericaccess.manager.ConnectionManagerFactory;
import com.monk.genericaccess.manager.ConnectionManagerImpl;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class ConnectionTest {

    ConnectionManager manager = ConnectionManagerFactory.getManager();
    @Test
    void connectionTest() throws SQLException {
        Connection conn = manager.getConn();
        assert conn!=null;
        ResultSet rs = conn.prepareStatement("SELECT 1 FROM dual").executeQuery();
        rs.next();
        assert 1 == rs.getInt(1);
        conn.close();
    }
}
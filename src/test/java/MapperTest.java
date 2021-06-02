import com.monk.genericaccess.demo.User;
import com.monk.genericaccess.mapper.SqlMapper;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

public class MapperTest {

    SqlMapper s = new SqlMapper();

    @Test
    void insertTest() {
        User user = new User();
        user.setUserId("123");
        user.setUserPw("3321");

        String tbName = "슈123";
        StringJoiner columns = new StringJoiner(",", "(", ")");
        StringJoiner values = new StringJoiner(",", "(",")");

        columns.add("123");
        values.add("123").add("1234");

        String sqlPre = "INSERT INTO {table} {columns} VALUES {values}";
        String sql = sqlPre.replace("{table}", tbName)
                .replace("{columns}", columns.toString())
                .replace("{values}", values.toString());


        System.out.println(sql);
    }
}

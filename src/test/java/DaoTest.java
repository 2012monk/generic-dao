import com.monk.genericaccess.GenericAccess;
import com.monk.genericaccess.GenericAccessFactory;
import com.monk.genericaccess.demo.UserService;
import com.monk.genericaccess.demo.User;
import com.monk.genericaccess.exception.IllegalAnnotationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

// ALL test passed!!

// 통과!

public class DaoTest {
    private final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(DaoTest.class);
    private final GenericAccess<User> dao = GenericAccessFactory.getInstance(User.class);
    private final UserService service = new UserService();
    private User user;

    @Test
    void order() throws IllegalAnnotationException {
        List<User> users = service.searchUserOrder("1", "userId");
        log.debug("users {}", users);
        assert users != null;
    }

    @Test
    void search() throws IllegalAnnotationException {
        List<User> users = service.searchId("t", "how");
        log.debug("users {}", users);
    }

    @Test
    void name() throws IllegalAnnotationException {
        log.debug("search with pid {}",service.searchPidLimit("1") );
    }

    @Test
    void filterTest() throws IllegalAnnotationException {
        log.debug("filter {}", service.getByPid("12"));
    }

    @BeforeEach
    @Test
    void insert() throws SQLException, IllegalAnnotationException {
        if (user == null) {
            user = new User();
        }
        Random r = new Random();
        StringBuilder id = new StringBuilder();

        for (int i=0;i<4;i++){
            id.append(r.nextInt(10));
        }
        user.setUserId(id.toString());
        user.setUserPw("345");

        assert user.getUserId().equals(dao.insert(user).getUserId());
    }

    @Test
    void update() throws SQLException, IllegalAnnotationException {
        assert user != null;
        user.setUserPw("ladskfjf");
        User u = dao.update(user);
        log.debug("{}", u);
        assert u != null;
    }


    @Test
    void delete() throws SQLException, IllegalAnnotationException {
        user.setUserPw("90903339");
        assert dao.delete(user);
    }


    @Test
    void deleteByKey() throws SQLException, IllegalAnnotationException {
        assert dao.deleteById(user.getUserId());
    }

    @Test
    void fields() throws InstantiationException, IllegalAccessException {
        HashMap<Class<?>, HashMap<String, Object>> cache = new HashMap<>();

        Field[] fields = User.class.getDeclaredFields();
        User out = User.class.newInstance();

        HashMap<String, Object> props = new HashMap<>();
        props.put("fields", fields);

        cache.put(User.class, props);

        for (Field f: fields) {
            f.setAccessible(true);
            f.set(out, "123");
        }

        log.debug("{}", out);
    }

    @Test
    void listTest() {
        log.debug("{}d", dao.selectAll());

    }

    @Test
    void getTest() throws IllegalAnnotationException {
        User user = dao.select("test1");
        log.debug("{}", user);
        assert user != null;
    }
}

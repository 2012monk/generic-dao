import org.junit.jupiter.api.Test;

import javax.lang.model.type.ReferenceType;

import static org.junit.jupiter.api.Assertions.*;

class RefTypeTest {


    @Test
    void isStringRefType() {
        System.out.println(ReferenceType.class.isAssignableFrom(String.class));
    }
}
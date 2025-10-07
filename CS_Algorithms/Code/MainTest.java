package Code;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    public void testClassExists() {
        Main t = new Main();
        assertNotNull(t);
    }
}

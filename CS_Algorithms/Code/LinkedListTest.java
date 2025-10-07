package Code;
import org.junit.Test;
import static org.junit.Assert.*;

public class LinkedListTest {
    @Test
    public void Size_OnEmptyList_IsZero() {
        LinkedList list = new LinkedList();
        assertEquals(0, list.size());
    }

    @Test
    public void Size_AfterAddingOne_IsOne() {
        LinkedList list = new LinkedList();
        list.Add(42);
        assertEquals(1, list.size());
    }

    @Test
    public void Find_OnEmpty_IsNull() {
        LinkedList list = new LinkedList();
        assertNull(list.Find(42));
    }

    @Test
    public void Find_AfterAdd_ReturnsNode() {
        LinkedList list = new LinkedList();
        list.Add(42);
        LinkedList.Node result = list.Find(42);
        assertNotNull(result);
        assertEquals(42, result.Value);
    }
}

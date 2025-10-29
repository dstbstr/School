import org.junit.Test;
import static org.junit.Assert.*;

public class RbtTests {
    @Test
    public void IsValid_OnEmpty_ReturnsTrue() {
        Rbt tree = new Rbt();
        assertTrue(tree.IsValidRbt());
    }

    @Test
    public void IsValid_WithJustRoot_ReturnsTrue() {
        Rbt tree = new Rbt();
        tree.Insert(42);
        assertTrue(tree.IsValidRbt());
    }

    @Test
    public void IsValid_WithSeveralInserts_ReturnsTrue() {
        Rbt tree = new Rbt();
        tree.Insert(1, 4, 2, 5, 11, 7, 14, 8, 15);
        assertTrue(tree.IsValidRbt());
    }

    @Test
    public void Find_AfterInsert_ReturnsNode() {
        Rbt tree = new Rbt();
        tree.Insert(42);

        Rbt.Node result = tree.Find(42);
        assertNotNull(result);
        assertEquals(42, result.Value);
    }

    @Test
    public void Find_AfterInsertThenDelete_ReturnsNull() {
        Rbt tree = new Rbt();
        tree.Insert(42);
        tree.Delete(42);

        Rbt.Node result = tree.Find(42);
        assertNull(result);
    }

    @Test
    public void IsValid_BigTreeWithOneDelete_ReturnsTrue() {
        Rbt tree = new Rbt();
        int[] vals = new int[] {1, 4, 2, 5, 11, 7, 14, 8, 15};
        tree.Insert(vals);

        for(int val : vals) {
            tree.Delete(val);
            assertTrue(tree.IsValidRbt());
            // Rbt other = tree.Clone();
            // other.Delete(val);
            // assertTrue(other.IsValidRbt());
        }
    }
}

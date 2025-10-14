import org.junit.Test;
import static org.junit.Assert.*;

public class BstTest {
    @Test
    public void Find_OnEmpty_ReturnsNull() {
        Bst tree = new Bst();
        assertNull(tree.Find(42));
    }

    @Test
    public void Find_AfterInsert_ReturnsNode() {
        Bst tree = new Bst();
        tree.Insert(42);

        Bst.Node result = tree.Find(42);
        assertNotNull(result);
        assertEquals(42, result.Value);
    }

    @Test
    public void Order_AfterToDll_IsInOrder() {
        Bst tree = new Bst();
        tree.Insert(6);
        tree.Insert(3);
        tree.Insert(7);
        tree.Insert(2);
        tree.Insert(5);
        tree.Insert(8);

        Bst.Node node = tree.ToDoubleLinkedList();
        assertNull(node.Left);
        while(node != null) {
            if(node.Left != null) {
                assertTrue(node.Left.Value <= node.Value);
            }
            if(node.Right != null) {
                assertTrue(node.Value <= node.Right.Value);
            }
            node = node.Right;
        }
    }
}

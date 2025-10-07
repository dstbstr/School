package Code;
import org.junit.Test;
import static org.junit.Assert.*;

public class SortsTest {
    @Test
    public void InsertionSort_WithNoElements_DoesNothing() {
        Integer[] array = new Integer[0];
        Sorts.Insert(array);
        assertArrayEquals(array, new Integer[0]);
    }
    
    @Test
    public void InsertSort_WithOneElement_DoesNothing() {
        Integer[] array = new Integer[]{42};
        Sorts.Insert(array);
        assertArrayEquals(array, new Integer[]{42});
    }

    @Test
    public void InsertSort_WithReverseOrder_SortsArray() {
        Integer[] array = new Integer[]{5, 4, 3, 2, 1};
        Sorts.Insert(array);
        assertArrayEquals(array, new Integer[]{1, 2, 3, 4, 5});
    }

    @Test
    public void InsertSort_WithDuplicates_SortsArray() {
        Integer[] array = new Integer[]{3, 1, 2, 3, 1};
        Sorts.Insert(array);
        assertArrayEquals(array, new Integer[]{1, 1, 2, 3, 3});
    }

    
    @Test
    public void MergeSort_WithNoElements_DoesNothing() {
        Integer[] array = new Integer[0];
        Sorts.Merge(array);
        assertArrayEquals(array, new Integer[0]);
    }
    @Test
    public void MergeSort_WithOneElement_DoesNothing() {
        Integer[] array = new Integer[]{42};
        Sorts.Merge(array);
        assertArrayEquals(array, new Integer[]{42});
    }
    @Test
    public void MergeSort_WithReverseOrder_SortsArray() {
        Integer[] array = new Integer[]{5, 4, 3, 2, 1};
        Sorts.Merge(array);
        assertArrayEquals(array, new Integer[]{1, 2, 3, 4, 5});
    }
    @Test
    public void MergeSort_WithDuplicates_SortsArray() {
        Integer[] array = new Integer[]{3, 1, 2, 3, 1};
        Sorts.Merge(array);
        assertArrayEquals(array, new Integer[]{1, 1, 2, 3, 3});
    }

    @Test
    public void WideMergeSort_WithNoElements_DoesNothing() {
        Integer[] array = new Integer[0];
        Sorts.WideMerge(array);
        assertArrayEquals(array, new Integer[0]);
    }
    @Test
    public void WideMergeSort_WithOneElement_DoesNothing() {
        Integer[] array = new Integer[]{42};
        Sorts.WideMerge(array);
        assertArrayEquals(array, new Integer[]{42});
    }
    @Test
    public void WideMergeSort_WithReverseOrder_SortsArray() {
        Integer[] array = new Integer[]{5, 4, 3, 2, 1};
        Sorts.WideMerge(array);
        assertArrayEquals(array, new Integer[]{1, 2, 3, 4, 5});
    }
    @Test
    public void WideMergeSort_WithDuplicates_SortsArray() {
        Integer[] array = new Integer[]{3, 1, 2, 3, 1};
        Sorts.WideMerge(array);
        assertArrayEquals(array, new Integer[]{1, 1, 2, 3, 3});
    }

}

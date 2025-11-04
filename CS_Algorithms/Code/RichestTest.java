import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;

public class RichestTest {
    @Test
    public void ToSortedArray_AfterMakeHeap_ReturnsSortedElements() {
        Richest.MinHeap heap = new Richest.MinHeap(5);
        heap.add(2);
        heap.add(11);
        heap.add(3);
        heap.add(7);
        heap.add(5);
        heap.makeHeap();

        int[] expected  = new int[]{11, 7, 5, 3, 2};
        int[] actual = heap.toSortedArray().mapToInt(i -> i).toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void Solve_WithLessThanMaxElements_ReturnsElementsInDescendingOrder() {
        String input = """
        5
        3
        8
        1
        4
        """;

        Stream<String> lines = Arrays.stream(input.split("\n"));
        Stream<Integer> result = Richest.Solve(lines, 10);
        //int[] expected = new int[]{1, 3, 4, 5, 8};
        int[] expected = new int[]{8, 5, 4, 3, 1};
        int[] actual = result.mapToInt(i -> i).toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void Solve_WithMoreThanMaxElements_ReturnsTopElementsInDescendingOrder() {
        String input = """
                5
                3
                11
                13
                8
                16
                44
                1
                21
                """;
        Stream<String> lines = Arrays.stream(input.split("\n"));
        Stream<Integer> result = Richest.Solve(lines, 5);
        int[] expected = new int[]{44, 21, 16, 13, 11};
        int[] actual = result.mapToInt(i -> i).toArray();
        assertArrayEquals(expected, actual);
    }
}

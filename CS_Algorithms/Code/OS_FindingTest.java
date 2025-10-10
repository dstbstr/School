import org.junit.Test;
import static org.junit.Assert.*;

public class OS_FindingTest {
    private static final int[] EXAMPLE_ARRAY = new int[]{4, 8, 4, 3, 6, 9, 2};
    @Test
    public void Solve_ExampleWith1_Returns2() {
        int target = 1;
        OS_Finding.Solution solution = new OS_Finding.Solution(target, EXAMPLE_ARRAY);
        assertEquals("2", solution.Solve());
    }

    @Test
    public void Solve_ExampleWith5_Returns6() {
        int target = 5;
        OS_Finding.Solution solution = new OS_Finding.Solution(target, EXAMPLE_ARRAY);
        assertEquals("6", solution.Solve());
    }

    @Test
    public void Solve_ExampleWith9_ReturnsNull() {
        int target = 9;
        OS_Finding.Solution solution = new OS_Finding.Solution(target, EXAMPLE_ARRAY);
        assertNull(solution.Solve());
    }
}

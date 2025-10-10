import org.junit.Test;
import static org.junit.Assert.*;

public class OS_FindingTest {
    //2,3,4,4,6,8,9
    private static final int[] EXAMPLE_ARRAY = new int[]{4, 8, 4, 3, 6, 9, 2};
    @Test
    public void Solve_ExampleWith1_Returns2() {
        int target = 1;
        String solution = OS_Finding.Solve(EXAMPLE_ARRAY, target);
        assertEquals("2", solution);
    }

    @Test
    public void Solve_ExampleWith5_Returns6() {
        int target = 5;
        String solution = OS_Finding.Solve(EXAMPLE_ARRAY, target);
        assertEquals("6", solution);
    }

    @Test
    public void Solve_ExampleWith0_ReturnsNull() {
        int target = 0;
        String solution = OS_Finding.Solve(EXAMPLE_ARRAY, target);
        assertEquals("null", solution);
    }

    @Test
    public void Solve_ExampleWith9_ReturnsNull() {
        int target = 9;
        String solution = OS_Finding.Solve(EXAMPLE_ARRAY, target);
        assertEquals("null", solution);
    }
    @Test
    public void Solve_WithExample7_Returns9() {
        int target = 7;
        String solution = OS_Finding.Solve(EXAMPLE_ARRAY, target);
        assertEquals("9", solution);
    }
}

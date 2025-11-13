import org.junit.Test;
import static org.junit.Assert.*;

public class FastMatrixMultiTest {
    @Test
    public void Solve_SampleInput_ReturnsExpectedOutput() {
        long[] arr = new long[]{10, 100, 5, 50};
        FastMatrixMulti.Result result = FastMatrixMulti.Solve(arr);
        assertEquals("((A1A2)A3)", result.str);
        assertEquals(7500L, result.cost);
    }

    @Test
    public void Solve_SimpleInput() {
        long[] arr = new long[]{3, 5, 7, 9};
        FastMatrixMulti.Result result = FastMatrixMulti.Solve(arr);
        assertEquals("((A1A2)A3)", result.str);
        assertEquals(294L, result.cost);
    }

    @Test
    public void Solve_LongInput() {
        long[] arr = new long[]{3, 7, 13, 12, 6,10, 8, 9, 12,5,9,3,7,13,13,12,12,1,9,10,3,5,10,8,8,15,9,4,8,6,7,2,14,4,10,1,1,7,2,16,14,7,1,16,3,3,9,13,16,8,7,2,12,14,16,5,12,7,3,16,2,15,15,8,1,3,11,7,6,6,1,8,10,11,5,8,8,5,1,12,14,10,8,2,8,4,7,9,11,1,1,5,7,7,12,13,12,5,14,12,13,11,9,5,3,1,14,16,15,6,14,6,11,7,11,7,16,15,1,16,2,14,2,10,9,8,15,7, 15};
        FastMatrixMulti.Result result = FastMatrixMulti.Solve(arr);
        assertTrue(result.str.length() > 0);
        assertTrue(result.cost > 0);
    }
}

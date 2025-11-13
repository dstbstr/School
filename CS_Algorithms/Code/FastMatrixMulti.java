// // Dustin Randall
import java.io.*;
import java.nio.file.*;

// Given a sequence of matrices, find the optimal order to multiply them
public class FastMatrixMulti {

    // record to hold the parenthesized string, and the total cost
    public static class Result {
        public final String str;
        public final long cost;

        public Result(String str, long cost) {
            this.str = str;
            this.cost = cost;
        }
    }
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument, but received " + args.length);
        }

        Path path = Paths.get(args[0]);
        // read all lines as longs to avoid overflowing an int in multiplication
        long[] arr = Files.lines(path).mapToLong(Long::parseLong).toArray();

        Result result = Solve(arr);
        System.out.println(result.str + "\n" + result.cost);
    }

    // Separate method for testability
    public static Result Solve(long[] arr) {
        // cache[i][j] holds the minimum cost of multiplying matrices from i to j
        long[][] cache = new long[arr.length + 1][arr.length + 1];
        // next[i][j] holds the index at which to split the product for optimal cost
        int[][] next = new int[arr.length + 1][arr.length + 1];

        // initialize cache
        for(int i = 1; i <= arr.length; i++) {
            cache[i][i] = 0;
        }

        for(int len = 2; len <= arr.length; len++) {
            for(int start = 1; start + len <= arr.length; start++) {
                int end = start + len - 1;
                cache[start][end] = Long.MAX_VALUE; // infinite
                for(int split = start; split < end; split++) {
                    // the cost to multiply A_start..split and A_(split+1)..end
                    long thisCost = arr[start - 1] * arr[split] * arr[end];
                    long cost = cache[start][split] + cache[split + 1][end] + thisCost;

                    // save the best cost and split point
                    if(cost < cache[start][end]) {
                        cache[start][end] = cost;
                        next[start][end] = split;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        buildString(sb, next, 1, arr.length - 1);
        
        // return the parenthesized string and the minimum cost
        return new Result(sb.toString(), cache[1][arr.length - 1]);
    }

    // instead of printing directly, add onto a string builder for testability
    private static void buildString(StringBuilder sb, int[][] next, int start, int end) {
        if(start == end) {
            sb.append("A").append(start);
            return;
        }
        sb.append("(");
        int split = next[start][end];
        buildString(sb, next, start, split);
        buildString(sb, next, split + 1, end);
        sb.append(")");
    }
}

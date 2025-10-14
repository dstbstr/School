// Dustin Randall
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class OS_Finding {
    private static final Random rand = new Random();

    /**
     * Opens a file by name and expects the file to contain one integer per line.
     * Returns the integers as an array if successful.
     */
    private static int[] readNumbers(String filename) {
        try (FileInputStream fis = new FileInputStream(filename)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            List<Integer> list = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(!line.isEmpty()) {
                    list.add(Integer.valueOf(line));
                }
            }
            return list.stream().mapToInt(i -> i).toArray();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read file: " + filename);
        }
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            throw new IllegalArgumentException("Expected 2 arguments, but received " + args.length);
        }
        
        int[] array = readNumbers(args[0]);
        int target = Integer.parseInt(args[1]);
        
        System.out.println(Solve(array, target));
    }
    public static String Solve(int[] array, int target) {
        // Bounds checking. Target is 1 indexed, so valid range is 1 to n
        if(target < 1 || target > array.length) {
            return "null";
        }
        return String.valueOf(Recurse(array, 0, array.length - 1, target));
    }

    /**
     * Will find the nth order statistic (nth smallest element) in the array
     * @param array The sub array to search
     * @param left The smallest index inclusive
     * @param right The largest index inclusive
     * @param target The order statistic to find, 1 indexed
     *
     * @return: the value of the nth order statistic
    */
    private static int Recurse(int[] array, int left, int right, int target) {
        // base case, array of 1 element must be the answer
        if(left == right) {
            // could add validation that target == 1
            return array[left];
        }

        // Move all elements <= pivot to the left, and all elements > pivot to the right
        int pivotIndex = Partition(array, left, right);

        // The number of values <= pivot
        int count = pivotIndex - left + 1;
        if(target == count) {
            return array[pivotIndex];
        } else if(target < count) {
            // Target is in the left partition, so search from left to pivotIndex - 1
            return Recurse(array, left, pivotIndex - 1, target);
        } else {
            // Target is in the right partition, so search from pivotIndex + 1 to right
            // We need to subtract count from target to account for the left partition's size
            return Recurse(array, pivotIndex + 1, right, target - count);
        }
    }

    /**
     * Partitions the array by selecting a random value as the pivot,
     * then moving all values <= pivot to the left side, and all values > pivot to the right.
     * 
     * @param array The sub array to partition
     * @param left The smallest index inclusive
     * @param right The largest index inclusive
     * @return The index of the pivot element after partitioning
     */
    private static int Partition(int[] array, int left, int right) {
        int partitionValue = rand.nextInt(left, right + 1);
        Swap(array, partitionValue, right);
        int pivot = array[right];
        int index = left;
        for(int i = left; i < right; i++) {
            if(array[i] <= pivot) {
                Swap(array, i, index);
                index++;
            }
        }

        // final swap required to move the partitionValue between the two partitions
        Swap(array, index, right);
        return index;
    }

    /**
     * Swaps two elements in an array
     * @param array The array in which to swap elements
     * @param i The index of the first element
     * @param j The index of the second element
     */
    private static void Swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}


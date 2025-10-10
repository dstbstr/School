import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class OS_Finding {
    private static final Random rand = new Random();

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
        if(target < 1 || target > array.length) {
            return "null";
        }
        return String.valueOf(Recurse(array, 0, array.length - 1, target));
    }

    private static int Recurse(int[] array, int left, int right, int target) {
        if(left == right) {
            return array[left];
        }
        int pivotIndex = Partition(array, left, right);
        int count = pivotIndex - left + 1;
        if(target == count) {
            return array[pivotIndex];
        } else if(target < count) {
            return Recurse(array, left, pivotIndex - 1, target);
        } else {
            return Recurse(array, pivotIndex + 1, right, target - count);
        }
    }
    private static int Partition(int[] array, int left, int right) {
        int partitionSize = rand.nextInt(left, right + 1);
        Swap(array, partitionSize, right);
        int pivot = array[right];
        int index = left;
        for(int i = left; i < right; i++) {
            if(array[i] <= pivot) {
                Swap(array, i, index);
                index++;
            }
        }
        Swap(array, index, right);
        return index;
    }

    private static void Swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}


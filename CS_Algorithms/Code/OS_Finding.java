import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class OS_Finding {
    public static int main(String[] args) {
        if(args.length != 2) {
            System.out.println("Expected 2 arguments, but received " + args.length);
            return 1;
        }
        
        int target;
        int[] array;
        try {
            target = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Invalid target value: " + args[1]);
            return 2;
        }
        try (FileInputStream fis = new FileInputStream(args[0])) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            List<Integer> list = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(!line.isEmpty()) {
                    list.add(Integer.valueOf(line));
                }
            }
            array = list.stream().mapToInt(i -> i).toArray();
        } catch (IOException ex) {
            System.out.println("Failed to read file: " + args[0]);
            return 3;
        }

        Solution solution = new Solution(target, array);
        System.out.println(solution.Solve());
        return 0;
    }
    static class Solution {
        private final int target;
        private final int[] array;
        public Solution(int target, int[] array) {
            this.target = target;
            this.array = array;
        }
        public String Solve() {
            if (target < 0 || target >= array.length) {
                return null;
            }
            return String.valueOf(array[target]);
        }

        private int randPartition(int left, int right) {
            Random rand = new Random();
            int pivot = rand.nextInt(left, right + 1);
            swap(pivot, right);
            return partition(left, right);
        }
        private int partition(int left, int right) {
            int pivot = array[right];
            int index = left;
            for(int i = left; i < right; i++) {
                if(array[i] <= pivot) {
                    swap(i, index);
                    index++;
                }
            }
            swap(index, right);
            return index;
        }

        private void swap(int i, int j) {
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}


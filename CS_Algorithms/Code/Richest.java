// // Dustin Randall
// import java.io.*;
// import java.nio.file.*;
// import java.util.Arrays;
// import java.util.concurrent.atomic.AtomicBoolean;
// import java.util.stream.Stream;

// public class Richest {
//     static class MinHeap {
//         private final int[] array;
//         private int size;
//         private boolean initialized = false;

//         // initialize the array with enough capacity, but don't build the heap yet
//         public MinHeap(int capacity) {
//             array = new int[capacity + 1];
//             size = 0;
//         }

//         // add an element to the end of the array
//         public void add(int value) {
//             // optionally check that initialized is false, and that size < capacity
//             size++;
//             array[size] = value;
//         }

//         // build the min-heap from the array
//         public void makeHeap() {
//             for(int i = size / 2; i > 0; i--) {
//                 heapify(i);
//             }
//             initialized = true;
//         }

//         public int getSize() { return size; }
//         public int getMin() { return array[1]; }
//         public boolean isInitialized() { return initialized; }

//         // replace the root with a new min, and re-heap
//         public void pushHeap(int value) {
//             array[1] = value;
//             heapify(1);
//         }

//         // destructive sort. Return as a stream to avoid duplicating the array
//         public Stream<Integer> toSortedArray() {
//             int initialSize = size;
//             for(int i = size; i > 0; i--) {
//                 swap(1, i);
//                 size--;
//                 heapify(1);
//             }
//             return Arrays.stream(array, 1, initialSize + 1).boxed();
//         }

//         private void heapify(int index) {
//             int min = index;
//             int left = 2 * index;
//             int right = left + 1;
//             if(left <= size && array[left] < array[min]) {
//                 min = left;
//             }
//             if(right <= size && array[right] < array[min]) {
//                 min = right;
//             }
//             if(min != index) {
//                 swap(index, min);
//                 heapify(min);
//             }
//         }

//         // std::swap anybody?
//         private void swap(int i, int j) {
//             int temp = array[i];
//             array[i] = array[j];
//             array[j] = temp;
//         }
//     }

//     // Given a stream of numbers, write them to a file, one per line
//     private static void writeResult(Stream<Integer> numbers, String fileName) throws IOException {
//         AtomicBoolean first = new AtomicBoolean(true);
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
//             numbers.forEach(number -> {
//                 try {
//                     if(!first.getAndSet(false)) {
//                         writer.newLine(); // Java makes this unnecessarily hard
//                     }
//                     writer.write(String.valueOf(number));
//                 } catch (IOException e) {
//                     throw new UncheckedIOException(e);
//                 }
//             });
//         }
//     }

//     // maxSize from the problem statement, passed as an argument for testability
//     private static final int MAX_SIZE = 10000;

//     public static void main(String[] args) throws IOException {
//         if(args.length != 1) {
//             throw new IllegalArgumentException("Expected 1 argument, but received " + args.length);
//         }

//         Path path = Paths.get(args[0]);

//         // stream in one line at a time
//         // if I stored this as a separate array, it would double the memory usage, violating the requirements
//         try (Stream<String> lines = Files.lines(path)) {
//             writeResult(Solve(lines, MAX_SIZE), "richest.output");
//         }
//     }

//     public static Stream<Integer> Solve(Stream<String> input, int maxSize) {
//         final MinHeap heap = new MinHeap(maxSize);

//         /*
//          * Ideally, this would read something like
//          * var heap new MinHeap(input.take(maxSize).Select(int.Parse).ToArray());
//          * input.Skip(maxSize).Select(int.Parse).ForEach(num => if(num > heap.GetMin()) heap.Push(num));
//          * but this isn't C#.
//          */
//         input.forEach(val -> {
//             int num = Integer.parseInt(val);
//             // for the first maxSize elements, add them to the heap's array
//             if(heap.getSize() < maxSize) {
//                 heap.add(num);
//                 return;
//             } else if(!heap.isInitialized()) {
//                 // once we have maxSize elements, build the heap once
//                 heap.makeHeap();
//             }

//             // update the heap if the next number is larger than the root
//             if(num > heap.getMin()) {
//                 heap.pushHeap(num);
//             }
//         });

//         // if we had fewer elements than maxSize, we need to build the heap now
//         if(!heap.isInitialized()) {
//             heap.makeHeap();
//         }

//         return heap.toSortedArray();
//     }
// }

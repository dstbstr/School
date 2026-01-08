//Dustin Randall

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class Dijkstra {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 arguments, but received " + args.length);
        }
        Path path = Paths.get(args[0]);
        List<String> lines = Files.readAllLines(path);
        Dijkstra instance = new Dijkstra();
        int startVertex = Integer.parseInt(args[1]);

        // Result contains the shortest path distances
        instance.Solve(lines, startVertex);
    }

    public class LinkedList {

        public class Node {

            public final int value;
            public Node next = null;

            public Node(int value) {
                this.value = value;
            }
        }

        private Node head;
        private Node tail;
        private int size = 0;

        public void PushBack(int val) {
            Node node = new Node(val);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                tail.next = node;
                tail = node;
            }
            size++;
        }

        public void PushFront(int val) {
            Node node = new Node(val);
            if (head == null) {
                head = node;
                tail = node;
            } else {
                node.next = head;
                head = node;
            }
            size++;
        }

        public int Size() {
            return size;
        }
    }

    public class Graph {

        public class Vertex {

            public final LinkedList edges;
            public boolean visited = false;

            public Vertex() {
                edges = new LinkedList();
            }
        }
        Vertex[] verticies;

        // initialize n nodes in the graph
        public Graph(int vCount) {
            verticies = new Vertex[vCount];
            for (int i = 0; i < vCount; i++) {
                verticies[i] = new Vertex();
            }
        }

        // Parse a line of the form "from:to1,to2,..."
        public void ParseLine(String line) {
            String[] parts = line.split(":");
            int from = Integer.parseInt(parts[0]);
            if (parts.length == 2) {
                String[] tos = parts[1].split(",");
                for (String toStr : tos) {
                    int to = Integer.parseInt(toStr);
                    InsertEdge(from, to);
                }
            }
            // optionally throw if parts.Length > 2
        }

        public void InsertEdge(int from, int to) {
            verticies[from].edges.PushBack(to);
        }
    }

    public void makeHeap(int[] heap, int size) {
        for (int i = size / 2; i > 0; i--) {
            heapify(heap, i, size);
        }
    }

    public int popHeap(int[] heap, int size) {
        swap(heap, 1, size);
        heapify(heap, 1, size - 1);
        return heap[size];
    }

    public void heapify(int[] heap, int index, int size) {
        int min = index;
        int left = 2 * index;
        int right = left + 1;
        if (left <= size && heap[left] < heap[min]) {
            min = left;
        }
        if (right <= size && heap[right] < heap[min]) {
            min = right;
        }
        if (min != index) {
            swap(heap, index, min);
            heapify(heap, min, size);
        }
    }

    private void swap(int[] heap, int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public void Solve(List<String> lines, int startVertex) {
        // Initialize the graph
        Graph graph = new Graph(lines.size());
        for (String line : lines) {
            graph.ParseLine(line);
        }

        graph.verticies[startVertex].visited = true;
        // TODO
    }
}

//Dustin Randall

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

class LinkedList<T> {
    public class Node {
        public final T value;
        public Node next = null;

        public Node(T value) {
            this.value = value;
        }
    }

    public Node head;
    private Node tail;

    public void add(T value) {
        Node node = new Node(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
    }

    public void forEach(Consumer<T> func) {
        Node curr = head;
        while(curr != null) {
            func.accept(curr.value);
            curr = curr.next;
        }
    }
}

class Graph {
    public class Edge {
        public final int to;
        public final int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    public class Vertex {
        public final LinkedList<Edge> edges = new LinkedList<>();
        public int id = 0;
        public int distance = Integer.MAX_VALUE;
        public Vertex previous = null;

        public Vertex(int id) {
            this.id = id;
        }
    }

    Vertex[] verticies;

    // initialize n nodes in the graph
    public Graph(int vCount) {
        verticies = new Vertex[vCount];
        for (int i = 0; i < vCount; i++) {
            verticies[i] = new Vertex(i);
        }
    }

    // Parse a line of the form "from:to1,w1;to2,w2;..."
    public void ParseLine(String line) {
        String[] parts = line.split(":");
        int from = Integer.parseInt(parts[0]);
        if (parts.length == 2) {
            String[] tos = parts[1].split(";");
            for (String toStr : tos) {
                String[] toParts = toStr.split(",");
                int to = Integer.parseInt(toParts[0]);
                int weight = Integer.parseInt(toParts[1]);
                verticies[from].edges.add(new Edge(to, weight));
            }
        }
    }
}

class Heap {
    public static<T> void makeHeap(T[] array, int size, Comparator<T> comparator) {
        for (int i = size / 2; i > 0; i--) {
            heapify(array, i, size, comparator);
        }
    }

    public static <T> T pop(T[] array, int size, Comparator<T> comparator) {
        swap(array, 1, size);
        heapify(array, 1, size - 1, comparator);
        return array[size];
    }

    public static <T> void heapify(T[] array, int index, int size, Comparator<T> comparator) {
        int min = index;
        int left = 2 * index;
        int right = left + 1;
        if (left <= size && comparator.compare(array[left], array[min]) < 0) {
            min = left;
        }
        if (right <= size && comparator.compare(array[right], array[min]) < 0) {
            min = right;
        }
        if (min != index) {
            swap(array, index, min);
            heapify(array, min, size, comparator);
        }
    }

    public static <T> void bubbleUp(T[] array, int index, Comparator<T> comparator) {
        while (index > 1 && comparator.compare(array[index], array[index / 2]) < 0) {
            swap(array, index, index / 2);
            index = index / 2;
        }
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

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

    LinkedList<String> Solve(List<String> lines, int startVertex) {
        // Initialize the graph
        Graph graph = new Graph(lines.size());
        for (String line : lines) {
            graph.ParseLine(line);
        }

        record State(int distance, int id) {}
        State[] heap = new State[graph.verticies.length + 1];
        Comparator<State> cmp = Comparator.comparingInt(s -> s.distance);

        int size = 0;
        for (Graph.Vertex v : graph.verticies) {
            v.distance = v.id == startVertex ? 0 : 999999; // INT_MAX causes overflow
            heap[++size] = new State(v.distance, v.id);
        }

        Heap.makeHeap(heap, size, cmp);
        while(size > 1) {
            State best = Heap.pop(heap, size, cmp);
            Graph.Vertex u = graph.verticies[best.id];
            size--;
            if(best.distance > u.distance) continue; // stale
            LinkedList<Graph.Edge>.Node current = u.edges.head;
            while(current != null) {
                Graph.Edge edge = current.value;
                current = current.next;

                // relax edge
                Graph.Vertex target = graph.verticies[edge.to];
                if(target.distance > u.distance + edge.weight) {
                    target.distance = u.distance + edge.weight;
                    target.previous = u;
                    heap[++size] = new State(target.distance, target.id);
                    // repair heap
                    Heap.bubbleUp(heap, size, cmp);
                }
            }
        }

        LinkedList<String> results = new LinkedList<>();
        for(int i = 0; i < lines.size(); i++) {
            if(i == startVertex) continue;
            String result = buildPath(i, graph.verticies[i]);
            results.add(result);
        }

        return results;
    }

    private String buildPath(int index, Graph.Vertex vertex) {
        String result = "[" + Integer.toString(index) + "]";

        if(vertex.previous == null) {
            result += "unreachable";
            return result;
        }
        int distance = vertex.distance;
        String path = Integer.toString(vertex.id);
        while(vertex.previous != null) {
            vertex = vertex.previous;
            path = Integer.toString(vertex.id) + "," + path;
        }
        result += "shortest path:(" + path + ") shortest distance:" + Integer.toString(distance);
        return result;
    }
}

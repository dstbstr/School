//Dustin Randall
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;

public class TopoSort {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument, but received " + args.length);
        }
        Path path = Paths.get(args[0]);
        List<String> lines = Files.readAllLines(path);
        TopoSort instance = new TopoSort();
        
        // Result contains the topological ordered nodes
        LinkedList result = instance.Solve(lines);

        // Iterate through the linked list and print the values
        TopoSort.LinkedList.Node current = result.head;
        while(current != null) {
            System.out.print(current.value);
            current = current.next;
            // avoid the trailing comma
            if(current != null) {
                System.out.print(",");
            }
        }
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
            if(head == null) {
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
            if(head == null) {
                head = node;
                tail = node;
            } else {
                node.next = head;
                head = node;
            }
            size++;
        }

        public int Size() { return size; }

        // Apply func to each node in the list in order
        public void ForEach(Consumer<Node> func) {
            Node curr = head;
            while(curr != null) {
                func.accept(curr);
                curr = curr.next;
            }
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
            for(int i = 0; i < vCount; i++) {
                verticies[i] = new Vertex();
            }
        }

        // Parse a line of the form "from:to1,to2,..."
        public void ParseLine(String line) {
            String[] parts = line.split(":");
            int from = Integer.parseInt(parts[0]);
            if(parts.length == 2) {
                String[] tos = parts[1].split(",");
                for(String toStr : tos) {
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

    // separated out for testability
    public LinkedList Solve(List<String> lines) {
        // Initialize the graph
        Graph graph = new Graph(lines.size());
        for(String line : lines) {
            graph.ParseLine(line);
        }

        LinkedList result = new LinkedList();
        // Pass the graph and the out result to DFS
        Dfs(graph, result);
        return result;
    }

    private void Dfs(Graph graph, LinkedList result) {
        // call each vertex in case of a disjoint graph
        for(int i = 0; i < graph.verticies.length; i++) {
            DfsVisit(graph, i, result);
        }
    }

    private void DfsVisit(Graph graph, int vertex, LinkedList result) {
        // if this node has been visited, don't add it to the result,
        // just return
        if(graph.verticies[vertex].visited) {
            return;
        }

        graph.verticies[vertex].visited = true;

        // ForEach guarantees in-order application
        graph.verticies[vertex].edges.ForEach(edgeNode -> {
            DfsVisit(graph, edgeNode.value, result);
        });

        // after all connected edges have been visited, then we can add
        // our vertex to the output's front.
        result.PushFront(vertex);
    }
}

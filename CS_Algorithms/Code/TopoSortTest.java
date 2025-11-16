
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class TopoSortTest {

    private TopoSort topo = new TopoSort();
    private TopoSort.LinkedList list = topo.new LinkedList();

    @Test
    public void LinkedListSize_AfterPushBack_IsOne() {
        list.PushBack(5);
        assertEquals(1, list.Size());
    }

    @Test
    public void LinkedListForEach_AfterAdd_CallsForEachElement() {
        list.PushBack(3);
        list.PushBack(5);
        list.PushBack(7);
        List<Integer> collected = new ArrayList<Integer>();
        list.ForEach(node -> collected.add(node.value));
        assertArrayEquals(new Integer[]{3, 5, 7}, collected.toArray());
    }

    @Test
    public void Graph_ParseLine_AddsEdges() {
        TopoSort.Graph graph = topo.new Graph(4);
        graph.ParseLine("1:3");
        graph.ParseLine("0:2,1,3");
        graph.ParseLine("3:2");
        graph.ParseLine("2:");

        assertEquals(3, graph.verticies[0].edges.Size());
        assertEquals(1, graph.verticies[1].edges.Size());
        assertEquals(0, graph.verticies[2].edges.Size());
        assertEquals(1, graph.verticies[3].edges.Size());
    }

    @Test
    public void Solve_WithSampleInput_ReturnsExpectedOutput() {
        List<String> lines = new ArrayList<String>();
        lines.add("1:3");
        lines.add("0:2,1,3");
        lines.add("3:2");
        lines.add("2");

        TopoSort.LinkedList result = topo.Solve(lines);
        assertTrue(isValidTopologicalSort(lines, result));
    }

    @Test
    public void Solve_WithLargerInput_IsValidSort() {
        List<String> lines = new ArrayList<>();
        lines.add("0:4,5");
        lines.add("1:3,4");
        lines.add("2:5");
        lines.add("3:2");
        lines.add("4:");
        lines.add("5:");
        TopoSort.LinkedList result = topo.Solve(lines);
        assertTrue(isValidTopologicalSort(lines, result));
    }

    @Test
    public void Solve_WithSingleInput_IsValidSort() {
        List<String> lines = new ArrayList<>();
        lines.add("0:");
        TopoSort.LinkedList result = topo.Solve(lines);
        assertTrue(isValidTopologicalSort(lines, result));
    }

    @Test
    public void Solve_WithDisjoinGraph_IsValidSort() {
        List<String> lines = new ArrayList<>();
        lines.add("0:1");
        lines.add("1:");
        lines.add("2:3");
        lines.add("3:");
        TopoSort.LinkedList result = topo.Solve(lines);
        assertTrue(isValidTopologicalSort(lines, result));
    }
    
    private boolean isValidTopologicalSort(List<String> input, TopoSort.LinkedList result) {
        TopoSort.Graph graph = topo.new Graph(input.size());
        for (String line : input) {
            graph.ParseLine(line);
        }
        List<Integer> order = new ArrayList<Integer>();
        result.ForEach(node -> order.add(node.value));

        // Map each vertex to its position in the ordering
        int[] position = new int[graph.verticies.length];
        for (int i = 0; i < order.size(); i++) {
            position[order.get(i)] = i;
        }

        // Check that for every edge (u -> v), u comes before v
        for (int u = 0; u < graph.verticies.length; u++) {
            final int from = u;
            graph.verticies[u].edges.ForEach(edgeNode -> {
                int to = edgeNode.value;
                if (position[from] >= position[to]) {
                    throw new AssertionError("Vertex " + from + " must come before " + to);
                }
            });
        }

        return true;
    }
}

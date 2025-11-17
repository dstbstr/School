import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DijkstraTests {

    @Test
    public void Graph_ParseLine_AddsEdges() {
        Graph graph = new Graph(4);
        graph.ParseLine("1:3,1");
        graph.ParseLine("0:3,4;1,2;2,5");
        graph.ParseLine("2:");
        graph.ParseLine("3:2,2");

        graph.verticies[0].edges.forEach(edge -> {
            if(edge.to == 3) assertEquals(4, edge.weight);
            else if(edge.to == 1) assertEquals(2, edge.weight);
            else if(edge.to == 2) assertEquals(5, edge.weight);
            else fail("Unexpected edge to " + edge.to);
        });
        graph.verticies[1].edges.forEach(edge -> {
            if(edge.to == 3) assertEquals(1, edge.weight);
            else fail("Unexpected edge to " + edge.to);
        });
        graph.verticies[2].edges.forEach(edge -> {
            fail("Unexpected edge to " + edge.to);
        });
        graph.verticies[3].edges.forEach(edge -> {
            if(edge.to == 2) assertEquals(2, edge.weight);
            else fail("Unexpected edge to " + edge.to);
        });
    }

    @Test
    public void Solve_WithSample1Input_ReturnsExpectedOutput() {
        List<String> lines = List.of("1:3,1", "0:3,4;1,2;2,5", "2:", "3:2,2");
        LinkedList<String> actual = new Dijkstra().Solve(lines, 3);

        LinkedList<String>.Node current = actual.head;
        assertEquals("[0]unreachable", current.value);
        current = current.next;
        assertEquals("[1]unreachable", current.value);
        current = current.next;
        assertEquals("[2]shortest path:(3,2) shortest distance:2", current.value);
        assertNull(current.next);
    }
}

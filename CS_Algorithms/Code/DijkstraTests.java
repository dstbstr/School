import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DijkstraTests {
    private Dijkstra instance = new Dijkstra();
    private Dijkstra.LinkedList list = instance.new LinkedList();

    @Test
    public void LinkedListSize_AfterPushBack_IsOne() {
        list.PushBack(5);
        assertEquals(1, list.Size());
    }
 
    @Test
    public void Graph_ParseLine_AddsEdges() {
        Dijkstra.Graph graph = instance.new Graph(4);
        graph.ParseLine("1:3");
        graph.ParseLine("0:2,1,3");
        graph.ParseLine("3:2");
        graph.ParseLine("2:");

        assertEquals(3, graph.verticies[0].edges.Size());
        assertEquals(1, graph.verticies[1].edges.Size());
        assertEquals(0, graph.verticies[2].edges.Size());
        assertEquals(1, graph.verticies[3].edges.Size());
    }

}

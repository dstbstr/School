// Queue class: singly linked, FIFO. enqueue at the rear, dequeue from the front. All O(1).
public class Queue {

    private class Node {
        Object data;
        Node next;
        Node(Object data) { this.data = data; this.next = null; }
    }

    private Node front;   // dequeue from here
    private Node rear;    // enqueue here
    private int size;

    public Queue() { front = null; rear = null; size = 0; }

    public boolean isEmpty() { return size == 0; }
    public int size()        { return size; }

    public void enqueue(Object data) {
        Node n = new Node(data);
        if (isEmpty()) {
            front = n;
            rear = n;
        } else {
            rear.next = n;
            rear = n;
        }
        size++;
    }

    public Object dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        Object data = front.data;
        front = front.next;
        size--;
        if (isEmpty()) {
            rear = null;
        }
        return data;
    }
}

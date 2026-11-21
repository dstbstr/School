public class Queue {

    private class Node {
        LinkedList data;
        Node next;

        Node(LinkedList d) {
            data = d;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public Queue() {
        front = rear = null;
        size = 0;
    }

    public void enqueue(LinkedList L) {
        Node n = new Node(L);
        if (size == 0) {
            front = rear = n;
        } else {
            rear.next = n;
            rear = n;
        }
        size++;
    }

    public LinkedList dequeue() {
        if (size == 0) throw new RuntimeException("Empty queue");

        LinkedList v = front.data;
        front = front.next;
        size--;
        if (size == 0) rear = null;

        return v;
    }

    public int size() { return size; }
}

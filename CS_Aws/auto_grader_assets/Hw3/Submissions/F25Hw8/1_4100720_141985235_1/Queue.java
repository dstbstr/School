public class Queue {

    private class Node {
        private Object data;
        private Node next;
        private Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node front, rear;
    private int size;

    public Queue() {
        front = rear = null;
        size = 0;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    public void enqueue(Object x) {
        Node p = new Node(x, null);
        if (rear == null) {
            front = rear = p;
        } else {
            rear.next = p;
            rear = p;
        }
        size++;
    }

    public Object dequeue() {
        if (front == null) return null;
        Object x = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return x;
    }

    public Object first() {
        if (front == null) return null;
        return front.data;
    }
}

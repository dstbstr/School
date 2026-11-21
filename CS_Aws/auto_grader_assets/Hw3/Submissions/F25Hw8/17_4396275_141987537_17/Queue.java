public class Queue {
    private class Node {
        LinkedList data;
        private Node next;

        public Node(LinkedList elem) {
            this.data = elem;
            this.next = null;
        }
    }

    protected Node head, tail;
    protected int size;

    public Queue() {
        this.head = null;
        this.tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(LinkedList elem) {
        Node node = new Node(elem);
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public LinkedList dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty.");
        }
        LinkedList tmp = head.data;
        head = head.next;
        size--;
        if (size == 0) {
            tail = null;
        }
        return tmp;
    }
}

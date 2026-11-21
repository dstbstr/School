public class Queue<T> {
    private class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public Queue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item, null);
        if (this.size == 0) {
            head = newNode; // initialize head and tail for empty queue
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public T dequeue() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        T item = head.data;
        head = head.next;
        size--;
        if (this.size == 0) tail = null; // reset tail if queue becomes empty
        return item;
    }

    public T front() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }

        return head.data;
    }

    public int size() {
        return this.size;
    }
}

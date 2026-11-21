public class Queue {
    public class Node {
        private Object data;
        private Node next, tail;
        private Node(Object data, Node next, Node tail) {
            this.data = data;
            this.next = next;
            this.tail = tail;
        }
    }
    protected Node head, tail;
    protected int size;
    public Queue() {
        this.head = new Node(null, null, null);
        this.tail = this.head;
        this.size = 0;
    }
    public int size() {return size;}
    public boolean isEmpty() {
        if (head == tail) return true;
        return false;
    }
    public Object front() throws Exception {
        if (isEmpty()) throw new Exception("Queue is empty.");
        return head.data;
    }
    public void enqueue(Object data) {
        Node newNode = new Node(data, head, head);
        if (head == tail) head = newNode;
        else tail.next = newNode;
        tail = newNode;
        size++;
    }
    public Object dequeue() {
        if (isEmpty()) throw new NullPointerException("Queue is empty.");
        Object temp = head.data;
        head = head.next;
        size--;
        if (size == 0) tail = null;
        return temp;
    }
}

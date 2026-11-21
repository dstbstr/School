public class Queue {
    private static class Node {
        LinkedList data;
        Node next;

        Node(LinkedList data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node front;
    private Node rear;
    private int size;

    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }

    public void enqueue(LinkedList list) {
        Node newNode = new Node(list);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public LinkedList dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        LinkedList data = front.data;
        front = front.next;
        size--;
        if (front == null) {
            rear = null;
        }
        return data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}

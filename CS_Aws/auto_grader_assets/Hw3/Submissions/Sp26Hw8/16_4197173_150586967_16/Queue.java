public class Queue {
    private class Node {
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

        if (front == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    public LinkedList dequeue() {
        if (front == null) {
            throw new RuntimeException("Queue is empty");
        }

        LinkedList value = front.data;
        front = front.next;
        size--;

        if (front == null) {
            rear = null;
        }

        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }
}
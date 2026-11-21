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

    public void enqueue(LinkedList value) {
        Node nn = new Node(value);
        if (rear == null){
            front = rear = nn;
        } else {
            rear.next = nn;
            rear = nn;
        }
        size++;
    }

    public LinkedList dequeue() {
        if (front == null) return null;
        LinkedList temp = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}


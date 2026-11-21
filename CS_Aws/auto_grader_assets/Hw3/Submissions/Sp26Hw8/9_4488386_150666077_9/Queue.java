public class Queue {
    private class QNode {
        LinkedList data;
        QNode next;

        QNode(LinkedList data) { this.data = data; }
    }

    private QNode front;
    private QNode rear;
    private int size;

    public Queue() {
        front = rear = null;
        size = 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public void enqueue(LinkedList item) {
        QNode node = new QNode(item);

        if (isEmpty()) {
            front = rear = node;
        } else {
            rear.next = node;
            rear = node;
        }
        size++;
    }

    public LinkedList dequeue() {
        if (isEmpty()) { throw new RuntimeException("Queue Empty"); }

        LinkedList value = front.data;
        front = front.next;
        size--;

        if (size == 0) { rear = null; }

        return value;
    }
}
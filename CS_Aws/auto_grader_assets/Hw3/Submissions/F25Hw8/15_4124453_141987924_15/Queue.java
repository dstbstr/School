public class Queue {

    // NODE FOR THE QUEUE
    private static class QNode {
        LinkedList data;
        QNode next;

        QNode(LinkedList d) {
            data = d;
            next = null;
        }
    }

    private QNode front;
    private QNode rear;
    private int size;

    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public void enqueue(LinkedList list) {
        QNode newNode = new QNode(list);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public LinkedList dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Empty queue");
        }
        LinkedList result = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return result;
    }
}

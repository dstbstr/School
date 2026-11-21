public class Queue {

    private class QNode {
        LinkedList data;
        QNode next;

        QNode(LinkedList data) {
            this.data = data;
        }
    }

    private QNode front;
    private QNode rear;
    private int size;

    public Queue() {
        front = rear = null;
        size = 0;
    }

    public void enqueue(LinkedList list) {
        QNode newNode = new QNode(list);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }


    public LinkedList dequeue() {
        if (isEmpty())
            throw new RuntimeException("Queue is empty");

        LinkedList value = front.data;

        front = front.next;
        size--;

        if (front == null)
            rear = null;

        return value;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
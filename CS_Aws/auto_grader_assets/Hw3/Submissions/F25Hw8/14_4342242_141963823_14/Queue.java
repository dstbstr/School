public class Queue {

    private static class QNode {
        LinkedList data;
        QNode next;

        QNode(LinkedList d) {
            data = d;
            next = null;
        }
    }

    private QNode front; //first node
    private QNode rear; //last node
    private int size; //number items in queue

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
        QNode node = new QNode(list);
        if (rear == null) {
            front = node;
            rear = node;
        } else {
            rear.next = node;
            rear = node;
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


}

public class Queue {
    private class QueueNode {
        LinkedList data;
        QueueNode next;

        QueueNode (LinkedList d) {data = d;}
    }

    private QueueNode front;
    private QueueNode rear;
    private int size = 0;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(LinkedList L) {
        QueueNode newNode = new QueueNode(L);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        }
        else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public LinkedList dequeue() {
        LinkedList list = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        else {
            rear.next = null;
        }
        size--;
        return list;
    }
}

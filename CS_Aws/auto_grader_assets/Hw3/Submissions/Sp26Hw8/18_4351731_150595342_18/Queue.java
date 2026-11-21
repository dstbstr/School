public class Queue {

    private class QNode {

        LinkedList data;
        QNode next;

        public QNode(LinkedList data) {
            this.data = data;
            this.next = null;
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
        }
        else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public LinkedList dequeue() {

        if (front == null) {
            return null;
        }

        LinkedList temp = front.data;
        front = front.next;

        if (front == null) {
            rear = null;
        }

        size--;
        return temp;
    }
}
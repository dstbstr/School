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

    public void enqueue(LinkedList list) {

        QNode temp = new QNode(list);

        if(front == null) {
            front = rear = temp;
        }
        else {
            rear.next = temp;
            rear = temp;
        }

        size++;
    }

    public LinkedList dequeue() {

        if(front == null)
            return null;

        LinkedList result = front.data;

        front = front.next;

        if(front == null)
            rear = null;

        size--;

        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

public class Queue {
    private class QueueNode {
        LinkedList data;
        QueueNode next;

        public QueueNode(LinkedList data) {
            this.data = data;
            this.next = null;
        }
    }

    private QueueNode front;
    private QueueNode rear;
    private int size;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(LinkedList list) {
        QueueNode newNode = new QueueNode(list);
        if (this.rear == null) {
            this.front = this.rear = newNode;
        } else {
            this.rear.next = newNode;
            this.rear = newNode;
        }
        this.size++;
    }

    public LinkedList dequeue() {
        if (this.front == null) {
            return null;
        }
        LinkedList temp = this.front.data;
        this.front = this.front.next;
        if (this.front == null) {
            this.rear = null;
        }
        this.size--;
        return temp;
    }
    public int size() {
        return this.size;
    }
}

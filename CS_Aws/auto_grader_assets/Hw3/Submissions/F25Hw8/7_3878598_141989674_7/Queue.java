public class Queue {



    private class QNode {
        private LinkedList data;
        private QNode next;

        private QNode(LinkedList d) {
            this.data = d;
            this.next = null;
        }
    }

    private QNode front;
    private QNode rear;
    private int size;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
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
        if (front == null) {
            throw new RuntimeException("The queue is empty");
        }
        QNode node = front;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return node.data;
    }

    public int size() {
        return this.size;
    }


}





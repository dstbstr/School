public class Queue {
    public class QNode {
        LinkedList data;
        QNode next;
        public QNode(LinkedList data) {
            this.data = data;
            this.next = null;
        }
    }
    public QNode head;
    public QNode tail;
    public void enqueue(LinkedList data) {
        QNode newNode = new QNode(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }
    }
    public LinkedList dequeue() {
        if (head == null) {
            return null;
        }
        else {
            QNode temp = head;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            return temp.data;
        }
    }
}

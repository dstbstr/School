public class Queue<T> {

    // Stores all data and links to the next queue element
    private class QNode {
        LinkedList data;
        QNode next;

        QNode(LinkedList d) {
            data = d;
            next = null;
        }
    }

    private QNode front;
    private QNode back;
    private int size;

    public Queue() {
        front = back = null;
        size = 0;
    }

    // Adds an item to the of the queue
    public void enqueue(LinkedList item) {
        QNode n = new QNode(item);

        if (back == null) {
            front = back = n;

        } else {
            back.next = n;
            back = n;
        }

        size++;

    }

    // First removes and then returns the item at the front of the queue
    public LinkedList dequeue() {
        if (front == null)
            throw new RuntimeException("dequeue on empty queue");

        LinkedList val = front.data;
        front = front.next;

        if (front == null)
            back = null;

        size--;
        return val;
    }

    // If there are no elements in the queue, it returns empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the number of queued elements
    public int size() {
        return size;
    }

}// end Queue
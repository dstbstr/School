public class Queue {
    private class Node {

        LinkedList data;
        Node next;

        Node(LinkedList d) {
            data = d;
        } // end of Node

    } // end of node

    // place holders
    private Node front;
    private Node back;
    private int size;

    public Queue() {
        front = back = null;
        size = 0;

    } // end of Queue

    public void enqueue(LinkedList x) {
        Node n = new Node(x);

        if (back == null) {
            front = back = n;
        } else {
            back.next = n;
            back = n;
        } // end of if else

        size++;
    } // end of enqueue

    public LinkedList dequeue() {
        if (front == null) return null;

        LinkedList temp = front.data;
        front = front.next;

        if (front == null) back = null;
        size--;
        return temp;
    } // end of dequeue

    public boolean isEmpty() {
        return size == 0;
    } // end of isEmpty

    public int size() {
        return size;
    } // end of size
} // end of queue


public class Queue<E> {
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<E> front;
    private Node<E> rear;
    private int size;

    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }


    public int getSize() {
        return size;
    }


    public void enqueue(E item) {
        Node<E> newNode = new Node<E>(item);
        if (rear == null) {    
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public E dequeue() {
        if (front == null) {
            throw new IllegalStateException("dequeue() called on an empty queue");
        }
        E item = front.data;
        front = front.next;
        size--;
        if (front == null) {   
            rear = null;
        }
        return item;
    }
}

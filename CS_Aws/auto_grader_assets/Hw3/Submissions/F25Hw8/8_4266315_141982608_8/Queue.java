public class Queue<E> {
    private final QueueNode<E> head;
    private int size;

    public Queue() {
        this.head = new QueueNode<>(null, null, null);
        this.head.next = this.head;
        this.head.prev = this.head;
        this.size = 0;
    }

    public void enqueue(E data) {
        QueueNode<E> newNode = new QueueNode<>(data, this.head, this.head.next);
        this.head.next.prev = newNode;
        this.head.next = newNode;
        this.size ++;
    }

    public E dequeue() {
        if(this.size == 0) return null;
        E toReturn = this.head.prev.data;
        this.head.prev.prev.next = this.head;
        this.head.prev = this.head.prev.prev;
        this.size --;
        return toReturn;
    }

    public int getSize() {
        return this.size;
    }

    private static class QueueNode<E> {
        private final E data;
        private QueueNode<E> prev;
        private QueueNode<E> next;
        private QueueNode(E data, QueueNode<E> prev, QueueNode<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}

public class LinkedQueue {
    private class Node {

        private Object data;
        private Node next;

        //
        public Node(Object elem) {
            this.data = elem;
            this.next = null;
        }
    }

    protected Node head, tail; // the head and tail nodes
    protected int size; // Keeps track of number of elements in queue

    /**
     * Creates an empty queue.
     */
    public LinkedQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() { // # Return the current queue size
        return this.size;
    }

    public boolean isEmpty() { // # Returns true iff queue is empty
        if ((head == null) && (tail == null) || this.size == 0)
            return true;

        return false;
    } // end of isEmpty method

    public void enqueue(Object elem) {
        Node node = new Node(elem);

        if (size == 0)
            head = node; // edge case of a previously empty queue
        else
            tail.next = node; // add node at the tail of the list

        tail = node; // update the reference to the tail node
        size++; // tail always points to the last inserted item.
    } // end of enqueue method

    public Object front() throws Exception { // # Return the first queue element
        if (size == 0)
            throw new Exception("Queue is empty.");

        return head.data;
    } // end of front method

    public Object dequeue() throws Exception {
        if (size == 0)
            throw new Exception("Queue is empty.");
        Object tmp = head.data;
        head = head.next; // head always points to the item that is ready to be dequeued next.
        size--;

        if (size == 0)
            tail = null; // the queue is now empty, why not set head = null also?
        return tmp;
    }// end of dequeue method

    public String toString() {
        String s = "";
        s += "(";
        if (!isEmpty()) {
            Node p = this.head;
            do {
                s += p.data;
                if (p != tail)
                    s += ", ";
                p = p.next;
            } while (p != null);
        }
        s += ")";
        return s;
    }
}

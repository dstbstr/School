public class Queue { // First you create the class for a Queue

    private class Node{ // Then you create the class for a node since the queue class uses nodes

        private Object data; // You define the fields that make up a node
        private Node next;

        private Node (Object data){ // You create the node constructor
            this.data = data;
            this.next = next;
        }
    }

    protected Node head; // Define what things make up a queue object
    protected Node tail;
    protected int size;

    public Queue(){ // This is the constructor for the Queue class
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size(){ // Gets the size of the queue object
        return this.size;
    }

    public boolean isEmpty(){ // returns if the queue object is empty
        if (this.size == 0){
            return true;
        }
        return false;
    }

    public void enqueue(Object data){ // This is basically just add last except we have a tail to keep track
        // does not need to return anything since you are just adding to the end of the queue
        Node nn = new Node(data);

        if (size == 0) {
            head = nn;
        }else{
            tail.next = nn;
        }
        tail = nn;
        size++;
    }

    public Object dequeue() throws Exception{
        if (size == 0){
            throw new Exception("List is empty");
        }
        // need to remove the first thing in the queue
        Object temp = head.data;
        head = head.next;
        size--;

        if (size == 0){
            tail = null; // Since you do head = head.next earlier, if the list is already 1 element head would be null
        } // however tail still would need to become null as well
        return temp;
    }

    public Object front() throws Exception{
        if (size == 0){
            throw new Exception("List is empty");
        }
        return head.data;
    }

}

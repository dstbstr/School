public class Queue {
 private static class Node {
        Object data;
        Node next;
        Node(Object data){
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public Queue() { //empty queue
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * @author Spencer Olson
     * @param d Object d is the data that is being passed through the method
     * method enqueue(Object d) creates a new node with the data d and puts it at the top of the queue and increments size
     */
    public void enqueue(Object d){
        Node newNode = new Node(d);
        if (tail == null) { //base Case
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * @author Spencer Olson
     * @return Returns the temporary object storing the data of the head
     * method dequeue() removes the first node of the queue and returns it
     */
    public Object dequeue() {
        Object temp;
        if (head == null){ //base case
            return null;
        }
        temp = head.data;
        head = head.next;
        size--;
        
        return temp;
    }


    public boolean isEmpty(){ 
        return size == 0;
    }

    public int size(){
        return size;
    }

}
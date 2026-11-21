
// here we create our custom queue that store LinkedList objects
 // it is used as part of the mergeSort implementation
public class Queue {


    // this is our custom queue not built in java
    private static class QNode {
        // here data stores linkedList object
        LinkedList data;
        // pointer to the next QNode
        QNode next;
        // here we create a new QNode
        QNode(LinkedList list) {
            data = list;
            // we stores the provided list in data
            next = null;
            // and initializes next to null no next node exist yet
        }
    }
   //here front is the  pointer to the first node in the queue
    private QNode front;
    // rear is the pointer to the last node in the queue
    private QNode rear;
    // and size is the current number of elements stored
    private int size;
    // here we initializes an empty queue
    public Queue() {
        // front = null because no first node
        front = rear = null;
        // rear to null because no last node exist
        size = 0;
        // and our size is 0
    }

    //here is our method is to add a LinkedList ot the queue
    public void enqueue(LinkedList list) {
        // first create the new node and wraps the linkedList in a new Qnode
        QNode newNode = new QNode(list);
       // if rear is null it means queue is empty
        if (rear == null) {
            // the new node becomes both the front and rear
            front = rear = newNode;

        } else {// if our queue already has element
            rear.next = newNode;
            // attache the new ndoe to the end of the current queue
            rear = newNode;
            // move the rear pointer to the new node
        }

        size++;
    }

    // we make our dequeue method here that will remove and return the
    // linked list stored at the front of the queue
    public LinkedList dequeue() {
      // check if our queue is empty if yes there is nothing to remove so
        // return null
        if (front == null) return null;
        // we store the linkedList inside the front node so it can be returned
        // after removal
        LinkedList val = front.data;
        // remove the first node by advancing the front pointer
        front = front.next;
      //if removing the node made the queue empty update the rear as well
        if (front == null)

            rear = null;
        // decrease the size
        size--;
        // return val
        return val;
    }
    // here we return the number of element currently in the queue

    public int size() { return size; }
  // return true if the queue has 0 elements otherwise false
    public boolean isEmpty() { return size == 0; }
}

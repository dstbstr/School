

/* a Queue implementation based on a doubly linked list */
public class Queue<I extends Comparable<I>>{

    public LinkedList<I> queue;
    
    /* constructor for the Queue class */
    public Queue(){
        this.queue = new LinkedList<I>();
    }

    /* enqueue : puts a given piece of data into the back of the queue */
    public void enqueue(I data){
        this.queue.addLast(data);
    }

    /* dequeue : removes and returns a given piece of data from the front of queue */
    public I dequeue(){
        return this.queue.removeFirst();
    }

    /* size : returns the size of the queue */
    public int size(){
        return this.queue.size();
    }

    /* isEmpty : returns true if it is empty, and false if it is not */
    public boolean isEmpty(){
        return this.queue.isEmpty();
    }
    
    /* getFront : returns the data at the front of the queue */
    public I getFront(){
        return this.queue.getFirst();
    }
    
    /* getRear : returns the data at the back of the queue */
    public I getRear(){
        return this.queue.getLast();
    }


}

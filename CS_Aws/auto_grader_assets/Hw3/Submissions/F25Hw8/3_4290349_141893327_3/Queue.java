public class Queue {

    private LinkedList list;

    /**
     * This method creates a new Linked List for the Queue data structure
     */
    public Queue() {
        list = new LinkedList();
    }

    /**
     * This method checks if the queue is empty by calling getSize() from the linkedList class.
     * Specifically it checks if the getSize() method call results in 0.
     * @return
     */
    public boolean isEmpty() {
        return list.getSize() == 0;
    }

    /**
     * This method returns the size by just calling the getSize() method.
     * @return
     */
    public int size() {
        return list.getSize();
    }

    /**
     * This method allows us to add methods to the queue.
     * It calls the addLast method from the LinkedList class to replicate the enqueue process.
     * @param item
     */
    public void enqueue(Object item) {
        list.addLast(item);
    }

    /**
     * This method removes or dequeues an item from the queue.
     * It uses the removeFirst() method to remove a node from the front of the list
     * To replicate the dequeue process.
     * @return
     */
    public Object dequeue() {
        return list.removeFirst();
    }
}

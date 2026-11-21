
/**
 * Implementing a queue by means of a singly-linked list of nodes.
 * All operations are performed in constant time.
 *
 */
public class Queue {

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

  /** Creates an empty queue. */
  public Queue() {
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
  }

  public void enqueue(Object elem) {
    Node node = new Node(elem);

    if (size == 0)
      head = node; // edge case of a previously empty queue
    else
      tail.next = node; // add node at the tail of the list

    tail = node; // update the reference to the tail node
    size++; // tail always points to the last inserted item.
  }

  public Object front() throws Exception { // # Return the first queue element
    if (size == 0)
      throw new Exception("Queue is empty.");

    return head.data;
  }

  // begin#fragment dequeue
  public Object dequeue() throws Exception {
    if (size == 0)
      throw new Exception("Queue is empty.");
    Object tmp = head.data;
    head = head.next; // head always points to the item that is ready to be dequeued next.
    size--;

    if (size == 0)
      tail = null; // the queue is now empty, why not set head = null also?
    return tmp;
  }
  // end#fragment dequeue

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

  /**
   * Prints information about an operation and the queue.
   * 
   * @param op      operation performed
   * @param element element returned by the operation
   * @return information about the operation performed, the element
   *         returned by the operation and the content of the stack after
   *         the operation.
   */
  public static void status(Queue Q, String op, Object element) {
    System.out.println("---------------------------------");
    System.out.println(op);
    System.out.println("Returned: " + element);
    String emptyStatus;
    if (Q.isEmpty())
      emptyStatus = "empty";
    else
      emptyStatus = "not empty";
    System.out.println("size = " + Q.size() + ", " + emptyStatus);
    System.out.println("Queue: " + Q);
  }

  /**
   * Test program that performs a series of operations on on a queue and
   * prints the operation performed, the returned element and the
   * content of the stack after each operation.
   */
  public static void main(String[] args) {
    Object o;
    Queue A = new Queue();
    status(A, "New empty queue", null);
    try {
      A.enqueue(5);
      status(A, "enqueue(5)", null);
      A.enqueue(3);
      status(A, "enqueue(3)", null);
      A.enqueue(7);
      status(A, "enqueue(7)", null);
      o = A.dequeue();
      status(A, "dequeue()", o);
      A.enqueue(9);
      status(A, "enqueue(9)", null);
      o = A.dequeue();
      status(A, "dequeue()", o);
      o = A.front();
      status(A, "front()", o);

      while (!A.isEmpty()) {
        o = A.dequeue();
        status(A, "dequeue()", o);
      }

    } catch (Exception e) {
      System.out.println(e);
    }
  }

}

class EmptyQueueException extends RuntimeException {
  public EmptyQueueException(String err) {
    super(err);
  }
}

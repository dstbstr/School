// Linked Queue implementation 
public class LinkedQueue {
    private class Node {

      private Object data;
      private Node next;

      
      public Node(Object elem) {
          this.data = elem;
          this.next = null;
      }
  }
  
  protected Node head, tail;  
  protected int size;         

  /** Creates an empty queue. */
  public LinkedQueue() {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }
  
  public int size() {       
    return this.size;
  }
  
  public boolean isEmpty() {      
    if ( (head==null) && (tail==null) || this.size == 0 )
      return true;

    return false;
  }

  public void enqueue(Object elem) {
    Node node = new Node(elem);
    
    if (size == 0)
      head = node; // edge case of a previously empty queue
    else
      tail.next = node;  // add node at the tail of the list
    
    tail = node;          // update the reference to the tail node
    size++;               // tail always points to the last inserted item.
  }
  

  public Object front() throws Exception { 
    if (size == 0)
      throw new Exception("Queue is empty.");

    return head.data;
  }
  
  
  public Object dequeue() throws Exception {
    if (size == 0)
      throw new Exception("Queue is empty.");
    Object tmp = head.data;
    head = head.next;   
    size--;

    if (size == 0)
      tail = null; 
    return tmp;
  }
  

  public String toString() {
    String s = "";
    s += "(";
    if (!isEmpty()) {
      Node p = this.head;
      do {
	s += p.data ;
	if (p != tail)
	  s += ", ";
	p = p.next;
      } while (p != null); 
    }
    s += ")";
    return s;
  }
}

 
  

 




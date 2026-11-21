
//Taken from implementation shown in the lectures, but does not throw errors, only returns null values
public class Queue {

  private class Node {

    private Object data;
    private Node next;

    public Node(Object d, Node n) {
      this.data = d;
      this.next = n;
    }

  }


  Node head, tail;
  int size;

  public Queue() {
    this.head = null;
    this.tail = this.head;
    this.size = 0;
  }

  public int getSize() {
    return this.size;
  }

  public boolean isEmpty() {

    if ((this.head == null && this.tail == null) || this.size == 0) {
      return true;
    } else {
      return false;
    }
    
  }

  public void enqueue(Object data) {
    Node newNode = new Node(data, null);
    if (isEmpty()) {
      this.head = newNode;
    } else {
      tail.next = newNode;
    }
    tail = newNode;
    size++;
  }

  public Object dequeue() {

    if (isEmpty()) {
      return null;
    }
    
    Object data = this.head.data;
    head = head.next;
    size--;

    if (isEmpty()) {
      this.head = null;
    }

    return data;

  }

  public Object front() {
    if (isEmpty()) {
      return null;
    }
    return this.head.data;
  }


}

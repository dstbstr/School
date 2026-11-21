import java.lang.Comparable;
//Warnings are present, the program still works with this suppression removed, just here to make the output cleaner
@SuppressWarnings("unchecked")



public class LinkedList {

  private class Node {

    private Object data;
    private Node next, prev;

    private Node (Object data, Node prev, Node next) {
      this.data = data;
      this.prev = prev;
      this.next = next;
    }

  }

  private Node head;
  private int size;

  //Uses a dummy head node
  public LinkedList() {
    this.head = new Node(null, null, null);
    this.head.next = this.head;
    this.head.prev = this.head;
    this.size = 0;

  }

  public int getSize() {
    return this.size;
  }

  public boolean isEmpty() {
    return (this.size == 0);
  }

  public Object getFirst() {
    if (isEmpty()) return null;
    return this.head.next.data;
  }


  public void addLast(Object o) {

    Node newNode = new Node(o, null, null);
    Node last = this.head.prev;
    last.next = newNode;
    newNode.next = this.head;
    newNode.prev = last;
    this.head.prev = newNode;
    ++this.size;

  }

  public void addFirst(Object o) {

    this.head.next = new Node(o, this.head, this.head.next);
    this.size++;

  }

  public void removeFirst() {

    if (isEmpty()) {
      return;
    }

    if (this.head.next.next == null) {
      this.head.next = null;
    }

    this.head.next = this.head.next.next;
    this.head.next.prev = this.head;
    this.size--;

  }


  //Uses the same logic as provided in lecture
  public void MergeSort() {

    if (this.size == 0) {
      return;
    }

    Queue q = new Queue();
    LinkedList templist;
    LinkedList sublist1, sublist2;
    int count = 0;

    for (Node cur = this.head.next; cur != this.head; cur = cur.next) {
      LinkedList list = new LinkedList();
      list.addFirst(cur.data);
      q.enqueue(list);
    }

    while (q.getSize() > 1) {
      sublist1 = (LinkedList)q.dequeue();
      sublist2 = (LinkedList)q.dequeue();
      templist = merge(sublist1, sublist2);
      q.enqueue(templist);
    }

    LinkedList finalList = (LinkedList)q.dequeue();
    this.head = finalList.head;

  }

  public LinkedList merge(LinkedList list1, LinkedList list2) {

    LinkedList S = new LinkedList();
    Comparable fa, fb;
    Object f1, f2;
    while ((!list1.isEmpty()) && (!list2.isEmpty())) {
      fa = (Comparable)list1.getFirst();
      fb = (Comparable)list2.getFirst();

      if (fa.compareTo(fb) < 0) {
        list1.removeFirst();
        S.addLast(fa);
      } else {
        list2.removeFirst();
        S.addLast(fb);
      }

    }

    while (!list1.isEmpty()) {
      f1 = list1.getFirst();
      S.addLast(f1);
      list1.removeFirst();
    }

    while (!list2.isEmpty()) {
      f2 = list2.getFirst();
      S.addLast(f2);
      list2.removeFirst();
    }

    return S;

  }


  //Taken from previous homework
  public void InsertionSort() {

    if (this.size < 1) {
      return;
    }

    Node FUD = this.head.next;
    Node walker = null;
    Comparable temp = null;

    for (Node lastSorted = this.head.next; lastSorted.next != head; lastSorted = lastSorted.next) {


      walker = lastSorted;
      FUD = lastSorted.next;
      temp = (Comparable) FUD.data;

      while (walker != head && ((Comparable)walker.data).compareTo(temp) > 0) {
        walker.next.data = walker.data;
        walker = walker.prev;
      }
      
      walker.next.data = temp;


    }
  }


  public String toString() {
   
    if (this.size == 0) {
      return "Empty list";
    }

    String result = " | ";
    
    Node cur = this.head.next;
    while (cur != this.head) {
      result += "<- " + cur.data + " -> | ";
      cur = cur.next;
    }
    return result;

  }


  //Iterates through the list, checking to see if the value behind the current is less
  //If not, the count is off and the function returns 0
  //As long as count matches the list, returns true
  public boolean isSorted() {

    boolean sorted = false;
    int count = 0;

    Node prev, cur;
    cur = this.head.next;

    for(int i = 0; i < this.size-1; i++) {
      prev = cur;
      cur = cur.next;
      if (((Comparable)cur.data).compareTo(prev.data) < 0) count--;
      count++;

    }
    if (count == this.size - 1) {
      sorted = true;
    }

    return sorted;
  }

  
}

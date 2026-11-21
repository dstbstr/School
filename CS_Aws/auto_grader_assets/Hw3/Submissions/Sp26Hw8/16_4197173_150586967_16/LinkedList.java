public class LinkedList {
  private class Node {
    int data;
    Node next;

    Node(int data) {
      this.data = data;
      this.next = null;
    }
  }

  private Node head;
  private Node tail;
  private int size;

  public LinkedList() {
    head = null;
    tail = null;
    size = 0;
  }

  public void addFirst(int data) {
    Node newNode = new Node(data);

    if (head == null) {
      head = newNode;
      tail = newNode;
    } else {
      newNode.next = head;
      head = newNode;
    }

    size++;
  }

  public void addLast(int data) {
    Node newNode = new Node(data);

    if (head == null) {
      head = newNode;
      tail = newNode;
    } else {
      tail.next = newNode;
      tail = newNode;
    }

    size++;
  }

  public int getFirst() {
    if (head == null) {
      throw new RuntimeException("List is empty");
    }

    return head.data;
  }

  public int removeFirst() {
    if (head == null) {
      throw new RuntimeException("List is empty");
    }

    int value = head.data;
    head = head.next;
    size--;

    if (head == null) {
      tail = null;
    }

    return value;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int getSize() {
    return size;
  }

  public boolean isSorted() {
    if (head == null || head.next == null) {
      return true;
    }

    Node current = head;

    while (current.next != null) {
      if (current.data > current.next.data) {
        return false;
      }

      current = current.next;
    }

    return true;
  }

  public void MergeSort() {
    if (size <= 1) {
      return;
    }

    Queue q = new Queue();

    Node current = head;

    while (current != null) {
      LinkedList newList = new LinkedList();
      newList.addFirst(current.data);
      q.enqueue(newList);
      current = current.next;
    }

    while (q.getSize() > 1) {
      LinkedList sublist1 = q.dequeue();
      LinkedList sublist2 = q.dequeue();

      LinkedList tempList = merge(sublist1, sublist2);

      q.enqueue(tempList);
    }

    LinkedList sortedList = q.dequeue();

    this.head = sortedList.head;
    this.tail = sortedList.tail;
    this.size = sortedList.size;
  }

  private LinkedList merge(LinkedList A, LinkedList B) {
    LinkedList S = new LinkedList();

    while (!A.isEmpty() && !B.isEmpty()) {
      int fa = A.getFirst();
      int fb = B.getFirst();

      if (fa < fb) {
        A.removeFirst();
        S.addLast(fa);
      } else {
        B.removeFirst();
        S.addLast(fb);
      }
    }

    while (!A.isEmpty()) {
      int fa = A.getFirst();
      S.addLast(fa);
      A.removeFirst();
    }

    while (!B.isEmpty()) {
      int fb = B.getFirst();
      S.addLast(fb);
      B.removeFirst();
    }

    return S;
  }

  public void InsertionSort() {
    LinkedList sortedList = new LinkedList();

    while (!this.isEmpty()) {
      int value = this.removeFirst();
      sortedList.addOrdered(value);
    }

    this.head = sortedList.head;
    this.tail = sortedList.tail;
    this.size = sortedList.size;
  }

  private void addOrdered(int data) {
    Node newNode = new Node(data);

    if (head == null) {
      head = newNode;
      tail = newNode;
    } else if (data <= head.data) {
      newNode.next = head;
      head = newNode;
    } else {
      Node current = head;

      while (current.next != null && current.next.data < data) {
        current = current.next;
      }

      newNode.next = current.next;
      current.next = newNode;

      if (newNode.next == null) {
        tail = newNode;
      }
    }

    size++;
  }
}
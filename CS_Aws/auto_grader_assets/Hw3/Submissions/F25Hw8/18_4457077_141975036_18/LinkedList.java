
public class LinkedList {
    private Node head;
    private int size;
    private Node tail;

    private class Node {
        private Object data;
        private Node next;

        private Node(Object data) {
            this.data = data;
            this.next = null;
        }

    }

    public LinkedList() {
        this.head = new Node(null);
        this.size = 0;
        this.tail = this.head;
    }

    public int getSize() {
        return this.size;
    }

    public void addFirst(Object data) {
        Node nn = new Node(data);
        nn.next = this.head.next;
        this.head.next = nn;
        this.size++;
        if (this.size == 1) {
            this.tail = nn;
        }

    }


    public void addLast(Object data) {
        Node nn = new Node(data);
        this.tail.next = nn;
        this.tail = nn;
        this.size++;
    }

    public Object getFirst() {
        if (this.size == 0) {
            return null;
        }
        Node first = this.head.next;
        return first.data;
    }

    public Object removeFirst() {
        if (this.size == 0) {
            return null;
        }
        Node first = this.head.next;
        this.head.next = first.next;
        if (first == this.tail) {
            this.tail = this.head;
        }
        this.size--;
        return first.data;
    }


    public void MergeSort() {
        if (this.size == 0 || this.size == 1) {
            return;
        }
        Queue fullQueue = new Queue();
        while (this.size != 0) {
            Object data = this.removeFirst();
            LinkedList tempList = new LinkedList();
            tempList.addFirst(data);
            fullQueue.enqueue(tempList);
        }
        while (fullQueue.size() > 1) {
            LinkedList sublist1 = fullQueue.dequeue();
            LinkedList sublist2 = fullQueue.dequeue();
            LinkedList temp = merge(sublist1, sublist2);
            fullQueue.enqueue(temp);
        }
        LinkedList sorted = fullQueue.dequeue();
        if(sorted == null) {
            return;
        }
        this.head.next = sorted.head.next;
        this.tail = sorted.tail;
        this.size = sorted.size;

    }
    public LinkedList merge(LinkedList sublist1, LinkedList sublist2) {
        LinkedList temp = new LinkedList();
        while (sublist1.size != 0 && sublist2.size != 0) {
            Comparable data1 = (Comparable) sublist1.getFirst();
            Comparable data2 = (Comparable) sublist2.getFirst();
            if (data1.compareTo(data2) < 0) {
                temp.addLast(sublist1.removeFirst());
            } else {
                temp.addLast(sublist2.removeFirst());
            }
        }
        while (sublist1.size != 0) {
            temp.addLast(sublist1.removeFirst());
        }
        while (sublist2.size != 0) {
            temp.addLast(sublist2.removeFirst());
        }
        return temp;
    }

    public boolean isSorted() {
        if (this.size == 0) {
            return true;
        }
        if (this.size == 1) {
            return true;
        }
        boolean flag = true;
        Node cur = this.head.next;
        Node nextNode = cur.next;

        while (nextNode != null) {
            Comparable data1 = (Comparable) cur.data;
            Comparable data2 = (Comparable) nextNode.data;
            if (data1.compareTo(data2) > 0) {
                flag = false;
            }
            cur = cur.next;
            nextNode = nextNode.next;
        }
        return flag;
    }

    public void InsertionSort() {
        if (this.size == 0 || this.size == 1) {
            return;
        }
        Node first = this.head.next;
        Node cur = first.next;
        first.next = null;
            while (cur != null) {
                Node nextNode = cur.next;
                if (((Comparable) cur.data).compareTo(first.data) <= 0) {
                    cur.next = first;
                    first = cur;
                } else {
                    Node temp = first;
                    while (temp.next != null && ((Comparable) temp.next.data).compareTo(cur.data) < 0) {
                        temp = temp.next;
                    }
                    cur.next = temp.next;
                    temp.next = cur;
                }
                cur = nextNode;
            }
            this.head.next = first;
            Node tempTail = this.head;
            while (tempTail.next != null) {
                tempTail = tempTail.next;
            }
            this.tail = tempTail;
        }


    }

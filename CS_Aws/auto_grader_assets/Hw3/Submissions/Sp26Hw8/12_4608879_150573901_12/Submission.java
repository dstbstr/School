//name:Jiatong Lyu
import java.util.Random;

public class Tester {
    public static void main(String[] args) {
        LinkedList A = new LinkedList();
        LinkedList A2 = new LinkedList();
        int ramdomListSize = 20000;
        for(int i = 0; i < ramdomListSize; i++) {
            int randomInt = (int)(Math.random() * 3000000);
            A.addLast(randomInt);
            A2.addLast(randomInt);
        }
        //measure the time cost of merge sort
        double then = System.currentTimeMillis();
        A.MergeSort();
        double now = System.currentTimeMillis();
        System.out.println("Time cost in milliseconds for mergesort " + (now - then));
        System.out.println(A.isSorted()); //verify that your merge sort implementation works.
        System.out.println("Size of list A is: " + A.getSize());
        //measure the time cost of insertion sort
        then = System.currentTimeMillis();
        A2.InsertionSort();
        now = System.currentTimeMillis();
        System.out.println("Time cost in milliseconds for insertionsort " + (now - then));
        System.out.println(A2.isSorted() ); //verify that your insertion sort works.
        System.out.println("Size of list A2 is: " + A2.getSize());


    }//end of main
}//end of tester class


class LinkedList {

    private class Node {
        private Object data;
        private Node next, prev;

        private Node(Object data, Node pref, Node next) {
            this.data = data;
            this.prev = pref;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public LinkedList() {
        this.head = new Node(null, null, null);
        this.head.next = this.head;
        this.head.prev = this.head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == this.head.next;
    }

    public Object getFirst() {
        if (isEmpty()) {
            return null;
        }
        return head.next.data;
    }

    public Object removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node first = head.next;
        Object data = first.data;
        head.next = first.next;
        first.next.prev = head;
        size--;
        return data;
    }

    public int getSize() {
        return this.size;
    }

    public void addFirst(Object data) {
        Node nn = new Node(data, this.head, this.head.next);
        this.head.next.prev = nn;
        this.head.next = nn;
        this.size++;
    }

    public void addLast(Object data) {
        Node nn = new Node(data, this.head.prev, this.head);
        if (head.next == head) {

            nn.next = head;
            nn.prev = head;
            head.next = nn;
            head.prev = nn;
        } else {
            Node last = head.prev;
            nn.prev = last;
            nn.next = head;
            last.next = nn;
            head.prev = nn;
        }
        size++;


    }

    public LinkedList subListOfSmallerValues(Comparable data) {
        LinkedList list = new LinkedList();
        LinkedList result = new LinkedList();
        Node cur = head.next;
        while (cur != head) {
            if (cur.data != null && ((Comparable) cur.data).compareTo(data) < 0) {
                result.addLast(cur.data);
            }
            cur = cur.next;
        }
        return result;
    }

    public boolean removeStartingAtBack(Object dataToRemove) {
        Node cur = head.prev;
        while (cur != head) {
            if ((cur.data != null && cur.data.equals(dataToRemove)) || (cur.data == null && dataToRemove == null)) {
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                if (cur == head.prev) {
                    head.next = cur.next;
                }
                if (cur == head.next) {
                    head.prev = cur.prev;
                }
                size--;
                return true;
            }
            cur = cur.prev;
        }
        return false;
    }

    public int lastIndexOf(Object o) {
        int index = 0;
        int lastIndex = -1;
        Node cur = head.next;
        while (cur != head) {
            if ((o == null && cur.data == null) || (o != null && o.equals(cur.data))) {
                lastIndex = index;
            }
            cur = cur.next;
            index++;
        }
        return lastIndex;
    }

    public boolean retainAll(LinkedList other) throws NullPointerException {
        if (other == null)
            throw new NullPointerException();
        boolean modified = false;
        Node cur = head.next;
        while (cur != head) {
            Node next = cur.next;
            boolean found = false;
            Node o = other.head.next;
            while (o != other.head) {
                if ((cur.data == null && o.data == null) || (cur.data != null && cur.data.equals(o.data))) {
                    found = true;
                    break;
                }
                o = o.next;
            }
            if (!found) {
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                if (cur == head.next)
                    head.next = cur.next;
                if (cur == head.prev)
                    head.prev = cur.prev;
                size--;
                modified = true;
            }
            cur = next;
        }
        return modified;
    }

    public void InsertionSort() {
        if (head.next == head || head.next.next == head) {
            return;
        }
        Node cur = head.next.next;
        while (cur != head) {
            Node next = cur.next;
            Object key = cur.data;
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            if (cur == head.prev) {
                head.prev = cur.prev;
            }
            Node pos = head.next;
            while (pos != head && ((Comparable) pos.data).compareTo(key) < 0) {
                pos = pos.next;
            }
            Node before = pos.prev;
            before.next = cur;
            cur.prev = before;
            cur.next = pos;
            pos.prev = cur;
            cur = next;
        }
    }

    private LinkedList merge(LinkedList A,LinkedList B){
        LinkedList S=new LinkedList();
        while(!A.isEmpty()&& !B.isEmpty()){
            Comparable fa=(Comparable)A.getFirst();
            Comparable fb=(Comparable)B.getFirst();
            if(fa.compareTo(fb)<0){
                S.addLast(A.removeFirst());
            }
            else{
                S.addLast(B.removeFirst());
            }
        }
        while(!A.isEmpty()){
            S.addLast(A.
                    removeFirst());
        }
        while(!B.isEmpty()){
            S.addLast(B.removeFirst());
        }
        return S;
    }
    public void MergeSort(){
        if(this.size<=1){
            return;
        }
        Queue q=new Queue();
        Node cur=this.head.next;
        while(cur!=this.head){
            LinkedList newList=new LinkedList();
            newList.addFirst(cur.data);
            q.enqueue(newList);
            cur=cur.next;
        }
        while(q.size()>1){
            LinkedList sublist1= (LinkedList) q.dequeue();
            LinkedList sublist2=(LinkedList) q.dequeue();
            LinkedList tempList=merge(sublist1, sublist2);
            q.enqueue(tempList);
        }
        LinkedList sortedList=(LinkedList) q.dequeue();
        this.head=sortedList.head;
        this.size=sortedList.size;
    }
    public boolean isSorted(){
        if(this.size<=1){
            return true;
        }
        Node cur=this.head.next;
        while(cur.next!=this.head){
            Comparable current=(Comparable) cur.data;
            Comparable next=(Comparable) cur.next.data;
            if(current.compareTo(next)>0){
                return false;
            }
            cur=cur.next;
        }
        return true;
    }
}
class Queue{
    private class Node{
        private Object data;
        private Node next;
        public Node(Object elem){
            this.data=elem;
            this.next=null;
        }
    }
    private Node head, tail;
    private int size;
    public Queue(){
        this.head=null;
        this.tail=null;
        this.size=0;
    }
    public int size(){
        return this.size;
    }
    public boolean isEmpty(){
        return this.size == 0;
    }
    public void enqueue(Object elem){
        Node node=new Node(elem);
        if(size==0)
            head=node;
        else tail.next=node;
        tail=node;
        size++;
    }
    public Object dequeue(){
        if(size==0)
            return null;
        Object tmp=head.data;
        head=head.next;
        size--;
        if (size==0)
            tail=null;
        return tmp;
    }
}
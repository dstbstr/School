import javax.sound.sampled.Line;
import java.util.NoSuchElementException;

public class LinkedList {
    private class Node {
        private Object data;   //Assume data implemented Comparable
        private Node next, prev;
        private Node(Object data, Node pref, Node next)
        {
            this.data = data;
            this.prev = pref;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public LinkedList() {
        this.head = new Node(null, null, null );
        this.head.next = this.head;
        this.head.prev=this.head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == this.head.next;
    }

    @Override
    public String toString() {
        String result = "{";
        for (Node node = this.head.next; node != this.head; node = node.next) {
            if(node.next != this.head)
                result += node.data + "->";
            else
                result += node.data;
        }
        return result + "}";
    }

    public void removeFirst() throws Exception{
        // We need to change the head.next variable to cur.next
        // we need to change the cur.next prev to head
        if(this.size == 0){
            throw new NoSuchElementException();
        }
        Node prev = this.head;
        Node cur = this.head.next;

        prev.next = cur.next; //head points to cur.next
        cur.next.prev = prev;
        this.size--;
    } //Make sure this works

    public void InsertionSort() {
        Node lastSorted, sortedWalker;
        Comparable firstUnsortedData;
        for (lastSorted = head.next; lastSorted != head.prev; lastSorted = lastSorted.next){
            firstUnsortedData = (Comparable) lastSorted.next.data;
            for(sortedWalker = lastSorted; sortedWalker != head && firstUnsortedData.compareTo(sortedWalker.data) < 0; sortedWalker = sortedWalker.prev){
                sortedWalker.next.data = sortedWalker.data;
            }
            sortedWalker.next.data = firstUnsortedData;
        }
    }


    public void addFirst(Object data) {
        Node nn = new Node(data, this.head, this.head.next);
        this.head.next.prev = nn;
        this.head.next = nn;
        this.size ++;
    }


    public LinkedList Merge(LinkedList A, LinkedList B) throws Exception {

        LinkedList result = new LinkedList();

        Node aCur = A.head.next;
        Node bCur = B.head.next;

        while (!A.isEmpty() && !B.isEmpty()){ //both of these have to be true

            if (((Comparable) aCur.data).compareTo((Comparable) bCur.data) > 0){
                //if A is bigger than B
                result.addLast(bCur.data);
                bCur = bCur.next;
                B.removeFirst();
            }else{
                //if B is bigger than A
                result.addLast(aCur.data);
                aCur = aCur.next;
                A.removeFirst();
            }
        }
        while (!A.isEmpty()){ //If B ran out of data then run this because A still has some
            result.addLast(aCur.data);
            A.removeFirst();
        }
        while (!B.isEmpty()){ //If A ran out of data then run this because B still has some
            result.addLast(bCur.data);
            B.removeFirst();
        }
        return result;
    }


    public void MergeSort() throws Exception {

        // So this method turns this list into single node list objects
        // it puts these list objects into a queue
        // Then it dequeues them 2 at a time and uses merge() on this list to sort it
        // then it puts this sorted list in the queue again and grabs the next 2 to be sorted
        // it dequeues these 2 and sorts them and then put them back into the queue
        // then it grabs the two sorted lists and merges these so they are sorted
        // by the end you will have just one big sorted list to return
        // then I think you just set this.head equal to the new lists head or head.next or something

        Node cur = this.head.next;
        Queue q = new Queue();
        for (int i = 0; i < this.size; i++) {
            LinkedList newList = new LinkedList();
            newList.addFirst(cur.data); //Add the first element to this list
            q.enqueue(newList); // Put this list into a queue
            cur = cur.next;
        }
        while (q.size > 1) {
            LinkedList sublist1 = (LinkedList) q.dequeue();
            LinkedList sublist2 = (LinkedList) q.dequeue();
            LinkedList temp = Merge(sublist1, sublist2);
            q.enqueue(temp);
        }

        LinkedList sortedList = (LinkedList) q.dequeue();
        this.head = sortedList.head;
    }

    public boolean isSorted(){

        // So I need a cur that starts at the first real node, I then need a next that can tell me the data stored
        // within itself, I then need to check to see if next.data is greater than cur.data
        // I then need to move both of these to the next node

        Node cur = head.next;
        Node next = cur.next;
        boolean sorted = false;

        while (next != head){
            if (((Comparable) next.data).compareTo(cur.data) >= 0){
                cur = cur.next;
                next = next.next;
                sorted = true;
            }else{
                sorted = false;
                break;
            }
        }
        return sorted;
    }

    public void addLast(Object data) {
        if (data == null) {
            Node nn = new Node("null", this.head.prev, this.head);
            this.head.prev.next = nn;
            this.head.prev = nn;
            this.size ++;
        }else{
            Node nn = new Node(data, this.head.prev, this.head);
            this.head.prev.next = nn;
            this.head.prev = nn;
            this.size ++;
        }
    }

    public int getSize(){
        return this.size;
    }


}

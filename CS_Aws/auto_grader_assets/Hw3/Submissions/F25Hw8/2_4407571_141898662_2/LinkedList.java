// This linked list class stores integers using nodes
// we use add and remove methods to let us add and remove elements
// also we should use the method to show us if the list is sorted
// also this linked List can sort itslf using merge sort and insertion sort
// for this homeword I am going to use singly Linked list to do the merge Sort
    // insertion sort and isSorted() verification in O(n) TC
public class LinkedList {

    private static class Node {
        // each node stores one value called data
        int data;
        // each node hold a reference to the next node in the list
        Node next;
        // we create the constructor here set data to the given value
        // and set the next to null becasue the its next pointer is not established yet
        Node(int d) {
            data = d;
            next = null;
        }
    }
    // this is the first element in the list
    private Node head;
    // this reference to the last node we use this so the addLast() is fast and does not need traversal
    private Node tail;
    // this will stores the count of elements currently in the list
    private int size;

    // we create our constructor we create an empty list becasue no nodes exists yet
    // so assign head and tail to null and size to zero
    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }



   // we add out addfirst inserts at the front of the constant time
    // it takes an integer data value
    public void addFirst(int value) {
        // first create a new node that stores our value
        Node nn = new Node(value);
        // make sure to linke the new node before the current head
        // if the list is empty this still work
        nn.next = head;
        // we update the list's head so it points to the new first node
        head = nn;

        // here is tricky we make sure to assinge tail to null it says if our
        // list was empty , both head and tail should point to the same node only
        if (tail == null)
            // this fixes the tail pointer when inserting into an empty list
            tail = nn;
     // increase the size count by 1
        size++;
    }



    public void addLast(int value) {
        // we create a new node that stores the given value
        Node nn = new Node(value);
        // we do not need to intialize the next and prev pointers because they
        // are already initialized in the constructor
        //check whether the list is empty
        if (head == null) {
            // if head is null there are no nodes in the list
            // so both head and tail should poit to the only node
            head = tail = nn;

        } else {
            // otherwise the list already has at least one element
            tail.next = nn;
            // so the current taul node should link to the new node
            tail = nn;
            // and update the tail pointer so the new nodes becomes the last
            // node in the list
        }
        // increase the size of the list element count by one
        size++;
    }


   // we define the getFirst which will return the first value in the list
    public int getFirst() {
        // returns the integer stored in the head node this assuse that the node
        // is not empty
        return head.data;
    }

   // this is the method that remove the first node and returns its value
    public int removeFirst() {
        // if the list is empty return -1 which indicate error
        if (head == null) return -1;
        // first we need to store the value of the node before removing it
        int val = head.data;
        // move the head pointer to the next node
        head = head.next;
        // it makes the old head removed
        // decrese the size
        size--;
        // if removing the node made the list empty
        if (size == 0)
            // so assign the tail to null as well making sure head and tail matcehs
            tail = null;

        return val;
    }
    //this method returns true if our list has  no elements in it
    public boolean isEmpty() { return size == 0; }

    // this method returns the number of elements in the list
    public int getSize() { return size; }



    // this method check if the list is sorted in ascending order
    public boolean isSorted() {
        // if the list has 0 or 1 nodes it is automatically sorted
        if (head == null || head.next == null) return true;
        // first start with the first node

        Node cur = head;
        // make sure to store the data of first node
        int prev = cur.data;
        // move to the secound node
        cur = cur.next;
      // loop through the list until we reach the end
        while (cur != null) {
            // if any earlier value is greater than the current value the
            // the list is not sorted
            if (prev > cur.data) return false;
            // update prev to the current value
            prev = cur.data;
            // move to the next node
            cur = cur.next;
        }
        //  if the loop finishes wihtout the finding a violation the list is roted
        return true;
    }




    // by performing the merge sort we can convert each node into a single elment
    // linked list enqueve it , while queue contains more than one list
    // dequeue tow list merge them enqueue results and replace the list with the final sorted
    public void MergeSort() {
      // we do the same thing for the MergeSort , if the list has 0 or 1 element
        // it is already sorted
        if (size <= 1) return;
        // first we create a queue that will store many small linked list
        // during the merge sort process

        Queue q = new Queue();
       // first we need to convert each node into a one element list and
        // enqueue it

        Node cur = head;
        // start at the first node of the list
        while (cur != null) {
            // loop through every node in the original list
            LinkedList single = new LinkedList();
            // create and empty linked list that will hold one element
            single.addFirst(cur.data);
            // insert the current node data into the new single elemnt list
            q.enqueue(single);
            // put the single element list into queue
            cur = cur.next;
            // and move the next node in the original list
        }

       // here we keep merging pairs of list until only one list until one list remain
        while (q.size() > 1) {
            // as long as more than one list exists in the queue keep merging
            LinkedList A = q.dequeue();
            // take the first list out
            LinkedList B = q.dequeue();
            // take the secound list out
            LinkedList merged = merge(A, B);
            // here we combine the two sorted list into one larger sorted list
            q.enqueue(merged);

        }

        // when the loop finishes the queue contians exactly one fully sorted list
        LinkedList sorted = q.dequeue();
        // copy the sorted list properties into the current list
        // this effectively replace the old list with the sorted version
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }


    // we create the helper method for the sorted linkelist
    // merge two sorted linkedList into a single list
    private static LinkedList merge(LinkedList A, LinkedList B) {
        // we first create a new empty result list
        LinkedList S = new LinkedList();
        // we continue while neither list is fully consumed

        while (!A.isEmpty() && !B.isEmpty()) {
            // look at the front of each list if A's value is smaller
            // then remove it from a and append it to the result list S
            int fa = A.getFirst();
            int fb = B.getFirst();
            // otherwise if B's value is smaller or equal
            // remove from b and append it to S
            if (fa < fb) {
                A.removeFirst();
                S.addLast(fa);
            } else {
                B.removeFirst();
                S.addLast(fb);
            }
        }
        // if A still has items copy them all

        while (!A.isEmpty()) S.addLast(A.removeFirst());
        // if B still has item copy them all
        while (!B.isEmpty()) S.addLast(B.removeFirst());
        // return the fully merged sorted list
        return S;
    }



    public void InsertionSort() {
        // we do the same thing here is the list has 0 or 1 item , it is already
        // sorted

        if (size <= 1) return;
        // first we create a seprate empty list that will gradually become sorted

        LinkedList sorted = new LinkedList();

        // Remove from this list and insert into sorted one-by-one
        while (!this.isEmpty()) {
            // we continue until the original list is empty
            int value = this.removeFirst();
            // take the first element from the old list
            insertOrdered(sorted, value);
            // insert it into the new sorted lsit in the correct palce
        }

        // Replace contents
        // now the original list becomes the sorted list
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }


    // here we create the helper method that is used by the insertion sort
    private void insertOrdered(LinkedList list, int value) {
        // if the list is empty or value is samaller than the first value
        // insert it at the front and return

        if (list.head == null || value < list.head.data) {
            list.addFirst(value);
            return;
        }
        // start from the first node

        Node cur = list.head;
        // keep moving forward as long as there is a next node
        // and the next node's value is still less than  the value we want to
        // insert

        while (cur.next != null && cur.next.data < value) {
            cur = cur.next;
        }
        // here we create a new node to the node after cur

        Node nn = new Node(value);
        // link the new node to the node after cur
        nn.next = cur.next;
        // link the cur to the new node
        cur.next = nn;
        // if inserted at the very end update the tail

        if (nn.next == null)
            list.tail = nn;
       // increase the size
        list.size++;
    }
}


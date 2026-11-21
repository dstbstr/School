public class CircularDoublyLinkedList {
    //nested Node class
    public static class Node {
        protected Object data;
        protected Node next;
        protected Node prev;

        public Node() {
            this(null, null, null);
        }

        // Keep this only if you really need it, but remove the head references
        public Node(Object data) {
            this(data, null, null);
        }

        public Node(Object data, Node prev, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }//End of Node class

    protected Node head;
    protected int size;

    public CircularDoublyLinkedList(){

        this.head = new Node(null,null,null);
        this.size = 0;
        this.head.prev = this.head;
        this.head.next = this.head;
    }// end default LinkedList constructor

    public int size(){
        return this.size;
    }// end size method


    public void addFirst(Object data){
        Node NN = new Node(data, this.head, this.head.next);
        NN.next.prev = NN;
        NN.prev.next = NN;
        this.size++;
    }
    public void removeFirst() {
        if (size == 0) return;
        Node first = this.head.next;
        this.head.next = first.next;
        first.next.prev = this.head;
        size--;
    }
    public void addLast(Object data){
        Node NN = new Node(data,this.head.prev, this.head);
        this.head.prev.next = NN;
        this.head.prev = NN;
        this.size++;
    }
    public int getSize(){
        return this.size;
    }
    public boolean remove(int index){
        if(index < 0 || index >= size){ return false;}

        Node cur = this.head.next;
        int i = 0;
        while(i<index && cur != this.head){//Dont need this second check for this.head
            cur = cur.next;
            i++;
        }
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        size--;
        return true;
    }
    //Works with 1 node and empty and repeats
    public void InsertionSort(){
       for(Node EOFS = this.head.next.next; EOFS != this.head.next; EOFS = EOFS.next) {
          Node FUD = EOFS.prev;
          //Remove FUD from list
           FUD.prev.next = FUD.next;
           FUD.next.prev = FUD.prev;
          //Insert FUD into unsorted section of list starting at EOFS and going to the head backwards
           Node cur = FUD.prev;
           while(cur != this.head && FUD.data !=null &&cur.data !=null && ((Comparable)FUD.data).compareTo((Comparable)cur.data) < 0){
               cur = cur.prev;
           }
           //Insert FUD to the right of cur
           Node NN = new Node(FUD.data, cur,cur.next);
           cur.next.prev = NN;
           cur.next = NN;
       }
    }
    /*

3, dequeue your sorted linked list. Here think about how to make this list sorted, because the
dequeued list and this list are different object. Hint: we learned this technique when we used
addOrdered() method to sort an linked list.
     */
    public void MergeSort(){
        Queue q = new Queue();
        //Adds each item in the LL to the Queue as seperate LL
        for(Node cur = this.head.next; cur != this.head; cur = cur.next){
            CircularDoublyLinkedList NL = new CircularDoublyLinkedList();
            NL.addFirst(cur.data);
            q.enqueue(NL);
        }
        //While there is more than one item in the Queue
        while(q.getSize() > 1){
            //dequeue and assign to a LinkedList reference sublist1, (already sorted)
            CircularDoublyLinkedList sublist1 = (CircularDoublyLinkedList)q.dequeue();
            CircularDoublyLinkedList sublist2 = (CircularDoublyLinkedList)q.dequeue();
            //Merge them
            CircularDoublyLinkedList tempList = merge(sublist1, sublist2 );
            q.enqueue(tempList);
        }
        CircularDoublyLinkedList temp = q.getList();
        this.head = temp.head;

    }
    public static CircularDoublyLinkedList merge(CircularDoublyLinkedList A, CircularDoublyLinkedList B) {
        //create new LinkedList C and pointers fb and fa
        CircularDoublyLinkedList C = new CircularDoublyLinkedList();
        if(A.size != 0 && B.size != 0) {
            Node fa = A.head.next, fb = B.head.next;
            //While loop that walks through the list until one is empty
            while (A.size > 0 && B.size > 0) {
                //If fa is less than fb add to list C and remove from list else
                if (fa.data != null && fb.data != null && ((Comparable) fa.data).compareTo((Comparable) fb.data) < 0) {
                    C.addLast(fa.data);
                    A.removeFirst();
                } else {
                    C.addLast(fb.data);
                    B.removeFirst();
                }
                fa = A.head.next;//Aren't moveing them to the next pos in list just updating the LL to the new
                fb = B.head.next;//first element because there is a dummy head.

            }
        }
        //Once loop exits if one of the list still has items add to end.
        while (A.size != 0) {
            C.addLast(A.head.next.data);
            A.removeFirst();
        }
        while (B.size != 0) {

            C.addLast(B.head.next.data);
            B.removeFirst();
        }
        return C;

    }
    public boolean isSorted(){
        //Special case empty or 1 item
        if(this.size == 0){
            System.out.println("List is empty");
            return false;
        }else if(this.size == 1){
            return true;
        }
        //Loop through LL making sure each value is either equal to or greater than the one before it.
        for(Node cur = this.head.next.next,prev = this.head.next; cur != this.head; prev = cur, cur = cur.next){
            if(cur.data != null && prev.data != null &&((Comparable)cur.data).compareTo((Comparable)prev.data) < 0){
                return false;
            }
        }
        return true;
    }
    @Override
    public String toString(){

        String result = "";

        result += "List size is: " + this.size + "\r\n";

        for (Node cur = this.head.next; cur != this.head; cur = cur.next){
            result += cur.data + "->";
        }//end for

        return result;

    }// end toString method
}
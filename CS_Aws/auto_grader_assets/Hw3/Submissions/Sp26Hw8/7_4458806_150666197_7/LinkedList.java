

public class LinkedList<I extends Comparable<I>> implements Comparable<LinkedList<I>>{

    public class Node{
        public Node next;
        public Node prev;
        public I data;
        
        public Node(I data, Node next, Node prev){
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public Node(I data){
            this(data, null, null);
        }

        public Node(){
            this(null, null, null);
        }
    }

    public int size;
    public Node head;
    
    /* constructor for the LinkedList class */
    public LinkedList(){
        this.head = new Node();
        this.head.next = this.head;
        this.head.prev = this.head;
        this.size = 0;
    }

    /* addFirst : adds the data to the front of the list */
    public void addFirst(I data){
        Node newNode = new Node(data);
        newNode.next = this.head.next;
        newNode.prev = this.head;
        this.head.next.prev = newNode;
        this.head.next = newNode;

        this.size ++;
    }
    
    /* addLast : adds the data to the back of the list  */
    public void addLast(I data){
        Node newNode = new Node(data);
        newNode.next = this.head;
        newNode.prev = this.head.prev;
        this.head.prev.next = newNode;
        this.head.prev = newNode;
        
        this.size ++;
    }

    /* addAt : adds the data at the given index in the list */
    public void addAt(int index, I data){
        /* index checking */
        if( index < 0 || index > this.size())
            throw new IllegalArgumentException("Index " + index + " out of bounds for list of size " + this.size());

        Node cur = this.head;
        for(int i = 0; i < index; i ++)
            cur = cur.next; /* moves cur to right before the given index */

        Node newNode = new Node(data);
        newNode.next = cur.next;
        newNode.prev = cur;

        cur.next.prev = newNode;
        cur.next = newNode;

        this.size ++;
    }

    /* getLast : returns the last element of the list */
    public I getLast(){
        if(this.isEmpty())
            throw new RuntimeException("Cannot get last on an empty list");
        return this.head.prev.data;
    }

    /* getFirst : returns the first element of the list */
    public I getFirst(){
        if(this.isEmpty())
            throw new RuntimeException("Cannot get first on an empty list");
        return this.head.next.data;
    }

    /* getAt : gets the data at the given index in the list */
    public I getAt(int index){
        /* index checking */
        if( index < 0 || index >= this.size())
            throw new IllegalArgumentException("Index " + index + " out of bounds for list of size " + this.size());
        
        Node cur = this.head;
        for(int i = 0; i <= index; i++)
            cur = cur.next; /* moves cur to the given index */
        
        return cur.data;
    }

    /* removeFirst : removes the first element in the list and returns it */
    public I removeFirst(){
        /* checks if the list is empty */
        if(this.isEmpty())
            throw new RuntimeException("Cannot remove from an empty list");
        
        I ret = this.head.next.data;

        this.head.next.next.prev = this.head;
        this.head.next = this.head.next.next;

        this.size --;
        return ret;
    }

    /* removeLast : removes the last element in the list and returns it */
    public I removeLast(){
        /* checks if the list is empty */
        if(this.isEmpty())
            throw new RuntimeException("Cannot remove from an empty list");
        
        I ret = this.head.prev.data;

        this.head.prev.prev.next = this.head;
        this.head.prev = this.head.prev.prev;

        this.size --;
        return ret;
    }

    /* removeAt : removes the element at the given index and returns it */
    public I removeAt(int index){
        /* checks if the list is empty */
        if(this.isEmpty())
            throw new RuntimeException("Cannot remove from an empty list");
        if(index < 0 || index >= this.size())
            throw new IllegalArgumentException("Index " + index + " out of bounds for list of size " + this.size());

        I ret;
        Node cur = this.head;

        for(int i = 0; i < index; i++)
            cur = cur.next; /* moves cur right before the given index */

        ret = cur.next.data;
        cur.next.next.prev = cur;
        cur.next = cur.next.next;

        this.size --;
        return ret;
    }

    /* size : returns the size of the linked list */
    public int size(){
        return this.size;
    }

    /* isEmpty : returns true if the linked list is empty, otherwise returns false */
    public boolean isEmpty(){
        return this.size() == 0;
    }
    
    /* MergeSort : preforms a merge sort on this list */
    public void MergeSort(){
        if(this.size <= 1)
            return;
        Node cur = this.head.next;
        Queue<LinkedList<I>> queue = new Queue<LinkedList<I>>(); 
        /* puts all elements in the queue */
        for(int i = 0; i < this.size(); i ++){
            LinkedList<I> newList = new LinkedList<I>();
            newList.addFirst(cur.data);
            queue.enqueue(newList);
            cur = cur.next;
        }
    
        /* goes throught the queue untill the only item left is a sinle linked list with all the items sorted */
        while( queue.size() > 1){
            LinkedList<I> subList1 = queue.dequeue();
            LinkedList<I> subList2 = queue.dequeue();
            LinkedList<I> tempList = merge(subList1, subList2);
            queue.enqueue(tempList);
        }

        /* makes this list the sorted list */
        LinkedList<I> sortedList = queue.dequeue();
        this.head.next = sortedList.head.next;
        this.head.prev = sortedList.head.prev;
        this.size = sortedList.size();

    }
    
    /* merge : merges two sorted linked lists into one sorted linked list */
    private LinkedList<I> merge(LinkedList<I> A, LinkedList<I> B){
        I fa;
        I fb;
        LinkedList<I> S = new LinkedList<I>();
        while( !A.isEmpty() && !B.isEmpty()) {
            fa =  A.getFirst();
            fb = B.getFirst();
            if(fa.compareTo(fb) < 0){
                A.removeFirst();
                S.addLast(fa);
            } else{
                B.removeFirst();
                S.addLast(fb);
            }
        }
        
        while( !A.isEmpty()){
            fa = A.getFirst();
            S.addLast(fa);
            A.removeFirst();
        }
        while( !B.isEmpty()){
            fb = B.getFirst();
            S.addLast(fb);
            B.removeFirst();
        }

        return S;
    }

    /* isSorted : returns true if the list is sorted, and false if it is not */
    public boolean isSorted(){
        if(this.size() <= 1){
            return true;
        }

        Node prev = this.head.next;
        Node cur = prev.next;
    

        for(int i = 1; i < this.size(); i++){
            if( prev.data.compareTo(cur.data) > 0 ){
                return false;
            }
            
            cur = cur.next;
            prev = prev.next;
        }

        return true;
    }

    /* InsertionSort : sorts this linked list using the insertion sort algorithm */
    public void InsertionSort(){
        if(this.size <= 1)
            return;

        Node FUN; /* First unsorted node */
        FUN = this.head.next.next;

        for(int i = 1; i < this.size(); i ++ ){
            this.addToSorted(i, FUN.data);
            this.removeAt( i + 1);
            FUN = FUN.next;
        }


    }

    /* addToSorted : adds element into the sorted section of an insertion sort, sorted */
    private void addToSorted(int indexOfFirstUnsortedElement, I data){
        Node cur = this.head;
        
        for(int i = 0; i < indexOfFirstUnsortedElement; i++){
            if(cur.next.data.compareTo(data) >= 0){
                break;
            }
            cur = cur.next;
        }

        Node newNode = new Node(data);
        cur.next.prev = newNode;
        newNode.next = cur.next;
        newNode.prev = cur;
        cur.next = newNode;
        
        this.size ++;
    }
    
    @Override
    public int compareTo(LinkedList<I> other){
        return this.size - other.size;
    }
}



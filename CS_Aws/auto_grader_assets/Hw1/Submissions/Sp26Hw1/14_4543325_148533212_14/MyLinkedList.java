
public class MyLinkedList {
    private ListNode head;
    private int size;

    //inner class for ListNode
    private class ListNode {
        private Object data;
        private ListNode next;

        public ListNode(Object d) {
            this.data = d;
            this.next = null;
        }
    }

    public MyLinkedList() {
        this.head = new ListNode(null); //with a dummy head node
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * @author Spencer Olson
     * @param e is the data passed through for the data of the new node
     * addFirst() method will add a node element with 'Object e' at the front of a list
     */
    public void addFirst(Object e)
    {
        ListNode node = new ListNode(e);
        node.next = head.next;
        head.next = node;
        size++;
    }

    /**
     * @author Spencer Olson
     * @return The return object 'temp' returns a temporary placeholder for a removed node
     * @throws IllegalArgumentException IllegalArgumentException is thrown when the size of the list would be impossible to remove an element
     * removeFirst() method removes the first node in the list
     */
    public Object removeFirst() throws IllegalArgumentException {
        if(size <= 0){ //Validation
            throw new IllegalArgumentException("Exception: LinkedList is empty!");
        }

        Object temp = head.next.data; //Creates an object that holds the data stored in the node succeeding the dummy head node
        head.next = head.next.next; // Sets the node after head to the node succeeding the first node that was removed
        size--; //Decrements size
        return temp; //Returns the data stored in the temporary object
    }

    /**
     * @author Spencer Olson
     * @param o Object o is the data that the method will check if it exists in the list
     * @return False will be returned by default unless the conditional through the for loop is met, in which it will return true
     * contains(Object o) method checks if the Object o is an element in the list
     */
    public boolean contains(Object o) {
        if(size <= 0){ //Size validation that list is not empty
            return false;
        }

        ListNode cur = head.next; //Creates cur pointer

        for(int i = 0; i < this.size(); i++){
            if(cur.data == null){ //Edge Case
                return true;
            }
            if(cur.data != null && cur.data.equals(o)){
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    /**
     * @author Spencer Olson
     * @param o Object o is the data that is passed through to be the object that is removed from the list
     * @return Return false if the data was unable to be removed, returns true if the object was able to removed
     * remove(Object o) method removes Object o from the list
     */
    public boolean remove(Object o){
        if(this.size() <= 0){
        return false;
        }

        //Creating pointers
        ListNode prev = head;
        ListNode cur = head.next;

        // //List traversal
        for(int i = 0; i < this.size(); i++){ //Edge Case
            if(o == null){
                if(cur.data == null){
                    prev.next = cur.next;
                    size--;
                    return true;
                }
            } else {
                if(cur.data.equals(o)){
                    prev.next = cur.next;
                    size--;
                    return true;
                }
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    /**
     * @author Spencer Olson
     * @param o Object o is the object that should have
     * @return boolean 'removed' returns true if the operation of the delegate call from the remove(Object o) method was successful and false otherwise
     removeAllCopies(Object o) removes all instances of element Object o in a list
     */
    public boolean removeAllCopies(Object o) {

        boolean removed = remove(o);
        while(remove(o)){ //Delegate Call
        }
        return removed;
    }

    /**
     * @author Spencer Olson
     * @param A "MyLinkedList A" is the list A passed through
     * @param B "MyLinkedList B" is the list B passed through
     * @return Linked List C is the list that is returned which results from MyLinkedList A and MyLinkedList B
     * interLeave(MyLinkedList A, MyLinkedList B) method interweaves the two linked lists that are passed through starting with list A
     */
    public static MyLinkedList interleave(MyLinkedList A, MyLinkedList B) {

        MyLinkedList C = new MyLinkedList();
        ListNode curA = A.head.next;
        ListNode curB = B.head.next;
        ListNode curC = C.head;

        while(curA != null && curB != null){
            C.add(curA.data);
            C.add(curB.data);
            curA = curA.next;
            curB = curB.next;
            C.size = C.size + 2;
        }

        while(curA != null){
            C.add(curA.data);
            curA = curA.next;
        }

        while(curB != null){
            C.add(curB.data);
            curB = curB.next;
        }

        return C;
    }

    /**
     * @author Spnecer Olson
     * @param index int index is the index in which the element will be added
     * @param o Object o is the data of the element that will be added
     * @throws IndexOutOfBoundsException IndexOutOfBoundsException is thrown if the index passed through is out of the scope of the list
     * add(int index, Object o) method will add the Object o at the specified index
     */
    public void add(int index, Object o) throws IndexOutOfBoundsException {

        if(index < 0 || index > this.size){
            throw new IndexOutOfBoundsException("Index Passed in not valid!");
        }

        ListNode cur = head;
        int i = 0;

        while(i < index){
            cur = cur.next;
            i++;
        }
        ListNode newNode = new ListNode(o);
        newNode.next = cur.next;
        cur.next = newNode;
        size++;
    }

    /**
     * @author Spencer Olson
     * @param index int index is the index passed through
     * @return cur.data is the data of the element at the specified index
     * @throws IndexOutOfBoundsException IndexOutOfBoundsException is thrown if the index passed through is out of the scope of the list
     * method get(int index) gets and returns the data of the node at a specified index
     */
    public Object get(int index) throws IndexOutOfBoundsException{

        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException("The index is out of bounds.");
        }

        ListNode cur = head.next;

        for(int i = 0; i < index; i++){
            cur = cur.next;
        }
        return cur.data;
    }

    /**
     * @author Spencer Olson
     * @param index int index is the specified index that the element should be removed
     * @return 'temp' is a temporary object storing and returning the data from the element that was removed
     * @throws IndexOutOfBoundsException IndexOutOfBoundsException is thrown if the index passed through is out of the scope of the list
     * method remove(int index) removes the element at a specified index
     */
    public Object remove(int index) throws IndexOutOfBoundsException {

        if(index < 0 || index >= this.size){
            throw new IndexOutOfBoundsException("Provided index is out of bounds!");
        }

        ListNode cur = head;
        ListNode prev = null;

        for(int i = 0; i <= index; i++){

            prev = cur;
            cur = cur.next;

            if(i == index){
                Object temp = cur.data;
                prev.next = cur.next;
                size--;
                return temp;
            }
        }
        return null;
    }


    /**
     * @author Spencer Olson
     * @param e Object e is the data for the element that is being passed in
     * @return True is returned when the node is created
     * add(Object e) method adds a node containing Object e at the end of the list
     */
    public boolean add(Object e) {
        ListNode cur = head;
        ListNode newNode = new ListNode(e);

        while(cur.next != null){
            cur = cur.next;
        }
        cur.next = newNode;
        size++;
        return true;
    }

    @Override
    public String toString() {
        String result = "{";
        for (ListNode node = this.head.next; node != null; node = node.next) {
            if(node.next != null)
                result += node.data + "->";
            else
                result += node.data;
        }
        return result + "}";
    }
}

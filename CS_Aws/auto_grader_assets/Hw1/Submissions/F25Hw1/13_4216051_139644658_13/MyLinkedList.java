import java.util.NoSuchElementException;

public class MyLinkedList {
	
	private ListNode head;
	private int size;
	
	//inner class for ListNode
	private class ListNode {

		private Object data;
		private ListNode next;

		private ListNode(Object d) {
			this.data = d;
			this.next = null;

		} // end of ListNode
	} // end of ListNode
	
	public MyLinkedList() {
		this.head = new ListNode(null); //with a dummy head node
		this.size = 0;
	} // end of MylinkedList
	
	public int size() {
		return this.size;
	} // end of size

	public boolean isEmpty() {
		return size == 0;
	} // end of isEmpty

	// Add Object e to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method in your source code.
	public void addFirst(Object e)
	{
		ListNode node = new ListNode(e);
		node.next = head.next;
		head.next = node;
		size++;
	} // end of addFirst
	
	// Remove(cut out) the first data node(the node succeeding the dummy node) 
	//       in this list, then returns the data in the node removed.
	// If the size of this list is zero, throws an Exception.
	public Object removeFirst() throws Exception {

		//empty list check
		if (isEmpty()) {
			throw new NoSuchElementException();
		} // end of if


		ListNode cur = head.next; // setting up first node to be removed

		head.next = cur.next; // pointing the head to whats after cur for removal
		size--; // downsizing to get rid of the previous node

		return cur.data; // returning the deleted data
	} // end of removeFirst
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {

		ListNode cur = head.next; // setting up cur

		while (cur != null) { // loop init
			if(cur.data == null){ // null check
				if(o == null){ // null check
					return true;
				} // end of if
			} else {
				if(cur.data.equals(o)){ // checking data
					return true; // returning true if found
				} // end of if
			} // end of else

			cur = cur.next; // moving cur

		} // end of while
		return false; // returning if not found

	} // end of contains
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {

		//helpers
		ListNode cur = head.next;
		ListNode prev = head;

		if(isEmpty()){
			return false;
		}

		while(cur != null) {
			// moving through list to find object
			if ( (cur.data == null && o == null) || (cur.data != null && cur.data.equals(o)) ) {
				prev.next = cur.next;
				size--;
				return true;
			} // end of if

			// moving
			prev = cur;
			cur = cur.next;
		} // end of while


    return false; // not found
		
	} // end of remove

	// Removes all copies of o from this linked list.
	// You have to handle the cases where Object o may 
	//        have zero, one or multiple copies in this list.
	// If any element has been removed from this list, returns true. 
	//        Otherwise returns false.
	// Note: be careful when multiple copies of Object o are stored
	//        in consecutive(adjacent) list nodes.
	//        E.g. []->["A"]->["A"]->["B"]. 
	//        Be careful when removing all "A"s in this example.
	// Note: This list may contains zero, one or multiple copies of null data elements.
	public boolean removeAllCopies(Object o) {

		//empty list check
		if (isEmpty()) {
			return false;
		} // end of if

		// helpers
		boolean removed = false;
		ListNode cur = head.next;
		ListNode prev = head;

		// going through list
		while (cur != null) {

			// if both are null
			if (cur.data == null && o == null) {
				prev.next = cur.next;
				cur = cur.next;
				size--;
				removed = true;
			} // end of if

			// if both are not null
			else if (cur.data != null && cur.data.equals(o)) {
				prev.next = cur.next;
				cur = cur.next;
				size--;
				removed = true;
			} else {

				//move through list
				prev = cur;
				cur = cur.next;
			} // end of if else
		} // end of while

		return removed;
	} // end of removeAllCopies


	// Insert data elements from linkedlist A and B alternately into 
	//    a new linkedlist C, then returns C.
        // Follow the pattern to pick items in list A and B, 
        //        linkedlist A->linkedlist B->linkedlist A->linkedlist B …
	// If A is longer than B, append remaining items in A to C
	//     when the end of B is first reached.
	// If B is longer than A, append remaining items in B to C
	//     when the end of A is first reached.
	// E.g1 A = {1, 3, 5, 7, 9} and B = {2, 4, 6}; and 
	//       C will be {1, 2, 3, 4, 5, 6, 7, 9}.
        //
	// E.g2 A = {1, 3, 5} and B = {2, 4, 6, 8, 10}; and 
	//       C will be {1, 2, 3, 4, 5, 6, 8, 10}.
	// Note: after this method is called, both list A and B are UNCHANGED.
	public static MyLinkedList interleave(MyLinkedList A, MyLinkedList B) {

		// creating new list
		MyLinkedList C = new MyLinkedList();

		// helpers / placeholder
		ListNode curA = A.head.next;
		ListNode curB = B.head.next;

		// adding to new list
		while (curA != null || curB != null) {

			//adding A
			if (curA != null) {
				C.add(curA.data);
				curA = curA.next;
			} // end of if

			//Adding B
			if (curB != null) {
				C.add(curB.data);
				curB = curB.next;
			} // end of if
		} // end of while


		return C;
	} // end of interleave
	
	// Inserts the specified element at the specified position in this list. 
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.
	
	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].
	public void add(int index, Object o) {

		// out of bounds check
		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException();
		} // end of if

		// helpers
		ListNode prev = head;

		// move through list
		for ( int i = 0 ; i < index ; i++){
			prev = prev.next;
		} // end of if

		// adding new node
		ListNode newNode = new ListNode(o);
		newNode.next = prev.next;
		prev.next = newNode;
		size++;

	} // end of add at index
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{

		// setting up cur and counter
		ListNode cur = head.next;
		int count = 0;

		// empty list check
		if (isEmpty()) {
			return null;
		} // end of if

		 if(index > size -1 || index < 0){ // out of bounds check
			 throw new IndexOutOfBoundsException();
		 } else {

			 while (count < index) {
				 cur = cur.next;
				 count++;
			 } //end of while

			 return cur.data; // returning the data
		 } // end of if else
	} // end of get
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException {

		// helpers
		ListNode cur = head.next;
		ListNode prev = head;
		int count = 0;

		if(index > size -1 || index < 0){ // null exception
			throw new IndexOutOfBoundsException();
		} // end of if

		if( index == 0){
			return head.data;
		}
		
		//moving through list
		while (count < index) {
			prev = cur;
			cur = cur.next;
			count++;
		} // end of while

		//removal
		prev.next = cur.next;
		size--;
		return cur.data;

	}// end of remove at index

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {

		//helpers
		ListNode cur = head.next;
		ListNode nn = new ListNode(e);

		if(head.next == null){
			head.next = nn;
		} else {
			while (cur.next != null) {
				cur = cur.next;
			} // end of while

			cur.next = nn;

		} // end of if else


		size++;
		return true;
	} // end of add
	
        //Please DO NOT Change the following toString() method!!!
        //You must include the following toString() method in your source code.
	@Override
	public String toString() {
		String result = "{";
	    for (ListNode node = this.head.next; node != null; node = node.next) {
	    		if(node.next != null)
	    			result += node.data + "->"; 
	    		else
	    			result += node.data;
	    } // end of for
	    return result + "}";
	  } // end of to String
} // end of MyLinkedList

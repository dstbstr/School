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

	// Add Object e to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method in your source code.
	public void addFirst(Object e)
	{
		ListNode node = new ListNode(e);
		node.next = head.next;
		head.next = node;
		size++;
	}
	
	// Remove(cut out) the first data node(the node succeeding the dummy node) 
	//       in this list, then returns the data in the node removed.
	// If the size of this list is zero, throws an Exception.
	public Object removeFirst() throws Exception {

		//check for empty exceptions size of list 0
		if(size == 0){
			throw new NoSuchElementException("LinkedList is empty!");
		}//end statement

		//find first real node
		ListNode rFirst = head.next;

		//store data in memory
		Object data = rFirst.data;

		//bridge over the first node
		head.next = rFirst.next;

		//decrement size of list
		size--;

		return data; //change this as you need.
	}
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {

		//start from first real Node
		ListNode cur = head.next;

		//walk through list nodes for null
		while(cur != null) {
			if (o == null) {
				if (cur.data == null) {
					return true;
				}
			} else {
				if (o.equals(cur.data)) {
					return true;
				}
			}
			//walk to next node
			cur = cur.next;
		}//end loop

		return false; //change this as you need.
	}
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {

		ListNode prev = head;
		ListNode cur = head.next;

		while(cur != null) {

			if(o == null ? cur.data == null : o.equals(cur.data)) {

				prev.next = cur.next;
				size--;
				return true;

			}

			prev = cur;
			cur = cur.next;

		}
		
		return false; //change this as you need.
	}

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
	public boolean removeAllCopies( Object o ) { //passed test

		boolean deletedNodes = false;

		ListNode prev = head;
		ListNode cur = head.next;

		while(cur != null) {

			if(o == null ? cur.data == null : o.equals(cur.data)) {

				//cut out matching node
				prev.next = cur.next;

				//walk to next node
				cur = prev.next;

				//decrement size
				size--;

				//assign list to true
				deletedNodes = true;
			}else {
				prev = cur;
				cur = cur.next;

			}

		}
		//return removed all copies list <deletedNodes>
		return deletedNodes; //change this as you need.
	}
	
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

		//create list to add elements from A and B
		MyLinkedList C = new MyLinkedList();

		//make end pointer for C.
		ListNode end = C.head;

		//start at non-dummy Node for list A and B
		ListNode placeA = A.head.next;
		ListNode placeB = B.head.next;

		//iterate through loops until null pointer.
		while(placeA != null || placeB != null) {

			//Walk through both Lists A and B once per iteration of loop
			//List A
			if (placeA != null) {

				//replicate node data to list C
				end.next = C.new ListNode(placeA.data);

				end = end.next;
				C.size++;
				placeA = placeA.next;
			}

			//List B
			if (placeB != null) {

				//replicate node data to list C
				end.next = C.new ListNode(placeB.data);
				end = end.next;
				C.size++;
				placeB = placeB.next;
			}

		}
		
		return C; //change this as you need.
	}
	
	// Inserts the specified element at the specified position in this list. 
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.
	
	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].
	public void add(int index, Object o) {

		//pre-condition check for size and length
		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException("Index Passed in not valid!");
		}

		ListNode prev = head;

		for(int i = 0; i < index; i++) {
			prev = prev.next;
		}

		//create new node for passed in index
		ListNode newNode = new ListNode(o);

		newNode.next = prev.next;
		prev.next = newNode;

		//increment size
		size++;
		
	}
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{

		//pre-condition check
		if (index < 0 || index >= this.size()){
			throw new IndexOutOfBoundsException("Provided index is out of bounds!");
		}

		//start traversing from first real data node
		ListNode current = head.next;

		//iterate through list to find target index
		for (int i = 0; i < index; i++){

			current = current.next;
			
		}

		return current.data; //change this as you need.
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException {

		//pre-condition check
		if (index < 0 || index >= this.size){
			throw new IndexOutOfBoundsException("Provided index is out of bounds!");
		}

		//start at the dummy head
		ListNode prev = head;

		for (int i = 0; i < index; i++)	{
			prev = prev.next;
		}

		ListNode removeIndex = prev.next;

		prev.next = removeIndex.next;
		size--;

		return removeIndex.data; //change this as you need.
	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {

		//start from DH Node
		ListNode cur = head;

		//walk through linkedlist
		while(cur.next != null) {
			cur = cur.next;
		}

		//create and add new node to list end
		cur.next = new ListNode(e);

		//increase size of linkedlist
		size++;
		
		return true; //change this as you need.
	}
	
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
	    }
	    return result + "}";
	  }
}

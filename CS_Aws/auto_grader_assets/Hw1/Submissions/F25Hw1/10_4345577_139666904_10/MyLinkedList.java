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

		if  (isEmpty()) {
			throw new Exception("List is empty");
		}

		head.next = head.next.next; // The next pointer is redirected from the dummy head to the next node
		size--; // Decreases the size of the node
		
		return null; //change this as you need.
	}

	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {

		ListNode node = head.next; // This skips the dummy head and automatically starts from the first data node
		while (node != null) {
			if (o == null ? node.data == null : o.equals(node.data)) {
				return true; // Will return a match if found true. If not, "return false" will mean element is complete
			}
			node = node.next; // Allows for movement to the next node
		}

		return false; //change this as you need.
	}

	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {

		ListNode prev = head; // The previous pointer
		ListNode cur = head.next; // The current pointer

		while (cur != null) {

			if (o == null ? cur.data == null : o.equals(cur.data)) {

				prev.next = cur.next; // By connecting cur.next to the previous node, it will remove the node
				size--; // Decrease the count in the object
				return true; // If the data is complete, it will return true
			}

			// both of the pointers are moving forward
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

		int i = 0; // This integer is used as a counter
		while (contains(o)) {
			remove(o); // Removes one object from the node
			i++;
		}

		if (i > 0)
			return false; // If the integer is greater than 0, the node returns false

		return true; //change this as you need.

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

		MyLinkedList C = new MyLinkedList(); // The new LinkList that's created
		ListNode nodeA = A.head.next; // Start of A node
		ListNode nodeB = B.head.next; // Start of B node

		while (nodeA != null && nodeB != null) {

			// Adds the next element of A if proven to be true
			if (nodeA != null) {
				C.addFirst(nodeA.data);
				nodeA = nodeA.next;
			}

			// Adds the next element of B if proven to be true
			if (nodeB != null) {
				nodeA = nodeA.next;
				nodeB = nodeB.next;
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

		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}

		ListNode newNode = new ListNode(o); // Creates a new node

		// Helps link the newly created node into the list created
		newNode.next = head.next;
		head.next = newNode;

		size++; // Increases the size

	}
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) {

		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}

		ListNode node = head.next; // Start of the data node

		// Help move the index of the node forward
		for (int i = 0; i < index; i++) {
			node = node.next;
		}

		return null; //change this as you need.
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) {

		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}

		// Finds the node before removing it
		ListNode prev = head;
		for (int i = 0; i < index; i++) {
			prev = prev.next;
		}

		ListNode remove = prev.next; // Removes the node after the previous
		prev.next = remove.next; // Allows data to bypass the node and remove it
		size--; // Decrease the size of the node
		
		return remove.data; //change this as you need.
	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {

		ListNode node = head; // Dummy Head Starter

		// Moves to the last node
		while(node != null) {
			node = node.next;
		}

		ListNode newNode = new ListNode(e); // Attaches a new node
		size++; // Increase the size
		
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

import java.util.LinkedList;
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
		if (isEmpty())
			throw new NoSuchElementException();
		Object removed = head.next.data;
		this.head.next = head.next.next;
		size--;
		return removed;
	}
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {
		ListNode cur = head.next;
		while (cur != null) {
			if (o == null ? cur.data == null : o.equals(cur.data))
				return true;
			cur = cur.next;
		}
		return false;
	}
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {

		// Initialize variables
		int i = 0;
		ListNode cur = head.next;
		ListNode prev = head;

		// Start off the while loop
		while (cur != null) {
			if (o == null ? get(i)==null : o.equals(get(i))) { // I used the ternary operator from the description.
				prev.next = cur.next;
				size--;
				return true;
				// If conditions are met, the code above cuts out the node.
			}
			i++;
			prev = cur;
			cur = cur.next;
			// Otherwise, the code just continues to traverse the LinkedList
		}
		return false; // If we never hit the target, got to return false.
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
	public boolean removeAllCopies( Object o ) {
		// Initialize variables
		ListNode cur = head.next;
		ListNode prev = head;

		int i = 0; // We'll use this later

		while (cur != null) {
			// I modified the original ternary operator from the previous remove method. I realized I can just
			// use the cur.data that is already being iterated instead of relying on get(i) to get the data.
			if (o == null ? cur.data ==null : o.equals(cur.data)) {
				prev.next = cur.next;
				size--;
				i++; // If conditions are met, cut the node from the list
			}
			prev = cur;
			cur = cur.next; // Otherwise, keep iterating.
		}
		if (i > 0) // Here is why the i was useful.
			return true;
		else
			return false;
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
		MyLinkedList result = new MyLinkedList();

		// Assume DH LinkedLists for both params
		ListNode curA = A.head.next;
		ListNode curB = B.head.next;

		// Standard case, I talk more about this below.
		while (curA != null || curB != null) {
			if (curA != null) {
				result.add(curA.data);
				curA = curA.next;
			}
			if (curB != null) {
				result.add(curB.data);
				curB = curB.next;
			}
		}

		return result; // Here is the output.

		// Extra Node:
		// I was attempting to write this with an int i iterator originally that would switch which list
		// result took from depending on whether the counter was odd or even in each run. It made things more complicated
		// and I ultimately did away with this system. I realized the solution was more simple than that.
		// If I just have the outer while loop accounting for an either or case, and then use if statements
		// to analyze if either curA or curB is empty, it will already do what I was hoping to do without the need of the
		// i iterator. I found it quite funny once I spotted the redundancy.
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
		// Edge case if things are easy
		if (index < 0 || index > this.size)
			throw new IndexOutOfBoundsException();


		// This is where I initialize the variables. Node is particularly important since its the node
		// we'll add later.

		int i = 0;
		ListNode node = new ListNode(o);
		ListNode cur = head.next;
		ListNode prev = head;

		// Until i is equal to index, keep iterating the other three variables.
		while (i < index){
			prev = cur;
			cur = cur.next;
			i++;
		}

		// Fit in the node with data in between prev and cur. Afterward, increase the size by one.
		node.next = cur;
		prev.next = node;
		size++;
		
	}
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{
		// Solve for param edge case
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException();

		// Initialize the variables
		ListNode cur = head.next;
		int i = 0;

		// Normal iterator for normal case
		while (i < index){
			cur = cur.next;
			i++;
		}

		// Returning data
		return cur.data;

		// Not a lot to say about this method, it didn't seem particularly complicated.
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException {
		// Basic param edge case
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException();

		// Initialize variables
		ListNode cur = head.next;
		ListNode prev = head;
		int i = 0;

		// Normal iterator
		while (i < index){
			prev = cur;
			cur = cur.next;
			i++;
		}

		// Finalizing results, this also accounts for base case of index = 0.
		// This is where we cut out cur.
		prev.next = cur.next;
		size--;
		return null;
	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {
		// Initialize necessary variables
		ListNode node = new ListNode(e);
		ListNode cur = head.next;
		ListNode prev = head;

		// Iterate through the list
		while (cur != null) {
			prev = cur;
			cur = cur.next;
		}

		// Finalize results
		prev.next = node;
		size++;
		return true;

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

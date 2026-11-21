public class CDoublyLinkedList {

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

	public CDoublyLinkedList() {
		this.head = new Node(null, null, null );
		this.head.next = this.head;
		this.head.prev=this.head;
		this.size = 0;
	}

	public boolean isEmpty() {
		return this.head == this.head.next;
	} 
	
	// Add Object data to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method
	//      in your source code.
	public void addFirst(Object data) {
		Node nn = new Node(data, this.head, this.head.next);
		this.head.next.prev = nn;
		this.head.next = nn;
		this.size ++;
	}

	// write a method void addLast(Object data) that will insert 
	// the piece of data at the end of the current list.
	// Note: this list is allowed to store null data element in its list node.
	public void addLast(Object data) {
		Node nn = new Node(data, this.head.prev, this.head);
		this.head.prev.next = nn;
		this.head.prev = nn;
		this.size++;
	}
	
	// Write the subListOfSmallerValues method.  
	// It should return a CDoublyLinkedList object 
	//     containing data that is smaller than the value passed to the method.
        // If a null data element in this list is encountered, you can skip it.
        // You need to use the CompareTo() method to determine which object is smaller.
        // If list A contains {9, 11, 14, 6, 4, 7} and you call  A.subListOfSmallerValues(10),
        // the method call returns a list that contains data in A that is smaller than 10, the passed-in argument.
        // That is, the returned list contains { 9, 6, 4, 7}.
	public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
	
		CDoublyLinkedList result = new CDoublyLinkedList();

		for(Node cur = this.head.next; cur != this.head; cur = cur.next) {
			if(cur.data != null) {
				Comparable temp = (Comparable) cur.data;
				if(temp.compareTo(data) < 0) {
					result.addLast(cur.data);
				}
			}
		}

		return result;
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
	
		for(Node cur = this.head.prev; cur != this.head; cur = cur.prev) {

			if((dataToRemove == null && cur.data == null) ||
			   (dataToRemove != null && dataToRemove.equals(cur.data))) {

				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				this.size--;
				return true;
			}
		}

		return false;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {

		int index = 0;
		int result = -1;

		for(Node cur = this.head.next; cur != this.head; cur = cur.next) {
			if((o == null && cur.data == null) ||
			   (o != null && o.equals(cur.data))) {
				result = index;
			}
			index++;
		}

		return result;
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {

	    if(other == null) throw new NullPointerException();

	    boolean changed = false;
	    Node cur = this.head.next;

	    while(cur != this.head) {
	    	Node nextNode = cur.next;

	    	if(!contains(other, cur.data)) {
	    		cur.prev.next = cur.next;
	    		cur.next.prev = cur.prev;
	    		this.size--;
	    		changed = true;
	    	}

	    	cur = nextNode;
	    }

	    return changed;
	}
	
	// helper method for retainAll
	private boolean contains(CDoublyLinkedList list, Object data) {
		for(Node cur = list.head.next; cur != list.head; cur = cur.next) {
			if((data == null && cur.data == null) ||
			   (data != null && data.equals(cur.data))) {
				return true;
			}
		}
		return false;
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		
		if(this.size <= 1) return;

		for(Node i = this.head.next.next; i != this.head; i = i.next) {
			Object key = i.data;
			Node j = i.prev;

			while(j != this.head && compare(j.data, key) > 0) {
				j.next.data = j.data;
				j = j.prev;
			}

			j.next.data = key;
		}
	}
	
	// helper method for comparison
	private int compare(Object a, Object b) {
		if(a == null && b == null) return 0;
		if(a == null) return -1;
		if(b == null) return 1;
		return ((Comparable)a).compareTo(b);
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
}

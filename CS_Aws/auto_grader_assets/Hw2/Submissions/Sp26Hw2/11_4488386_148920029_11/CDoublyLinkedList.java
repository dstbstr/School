// ryan larkin
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

		for (Node cur = head.next; cur != head; cur = cur.next) {
			if (cur.data != null) {
				Comparable c = (Comparable) cur.data;
				if (c.compareTo(data) < 0) {
					result.addLast(cur.data);
				}
			}
		}
		return result; //change this as needed.
	}

	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list.
        // It scans the list from back to the front by following the prev links.
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
		for (Node cur = head.prev; cur != head; cur = cur.prev) {
			if (dataToRemove == null) {
				if (cur.data == null) {
					removeNode(cur);
					return true;
				}
			} else if (dataToRemove.equals(cur.data)) {
				removeNode(cur);
				return true;
			}
		}
		return false;//change this as needed.
	}

	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
		int index = size-1;

		for (Node cur = head.prev; cur != head; cur = cur.prev) {
			if (o == null) {
				if (cur.data == null) { return index; }
			} else if (o.equals(cur.data)) { return index; }
			index--;
		}
		return -1; //change this as needed.
	}


	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		if (other == null) { throw new NullPointerException("other is null"); }

		boolean changed = false;
		Node cur = head.next;

		while (cur != head) {
			Node next = cur.next;
			if (!other.contains(cur.data)) {
				removeNode(cur);
				changed = true;
			}
			cur = next;
		}
	    return changed; //change this as needed.
	}

	private boolean contains(Object o) {
		for (Node cur = head.next; cur != head; cur = cur.next) {
			if (o == null) {
				if (cur.data == null) { return true; }
			} else if (o.equals(cur.data)) { return true; }
		}
		return false;
	}


	private void removeNode(Node node) {
		node.prev.next = node.next;
		node.next.prev = node.prev;
		size--;
	}

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		Node lastSorted, sortedWalker;
		Comparable firstUnsortedData;
		for (lastSorted = this.head.next; lastSorted != this.head.prev; lastSorted = lastSorted.next) {
			firstUnsortedData = (Comparable) lastSorted.next.data;
			for (sortedWalker = lastSorted; sortedWalker != head && ((Comparable) sortedWalker.data).compareTo(firstUnsortedData) > 0; sortedWalker = sortedWalker.prev) {
				sortedWalker.next.data = sortedWalker.data;
			}
			sortedWalker.next.data = firstUnsortedData;
		}
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
			
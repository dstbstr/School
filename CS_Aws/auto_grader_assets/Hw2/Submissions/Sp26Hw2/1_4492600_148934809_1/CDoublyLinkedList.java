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
		Node cur = this.head.prev;
		cur.next = new Node(data, cur, cur.next);
		head.prev = cur.next;
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
		Node cur = this.head;
		CDoublyLinkedList subListOfSmallerValues = new CDoublyLinkedList();
		while (cur.next != head) {
			if (cur.data == null) {
				subListOfSmallerValues.addLast(cur.next.data);
			}
			else if (data.compareTo(cur.next.data)>0) {
				subListOfSmallerValues.addLast(cur.next.data);
			}
			cur = cur.next;
		}
		return subListOfSmallerValues; //change this as needed.
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
		Node cur = this.head.prev;
		while (cur != head) {
			if (dataToRemove == null && cur.data == null) {
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				this.size--;
				return true;
			}
			else if (dataToRemove != null && cur.data != null && dataToRemove.equals(cur.data)) {
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				this.size--;
				return true;
			}
			cur = cur.prev;
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
		Node cur = this.head.next;
		int lastIndex = -1;
		while (cur != head) {
			lastIndex++;
			if (cur.data != null) {
				if (cur.data.equals(o)) {
					return lastIndex;
				}
			}
			cur = cur.next;
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
		if (other == null) {
			throw new NullPointerException();
		}
		Node cur = this.head.next;
		Node otherCur;
		boolean retained = false;
		boolean removed = false;
		while (cur != this.head) {
			otherCur = other.head.next;
			while (otherCur.next != other.head) {
				if (cur.data != null && otherCur.data != null) {
					if (cur.data.equals(otherCur.next.data)) {
						retained = true;
					}
				}
				otherCur = otherCur.next;
			}
			if (!retained) {
				cur.next.prev = cur.prev;
				cur.prev.next = cur.next;
				this.size --;
				removed = true;
			}
			cur = cur.next;
			retained = false;
		}
	    return removed; //change this as needed.
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		if (this.size == 0) {
			return;
		}
		Node cur;
		Node unsorted = this.head.next.next;
		Node sorted = this.head.next;
		while (unsorted != head) {
			if (sorted.data != null && unsorted.data != null) {
				if (((Comparable) unsorted.data).compareTo(sorted.data) < 0) {
					unsorted.next.prev = sorted;
					sorted.prev.next = unsorted;
					unsorted.prev = sorted.prev;
					sorted.next = unsorted.next;
					unsorted.next = sorted;
					sorted.prev = unsorted;
					unsorted = sorted;
					sorted = unsorted.prev;
				}
			}
			cur = sorted.prev;
			while (cur.prev != head) {
				if (cur.data != null && sorted.data != null) {
					if (((Comparable) sorted.data).compareTo(cur.data) < 0) {
						sorted.next.prev = cur;
						cur.prev.next = sorted;
						sorted.prev = cur.prev;
						cur.next = sorted.next;
						sorted.next = cur;
						cur.prev = sorted;
						sorted = cur;
						cur = sorted.prev;
					}
				}
				cur = cur.prev;
				sorted = sorted.prev;
			}
			unsorted = unsorted.next;
			sorted = unsorted.prev;
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
			
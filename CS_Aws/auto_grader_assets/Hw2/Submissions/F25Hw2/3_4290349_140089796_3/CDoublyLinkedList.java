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
		this.size ++;
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
		CDoublyLinkedList sublist = new CDoublyLinkedList();

		Node cur = this.head.next;

		while (cur != this.head) {
			// Needs this to prevent null pointer exception
			if (cur.data == null)
				cur = cur.next;
			// This is where we compare our data and if small enough, add to sublist.
			if (data.compareTo(cur.data) > 0) {
				sublist.addLast(cur.data);
			}
			cur = cur.next;
		}

		// Finally, we return the sublist.
		return sublist;
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {

		Node prev = this.head.prev;

		while (prev != this.head) {
			if (prev.data == null) {
				if (dataToRemove == null) {
					prev.prev.next = prev.next;
					prev.next.prev = prev.prev;
					this.size--;
					return true;
				}
				else {
					prev = prev.prev;
				}
			}
			else if (prev.data.equals(dataToRemove)) {
				prev.prev.next = prev.next;
				prev.next.prev = prev.prev;
				this.size--;
				return true;
			}
			prev = prev.prev;
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
		Node cur = this.head.next; int i = 0, min = -1;

		while (cur != this.head) {
			if (o==null ? cur.data==null : o.equals(cur.data)) {
				min = i;
			}
			cur = cur.next;
			i++;
		}
		return min;
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		if (other == null)
			throw new NullPointerException();

		Node cur = this.head.next; int i = 0;

		while (cur != this.head) {
			if (!other.contains(cur.data)) {
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				this.size--;
				i++;
			}
			cur = cur.next;
		}

		if (i > 0)
			return true;
		return false;
	}

	// Helper method I built for retainAll.
	private boolean contains(Object o) {
		Node cur = this.head.next;
		while (cur != this.head) {
			if (o == null ? cur.data == null : o.equals(cur.data))
				return true;
			cur = cur.next;
		}
		return false;
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		if (this.size < 2)
			return;

		Node cur = this.head.next;
		while (cur != this.head) {
			Node prev = cur.prev, next = cur.next; // Need to reset these every iteration
			prev.next = next;
			next.prev = prev; // This part cuts out cur, cur still exists thanks to it being saved at
			// cur but it doesn't exist in the list since we removed it temporarily
			while (prev != this.head) {
				if (((Comparable) prev.data).compareTo(cur.data) <= 0) {
					break; // If we ever get to a sorted state we break the loop
				}
				prev = prev.prev; // Otherwise we just keep iterating through the list
			}
			cur.next = prev.next; cur.prev = prev; prev.next.prev = cur; prev.next = cur;
			cur = next;

			/**
			 * This train of pointers puts cur back in the list in its sorted state,
			 * Next, cur gets next means we'll continue the next iteration at next which was a placeholder
			 * That way we don't have to worry about going through the whole list including sorted portion
			 * Over and over again, that would get old fast.
			 */
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
			
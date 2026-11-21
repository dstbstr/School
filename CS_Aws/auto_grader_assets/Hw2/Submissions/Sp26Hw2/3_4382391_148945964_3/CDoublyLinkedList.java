import java.util.Objects;

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

		@Override
		public String toString() {
			return "Node{" + this.data + "}";
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
		Node node = new Node(data, this.head.prev, this.head);
		this.head.prev.next = node;
		this.head.prev = node;
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
		CDoublyLinkedList newList = new CDoublyLinkedList();

		Node current = this.head.next;
		while (current != this.head) {
			if (current.data == null) {
				current = current.next;
				continue;
			}
			Comparable currentData = (Comparable) current.data;
			if (currentData.compareTo(data) < 0) {
				newList.addLast(current.data);
			}
			current = current.next;
		}

		return newList;
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
		Node current = this.head.prev;

		while (current != this.head) {
			if (Objects.equals(current.data, dataToRemove)) {
				current.next.prev = current.prev;
				current.prev.next = current.next;
				this.size--;
				return true;
			}
			current = current.prev;
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
		Node current = this.head.prev;
		int index = this.size - 1;

		while (current != this.head) {
			if (Objects.equals(current.data, o)) {
				return index;
			}
			current = current.prev;
			index--;
		}

		return -1;
	}

	public boolean contains(Object other) {
		Node current = this.head.next;

		while (current != this.head) {
			if (Objects.equals(current.data, other)) {
				return true;
			}
			current = current.next;
		}

		return false;
	}
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		Node current = this.head.next;

		int initialSize = this.size;
		while (current != this.head) {
			if (!other.contains(current.data)) {
				current.next.prev = current.prev;
				current.prev.next = current.next;
				this.size--;
			}
			current = current.next;
		}

		return this.size != initialSize;
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.

	public void insertionSort() {
		// represents both the head of unsorted region (FUD) and tail of sorted region.
		Node current = this.head.next;

		// walk forward from beginning of list
		while (current != this.head) {
			Node next = current.next;
			Comparable currentData = (Comparable) current.data;

			// walk backward from current element
			if (current.prev != this.head && currentData.compareTo(current.prev.data) < 0) {
				Node insertCurrent = current.prev;
				while (insertCurrent != this.head && currentData.compareTo(insertCurrent.data) < 0) {
					insertCurrent = insertCurrent.prev;
				}

				// remove current from list
				current.next.prev = current.prev;
				current.prev.next = current.next;

				// insert current after insertCurrent
				current.next = insertCurrent.next;
				current.prev = insertCurrent;

				insertCurrent.next.prev = current;
				insertCurrent.next = current;
			}

			current = next;
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
			
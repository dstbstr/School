import java.util.ArrayList;

public class CDoublyLinkedList {
//	public static void main(String[] args) {
//		CDoublyLinkedList t = new CDoublyLinkedList();
//		t.addFirst(4);
//		t.addFirst(12);
//		t.addFirst(2);
//		t.addFirst(8);
//		t.addFirst(2);
//		t.addFirst(4);
//		t.addFirst(6);
//		t.addFirst(6);
//		System.out.println(t.toString());
//		t.insertionSort();
//		System.out.println(t.toString());
//	}

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

	public static void printBack(Node head) {
		if (head == null) {
			return;
		}
		printBack(head.next);
		System.out.print(head.data);
	}


	// write a method void addLast(Object data) that will insert 
	// the piece of data at the end of the current list.
	// Note: this list is allowed to store null data element in its list node.
	public void addLast(Object data) {
		Node cur = this.head.prev;
		Node nn = new Node(data, cur, cur.next);
		cur.next.prev = nn;
		cur.next = nn;
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
		CDoublyLinkedList nl = new CDoublyLinkedList();
		for (Node cur = head.next; cur != head; cur = cur.next) {
			if (cur.data == null) {
				continue;
			}
			if (cur.data.getClass() == data.getClass() && data.compareTo(cur.data) > 0) {
				nl.addLast(cur.data);
			}
		}
		return nl; //change this as needed.
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
		int count = 0;
		for (Node cur = head.prev; count < this.size; cur = cur.prev, count++) {
			if (cur.data == dataToRemove) {
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
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
		int ind = this.size;
		for (Node cur = head.prev; ind > 0; cur = cur.prev, ind--) {
			System.out.println(ind);
			if (cur.data == o) {
				return ind;
			}
		}
		return -1; //change this as needed.
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public ArrayList<Object> getData(CDoublyLinkedList other) {
		if (other == null) {
			throw new NullPointerException();
		}
		ArrayList<Object> data = new ArrayList<>();
		for (Node cur = head.next; cur != head; cur = cur.next) {
			data.add(cur.data);
		}
		return data;
	}

	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		ArrayList<Object> data = getData(other);
		boolean removed = false;
		for (Node cur = this.head.next; cur != head; cur = cur.next) {
			if (!data.contains(cur.data)) {
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				removed = true;
			}
		}
	    return removed; //change this as needed.
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		Node ls = head.next;
		Node fud = ls.next;
		while (fud != head) {
			if ((int)ls.data > (int)fud.data) {
				fud.next.prev = ls;
				ls.next = fud.next;
				Node cur;
				for (cur = ls; (cur != head) && (int)cur.data > (int)fud.data; cur = cur.prev) {}
				fud.next = cur.next;
				fud.prev = cur;
				fud.next.prev = fud;
				cur.next = fud;
				fud = ls.next;
			}
			else {
				ls = fud;
				fud = ls.next;
			}
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
			
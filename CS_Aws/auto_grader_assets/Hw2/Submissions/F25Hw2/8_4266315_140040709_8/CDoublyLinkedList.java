import java.util.HashSet;

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
        Node cur = head;
        Node newNode = new Node(data, null, null);
        cur = cur.prev;
        newNode.next = head;
        newNode.prev = cur;
        cur.next = newNode;
        head.prev = newNode;
        size ++;
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
        Node cur = head.next;
        for (int i = 0; i < size; i++) {
            if (cur.data != null) {
                if (data.compareTo(cur.data) > 0) {
                    newList.addLast(cur.data);
                }
            }
            cur = cur.next;
        }
		return newList;
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
	    Node cur = head;
        cur = cur.prev;
        for(int i = 0; i < size; i++) {
            if (cur.data == null) {
                if (dataToRemove == null) break;
            } else {
                if (cur.data.equals(dataToRemove)) break;
            }
            cur = cur.prev;
        }
        if(cur == head) return false;
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        size--;
        return true;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
        Node cur = head.prev;
        for(int i = size - 1; i >= 0; i--) {
            if(cur.data == null) {
                if(o == null) return i;
            }else {
                if(cur.data.equals(o)) return i;
            }
            cur = cur.prev;
        }
		return -1;
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
        if(other == null) throw new NullPointerException();
        HashSet<Object> otherData = new HashSet<>();
        boolean removed = false;
        Node cur = other.head.next;
        for(int i = 0; i < other.size; i++, cur = cur.next) {
            otherData.add(cur.data);
        }
        cur = head.next;
        for(int i = 0; i < size; i++, cur = cur.next) {
            if(!otherData.contains(cur.data)) {
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                size--;
                removed = true;
            }
        }
	    return removed;
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		if(size <= 1) return;
        Node cur = head.next.next;
        for(int i = 1; i < size; i++, cur = cur.next) {
            Comparable toInsert = (Comparable) cur.data;
            Node sortedCur = cur.prev;
            for(int j = i - 1; j >= 0; j--, sortedCur = sortedCur.prev) {

                if (toInsert.compareTo(sortedCur.data) > 0) {
                    sortedCur.next.data = toInsert;
                    break;
                }

                sortedCur.next.data = sortedCur.data;
                if(j == 0) {
                    sortedCur.data = toInsert;
                }
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
			
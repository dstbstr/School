public class LinkedList {

	private class Node {
		private Object data; // Assume data implemented Comparable
		private Node next, prev;

		private Node(Object data, Node pref, Node next) {
			this.data = data;
			this.prev = pref;
			this.next = next;
		}
	}

	private Node head;
	private int size;

	public LinkedList() {
		this.head = new Node(null, null, null);
		this.head.next = this.head;
		this.head.prev = this.head;
		this.size = 0;
	}

	public int getSize() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.head == this.head.next;
	}

	// Add Object data to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method
	// in your source code.
	public void addFirst(Object data) {
		Node nn = new Node(data, this.head, this.head.next);
		this.head.next.prev = nn;
		this.head.next = nn;
		this.size++;
	}

	// write a method void addLast(Object data) that will insert
	// the piece of data at the end of the current list.
	// Note: this list is allowed to store null data element in its list node.
	public void addLast(Object data) {
		Node cur = this.head.prev;
		Node toAdd = new Node(data, cur, cur.next);
		cur.next.prev = toAdd;
		cur.next = toAdd;
		size++;
	}

	public void removeFirst() {
		if (this.size == 0) {
			throw new IllegalArgumentException("List is empty");
		}

		Node cur = this.head.next;
		cur.next.prev = cur.prev;
		cur.prev.next = cur.next;
		size--;
	}

	public Object getFirst() {
		return this.head.next.data;
	}

	// Write the subListOfSmallerValues method.
	// It should return a CDoublyLinkedList object
	// containing data that is smaller than the value passed to the method.
	// If a null data element in this list is encountered, you can skip it.
	// You need to use the CompareTo() method to determine which object is smaller.
	// If list A contains {9, 11, 14, 6, 4, 7} and you call
	// A.subListOfSmallerValues(10),
	// the method call returns a list that contains data in A that is smaller than
	// 10, the passed-in argument.
	// That is, the returned list contains { 9, 6, 4, 7}.
	public LinkedList subListOfSmallerValues(Comparable data) {

		LinkedList sublist = new LinkedList();
		Node cur = this.head.next;
		while (cur != this.head) {
			if (((Comparable) cur.data).compareTo(data) < 0) {
				Node curSublist = sublist.head.prev;
				Node toAdd = new Node(cur.data, curSublist, curSublist.next);
				curSublist.next.prev = toAdd;
				curSublist.next = toAdd;
				sublist.size++;
			}
			cur = cur.next;
		}
		return sublist; // change this as needed.
	}

	// This method should remove the first occurrence of the data from the list,
	// starting at the *BACK* of the list.
	// It scans the list from back to the front by following the prev links.
	// The method should return true if successful, false otherwise.
	// Note that list node may contain null data element. Please handle this edge
	// case.
	public boolean removeStartingAtBack(Object dataToRemove) {

		Node cur = this.head.prev;
		while (cur != this.head) {
			if (cur.data == null) {
				if (cur.data == dataToRemove) {
					cur.prev.next = cur.next;
					cur.next.prev = cur.prev;
					size--;
					return true;
				}
			} else {
				if (cur.data.equals(dataToRemove)) {
					cur.prev.next = cur.next;
					cur.next.prev = cur.prev;
					size--;
					return true;
				}
			}
			cur = cur.prev;
		}
		return false;// change this as needed.
	}

	// Returns the index of the last occurrence of the specified element in this
	// list,
	// or -1 if this list does not contain the element.
	// More formally, returns the highest index i
	// such that (o==null ? get(i)==null : o.equals(get(i))),
	// or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge
	// case.
	public int lastIndexOf(Object o) {

		Node cur = this.head.next;
		int index = -1;
		for (int i = 0; cur != head; i++) {
			if (cur.data == null) {
				if (cur.data == o) {
					index = i;
				}
			} else {
				if (cur.data.equals(o)) {
					index = i;
				}
			}
			cur = cur.next;
		}

		return index; // change this as needed.
	}

	// Removes from this list all of its elements that
	// are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	// returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
	// Helper methods are allowed.
	public boolean retainAll(LinkedList other) throws NullPointerException {

		if (other == null) {
			throw new NullPointerException("Other linkedlist is null.");
		}
		boolean res = false;
		Node cur = this.head.next;
		while (cur != this.head) {
			boolean inListToKeep = false;
			Node curOther = other.head.next;
			while (curOther != other.head) {
				if (cur.data == null) {
					if (cur.data == curOther.data) {
						inListToKeep = true;
					}
				} else {
					if (cur.data.equals(curOther.data)) {
						inListToKeep = true;
					}
				}
				curOther = curOther.next;
			}
			if (inListToKeep == false) {
				res = true;
				while (removeStartingAtBack(cur.data)) {
					// this method will continue to remove the data until all copies are gone
				}
			}
			cur = cur.next;
		}

		return res; // change this as needed.
	}

	// Write this method to sort this list using insertion sort algorithm,
	// as we have learned in the classroom.
	public void InsertionSort() {
		Node lastSorted, sortedWalker;
		Comparable firstUnorderedData;
		for (lastSorted = this.head.next; lastSorted != this.head.prev; lastSorted = lastSorted.next) {
			firstUnorderedData = (Comparable) lastSorted.next.data;
			for (sortedWalker = lastSorted; sortedWalker != this.head && ((Comparable) sortedWalker.data)
					.compareTo(firstUnorderedData) > 0; sortedWalker = sortedWalker.prev) {
				sortedWalker.next.data = sortedWalker.data;
			}
			sortedWalker.next.data = firstUnorderedData;
		}
	}

	public void MergeSort() {
		Queue q = new Queue();
		Node cur = this.head.next;
		while (cur != this.head) {
			LinkedList newList = new LinkedList();
			newList.addLast(cur.data);
			q.enqueue(newList);
			cur = cur.next;
		}

		while (q.size() > 1) {
			try {
				LinkedList sublist1 = (LinkedList) q.dequeue();
				LinkedList sublist2 = (LinkedList) q.dequeue();
				LinkedList tempList = merge(sublist1, sublist2);
				q.enqueue(tempList);
			} catch (Exception e) {

			}

		}

		try {
			this.head = ((LinkedList) q.dequeue()).head;
		} catch (Exception e) {

		}
	}

	public boolean isSorted() {
		Node cur = this.head.next;
		boolean result = true;
		while (cur.data != null && cur.next != this.head) {
			if (((Comparable) cur.data).compareTo((Comparable) cur.next.data) > 0) {
				result = false;
			}
			cur = cur.next;
		}
		return result;
	}

	public boolean remove(Object o) {
		Node cur = this.head.next;
		while (cur != this.head) {
			if (cur.data == null) {
				if (cur.data == o) {
					cur.next.prev = cur.prev;
					cur.prev.next = cur.next;
					size--;
					return true;
				}
			} else {
				if (cur.data.equals(o)) {
					cur.next.prev = cur.prev;
					cur.prev.next = cur.next;
					size--;
					return true;
				}
			}
			cur = cur.next;
		}
		return false;
	}

	public static LinkedList merge(LinkedList A, LinkedList B) {

		LinkedList S = new LinkedList();
		while (!A.isEmpty() && !B.isEmpty()) {
			Object fa = A.getFirst();
			Object fb = B.getFirst();
			if (((Comparable) fa).compareTo((Comparable) fb) < 0) {
				A.removeFirst();
				S.addLast(fa);
			} else {
				B.removeFirst();
				S.addLast(fb);
			}

		}

		while (!A.isEmpty()) {
			S.addLast(A.getFirst());
			A.removeFirst();
		}

		while (!B.isEmpty()) {
			S.addLast(B.getFirst());
			B.removeFirst();
		}

		return S;

	}

	@Override
	public String toString() {
		String result = "{";
		for (Node node = this.head.next; node != this.head; node = node.next) {
			if (node.next != this.head)
				result += node.data + "->";
			else
				result += node.data;
		}
		return result + "}";
	}
}

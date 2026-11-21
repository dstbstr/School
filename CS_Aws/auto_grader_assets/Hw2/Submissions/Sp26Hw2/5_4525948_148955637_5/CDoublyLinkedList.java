public class CDoublyLinkedList {

	private class Node {
		private Object data;
		private Node next, prev;

		private Node(Object data, Node pref, Node next) {
			this.data = data;
			this.prev = pref;
			this.next = next;
		}
	}

	private Node head;
	private int size;

	public CDoublyLinkedList() {
		this.head = new Node(null, null, null);
		this.head.next = this.head;
		this.head.prev = this.head;
		this.size = 0;
	}

	public boolean isEmpty() {
		return this.head == this.head.next;
	}

	// DO NOT CHANGE
	public void addFirst(Object data) {
		Node nn = new Node(data, this.head, this.head.next);
		this.head.next.prev = nn;
		this.head.next = nn;
		this.size++;
	}

	// Add element to end of list
	public void addLast(Object data) {

		Node nn = new Node(data, this.head.prev, this.head);
		this.head.prev.next = nn;
		this.head.prev = nn;
		this.size++;
	}

	// Return list of values smaller than given value
	public CDoublyLinkedList subListOfSmallerValues(Comparable<?> data) {

		CDoublyLinkedList result = new CDoublyLinkedList();

		for(Node cur = this.head.prev; cur != this.head; cur = cur.prev) {

			if(cur.data == null)
				continue;

			if(cur.data instanceof Comparable) {

				Comparable<?> val = (Comparable<?>) cur.data;

				if(((Comparable)val).compareTo(data) < 0)
					result.addFirst(cur.data);
			}
		}

		return result;
	}

	// Remove first occurrence from the back
	public boolean removeStartingAtBack(Object dataToRemove) {

		for(Node cur = this.head.prev; cur != this.head; cur = cur.prev) {

			if(dataToRemove == null) {

				if(cur.data == null) {
					cur.prev.next = cur.next;
					cur.next.prev = cur.prev;
					size--;
					return true;
				}

			} else if(dataToRemove.equals(cur.data)) {

				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				size--;
				return true;
			}
		}

		return false;
	}

	// Return last index of element
	public int lastIndexOf(Object o) {

		int index = 0;
		int lastFound = -1;

		for(Node cur = this.head.next; cur != this.head; cur = cur.next) {

			if(o == null) {

				if(cur.data == null)
					lastFound = index;

			} else if(o.equals(cur.data)) {

				lastFound = index;
			}

			index++;
		}

		return lastFound;
	}

	// Keep only elements contained in other list
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {

		if(other == null)
			throw new NullPointerException();

		boolean removed = false;
		Node cur = this.head.next;

		while(cur != this.head) {

			Node nextNode = cur.next;
			boolean found = false;

			for(Node o = other.head.next; o != other.head; o = o.next) {

				if(cur.data == null) {

					if(o.data == null) {
						found = true;
						break;
					}

				} else if(cur.data.equals(o.data)) {

					found = true;
					break;
				}
			}

			if(!found) {

				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				size--;
				removed = true;
			}

			cur = nextNode;
		}

		return removed;
	}

	// Insertion sort
	public void insertionSort() {

		if(size <= 1)
			return;

		Node current = head.next.next;

		while(current != head) {

			Node nextCurrent = current.next;

			if(!(current.data instanceof Comparable)) {
				current = nextCurrent;
				continue;
			}

			Comparable<?> curVal = (Comparable<?>) current.data;
			Node scan = current.prev;

			// detach node
			current.prev.next = current.next;
			current.next.prev = current.prev;

			while(scan != head &&
				  scan.data instanceof Comparable &&
				  ((Comparable)scan.data).compareTo(curVal) > 0) {

				scan = scan.prev;
			}

			// insert after scan
			current.next = scan.next;
			current.prev = scan;
			scan.next.prev = current;
			scan.next = current;

			current = nextCurrent;
		}
	}

	@Override
	public String toString() {

		String result = "{";

		for(Node node = this.head.next; node != this.head; node = node.next) {

			if(node.next != this.head)
				result += node.data + "->";
			else
				result += node.data;
		}

		return result + "}";
	}
}

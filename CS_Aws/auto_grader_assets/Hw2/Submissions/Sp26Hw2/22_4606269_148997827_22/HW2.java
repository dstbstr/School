public class CDoublyLinkedList {

	private class Node {
		private Object data;   //Assume data implemented Comparable
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

	// Add Object data to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method
	//      in your source code.
	public void addFirst(Object data) {
		Node nn = new Node(data, this.head, this.head.next);
		this.head.next.prev = nn;
		this.head.next = nn;
		this.size++;
	}


	public void addLast(Object data) {
		Node newNode = new Node(data, this.head.prev, this.head);
		this.head.prev.next = newNode;
		this.head.prev = newNode;
		size++;

	}


	public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
		CDoublyLinkedList B = new CDoublyLinkedList();
		Node current = this.head.next;
		while (current != this.head) {
			if (current.data != null) {
				Comparable currentData = (Comparable) current.data;
				if (currentData.compareTo(data) < 0) {
					B.addLast(current.data);
				}
				current = current.next;
			}
		}
		return B;
	}


	public boolean removeStartingAtBack(Object dataToRemove) {
		Node current = this.head.prev;
		while (current != this.head) {
			if (dataToRemove == null) {
				if (current.data == null) {
					current.prev.next = current.next;
					current.next.prev = current.prev;
					this.size--;
					return true;
				}
			} else {
				if (dataToRemove.equals(current.data)) {
					current.prev.next = current.next;
					current.next.prev = current.prev;
					this.size--;
					return true;
				}
			}
			current = current.prev;

		}

		return false;//change this as needed.
	}

	public int lastIndexOf(Object o) {
		int index = -1;
		int currentIndex = 0;
		Node current = this.head.next;

		while (current != this.head) {
			if (o == null) {
				if (current.data == null) {
					index = currentIndex;
				}
			} else {
				if (o.equals(current.data)) {
					index = currentIndex;
				}
			}
			current = current.next;
			currentIndex++;
		}

		return index;
	}


	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		if (other == null) {
			throw new NullPointerException();
		}
		boolean isChanged = false;
		Node current = this.head.next;
		while (current != this.head) {
			Node nextNode = current.next;
			if (!contains(other, current.data)) {
				current.prev.next = current.next;
				current.next.prev = current.prev;
				this.size--;
				isChanged = true;
			}
			current = nextNode;
		}


		return isChanged; //change this as needed.
	}

	private boolean contains(CDoublyLinkedList list, Object item) {
		Node current = list.head.next;

		while (current != list.head) {
			if (item == null) {
				if (current.data == null) {
					return true;
				}
			} else {
				if (item.equals(current.data)) {
					return true;
				}
			}
			current = current.next;
		}
		return false;
	}
	

	// i did use the notes provided from canvas + youtube + chatgpt to explain some of the code.
	public void insertionSort() {
    if (this.size <= 1) {
        return;
    }

    Node lastSorted;
    Node sortedWalker;
    Comparable firstUnsortedData;

    for (lastSorted = this.head.next; lastSorted != this.head.prev; lastSorted = lastSorted.next) {
        firstUnsortedData = (Comparable) lastSorted.next.data;

        for (sortedWalker = lastSorted;
             sortedWalker != this.head && ((Comparable) sortedWalker.data).compareTo(firstUnsortedData) > 0;
             sortedWalker = sortedWalker.prev) {

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
			
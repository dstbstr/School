import java.util.NoSuchElementException;


public class MyLinkedList {

	private ListNode head;
	private int size;

	//inner class for ListNode
	private class ListNode {
		private Object data;
		private ListNode next;
		private ListNode(Object d) {
			this.data = d;
			this.next = null;
		}
	}

	public MyLinkedList() {
		this.head = new ListNode(null); //with a dummy head node
		this.size = 0;
	}

	public int size() {
		return this.size;
	}


	public boolean isEmpty() {
		return size == 0;
	}


	// Add Object e to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method in your source code.
	public void addFirst(Object e)
	{
		ListNode node = new ListNode(e);
		node.next = head.next;
		head.next = node;
		size++;
	}

	// Remove(cut out) the first data node(the node succeeding the dummy node)
	//       in this list, then returns the data in the node removed.
	// If the size of this list is zero, throws an Exception.
	public Object removeFirst() throws Exception { //DONE
		if(size == 0){
			throw new IllegalArgumentException("List is empty");
		}
		ListNode first = head.next;
		Object data = first.data;
		head.next = first.next;
		size --;
		return data;


	}

	// Returns true if this list contains the specified element o.
	// More formally, returns true if and only if this list contains at least one element e
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) { //DONE
		int i = 0;
		ListNode current = head.next; //Start at the first data point in the list
		while(i < size){
			Object e = current.data;
			if (o == null ? e == null : o.equals(e)) {


				return true;
			}
			current = current.next;
			i++;
		}
		return false;
	}

	// Removes the first occurrence of the specified element o from this list and returns true, if it is present.
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) { //DONE
		int i = 0;
		ListNode current = head.next;
		ListNode previous = head;
		while(current != null){
			if(o == null ? get(i) == null: o.equals(get(i))){
				previous.next = current.next;
				size --;
				return true;
			}
			previous = current;
			current = current.next;
			i++;
		}
		return false;
	}


	// Removes all copies of o from this linked list.
	// You have to handle the cases where Object o may
	//        have zero, one or multiple copies in this list.
	// If any element has been removed from this list, returns true.
	//        Otherwise returns false.
	// Note: be careful when multiple copies of Object o are stored
	//        in consecutive(adjacent) list nodes.
	//        E.g. []->["A"]->["A"]->["B"].
	//        Be careful when removing all "A"s in this example.
	// Note: This list may contains zero, one or multiple copies of null data elements.
	public boolean removeAllCopies( Object o ) { //passed test
		boolean removed = false;
		while(remove(o)){
			removed = true;
		}
		if(removed){
			return true;
		}
		return false;
	}

	// Insert data elements from linkedlist A and B alternately into
	//    a new linkedlist C, then returns C.
	// Follow the pattern to pick items in list A and B,
	//        linkedlist A->linkedlist B->linkedlist A->linkedlist B …
	// If A is longer than B, append remaining items in A to C
	//     when the end of B is first reached.
	// If B is longer than A, append remaining items in B to C
	//     when the end of A is first reached.
	// E.g1 A = {1, 3, 5, 7, 9} and B = {2, 4, 6}; and
	//       C will be {1, 2, 3, 4, 5, 6, 7, 9}.
	//
	// E.g2 A = {1, 3, 5} and B = {2, 4, 6, 8, 10}; and
	//       C will be {1, 2, 3, 4, 5, 6, 8, 10}.
	// Note: after this method is called, both list A and B are UNCHANGED.
	public static MyLinkedList interleave(MyLinkedList A, MyLinkedList B) {
		MyLinkedList C = new MyLinkedList();
		ListNode currentA = A.head.next;
		ListNode currentB = B.head.next;
		while(currentA != null || currentB != null) {
			if (currentA != null){
				C.add(currentA.data);
				currentA = currentA.next;
			}
			if(currentB != null){
				C.add(currentB.data);
				currentB = currentB.next;
			}
		}
		return C;
	}

	// Inserts the specified element at the specified position in this list.
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.

	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].
	public void add(int index, Object o) { //DONE
		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException("Index is out of bounds");
		}
		ListNode nn = new ListNode(o);
		ListNode current = head.next;
		ListNode previous = head;
		int i = 0;
		while(i < index){
			previous = current;
			current = current.next;
			i++;

		}
		nn.next = current;
		previous.next = nn;
		size++;
	}



	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{ //DONE
		if(index < 0 || index >=size){
			throw new IndexOutOfBoundsException("Index out of bounds in get");
		}
		ListNode current = head.next;
		for(int i = 0; i < index; i++){
			current = current.next;
		}
		return current.data;
	}

	// Removes (cuts out) the list node at the specified index in this list.
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException { //DONE
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		ListNode current = head.next;
		ListNode prev = head;
		Object data;
		int i = 0;
		while(i < index){
			prev = current;
			current = current.next;
			i++;
		}
		if(current.data == null){
			prev.next = current.next;
			size --;
			return null;
		}else{
			data = current.data;
			prev.next = current.next;
			size --;
			return data;
		}
	}



	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) { //DONE
		ListNode nn = new ListNode(e);
		ListNode current = head;
		while(current.next != null){
			current = current.next;
		}
		current.next = nn;
		size ++;
		return true;
	}

	//Please DO NOT Change the following toString() method!!!
	//You must include the following toString() method in your source code.
	@Override
	public String toString() {
		String result = "{";
		for (ListNode node = this.head.next; node != null; node = node.next) {
			if(node.next != null)
				result += node.data + "->";
			else
				result += node.data;
		}
		return result + "}";
	}
}


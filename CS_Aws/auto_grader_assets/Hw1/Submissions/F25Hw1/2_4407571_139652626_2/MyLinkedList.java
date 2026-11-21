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
	public void addFirst(Object e) {
		ListNode node = new ListNode(e);
		node.next = head.next;
		head.next = node;
		size++;
	}

	// Remove(cut out) the first data node(the node succeeding the dummy node)
	//       in this list, then returns the data in the node removed.
	// If the size of this list is zero, throws an Exception.
	public Object removeFirst() throws Exception {
		// first make sure that our first node which is after dummynode
		// is not null if it is null then throw NoSuch element exception
		if (head.next == null) {
			throw new NoSuchElementException("No such element exists");

		}
		// if there is no null value then we go to the next part of our code
		// now assign our value to the node that we want to remove
		ListNode r = head.next;
		// now points the node that we want to remove to the value after that to remove it
		// r is the a value we want to remove if we suppose [dummy ]-[a] - [b] -[c]
		head.next = r.next;
		// now decrement the value to get rid of the node we want to remove
		size--;
		// we will be left with [dummy] - [b] - [c]
		return r.data;
		// this will return the A back like what node we removed
	}

	// Returns true if this list contains the specified element o.
	// More formally, returns true if and only if this list contains at least one element e
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {
		// start off with our real node which is head.next
		ListNode e = head.next;
		// if our first real node is not null we keep going
		while (e != null) {
			// if our node and the object we are looking for are both zero or not zero but equal then our condiiton is true
			if (o == null && e.data == null || o != null && o.equals(e.data)) {
				return true;

			}
			// keep going untit the end of the list
			e = e.next;


		}
		// if we could not find the item then we return false
		return false;
	}

	// Removes the first occurrence of the specified element o from this list and returns true, if it is present.
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {
		// first check if the node that we are looking is empty or zero
		// if it is then we have nothing to remove so return false
		if (isEmpty()) {
			return false;

		}
		// we can start with dummy in case we can just remove the real first node super easily
		ListNode e = head;
		// starting from the first real node make sure it is not zero and we keep going
		while (e.next != null) {
			// while going through the node if the object we want to remove and the node is zero they match
			if ((o == null && e.next.data == null) ||
					// if the object we are looking for and the node is not zero but they are equal they also match
					(o != null && o.equals(e.next.data))) {
				// now we found the object we want to remove and it is e.next just skip it by assigning it to next one
				e.next = e.next.next;
				// now we decrease the size of our linked list cuz we just removed one of the node
				size--;
				// now we return true cuz the operation we wanted to do is performed
				return true;
			}
			e = e.next;


		}
		return false;


	}

	// Removes all copies of o from this linked list.
	// You have to handle the cases where Object o may
	//        have zero, one or multiple copies in this list.
	// If any element has been removed from this list, returns true.
	//        Otherwise returns false.
	// Note: be careful when multiple copies of Object o are stored
	//        in consecute(adjacent) list nodes.
	//        E.g. []->["A"]->["A"]->["B"].
	//        Be careful when removing all "A"s in this example.
	// Note: This list may contains zero, one or multiple copies of null data elements.
	public boolean removeAllCopies(Object o) {//passed test
		// first see is our list is empty like there is nothing to remove
		if (isEmpty()) {
			// if it is empty then return false cuz there is nothing to remove
			return false;
		}
		// now we set the variable R to false to keep track of object being remove in this code
		// control logic control
		boolean R = false;
		// we start with the dummy head to make the process of removing easier
		ListNode e = head;
		// we go through each node starting from the first real node making sure it is not zero
		while (e.next != null) {
			// if our object and current node we looking at are null then condition is true
			if (o == null && e.next.data == null || o != null && o.equals(e.next.data)) {
				// if the condition is true then we remove the current node by assigning it to the next node
				e.next = e.next.next;
				// now decrease the size of our linked list
				size--;
				// now our conditon is true
				R = true;
			} else {
				e = e.next;
			}

		}

		return R;
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

		// first we make a new node that hold the result we can name it C
		MyLinkedList C = new MyLinkedList();
		// now we start our pointer from A and b skiping the dummy head
		ListNode a = A.head.next;
		ListNode b = B.head.next;
		// we when create a tail for C to now where to attach it
		ListNode tail = C.head;
		// now we need to create a loop that go over A and B linked List make sure they are not null
		// It also checks if they have value as soon as they are both null the loops stops
		while (a != null || b != null) {
			// now we see if the element is in linked list A, then we pick it up otherwise
			// we get our Node from linked list B
			if (a != null) {
				tail.next = C.new ListNode(a.data);
				// move our tail forward
				tail = tail.next;
				// increase the size of the linked list
				C.size++;
				// move our pointer in A forward
				a = a.next;
			}
			if (b != null) {
				tail.next = C.new ListNode(b.data);
				// move our tail forward
				tail = tail.next;
				// increase the size of the linked list
				C.size++;
				// move our pointer in B forward
				b = b.next;
			}
		}

		return C; // return the new list C
	}


	// Inserts the specified element at the specified position in this list.
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.

	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].
	public void add(int index, Object o) {
		// we want to insert element o in a sepcific position
		// make sure that our index is not zero or it is not bigger than the size of our linked list;
		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException();
		}
		// start from dummy head
		ListNode prev = head;
		// move prev to the node just before the target position
		for (int i = 0; i < index; i++){
			prev = prev.next;

		}
		// now we create a new node cuz we found the position we want to insert our nn
		ListNode nn = new ListNode(o);
		// points the new node to the node after prev
		nn.next = prev.next;
		// point the prev to new node
		prev.next = nn;
		// increase the size of our linked list
		size++;


	}


	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{
		// we want to get the element at the position of the index
		// make sure our index is not less than 0 or it is not equal or bigger than the size of our linked list
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();
		}
		// we use G to temporary goes over our list starting from the real node
		ListNode G = head.next;
		// now we use our for loop to move one node pre iteration

		for (int i = 0; i < index; i++){
		// this keeps our for loop going
			G = G.next;

		}
		// now if we got the element we want then we return it.
		return G.data;
	}

	// Removes (cuts out) the list node at the specified index in this list.
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException {
		// first make sure our index is not less or equal or bigger than the size of the
		// It ensures we are not removing an element that does not exist
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException();

		}
		// we start from the dummy node
		ListNode prev = head;
		//  keep moving the prev until it is before the node we want to remove
		for(int i = 0; i < index; i++){
		// this keep the for loop moving
			prev = prev.next;



		}
		// T stands for the target we want to remove
		// assign T to the node ahead of the prev
		ListNode T = prev.next;
		// assign the pointer of the prev to the node ahead of the target cuz it removes the target by skipping it
		prev.next = T.next;
		// now decrease the size of our linked list
		size--;

        // return the removed element cuz our method is object
		return T.data; //change this as you need.
	}


	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {
		// we start with dummy head
		ListNode E = head;
		// keep going until we reach the end of the node
		// create a while loop and it keeps going as long as there is no element after our E.next
		while (E.next != null){
			E = E.next;

		}
		// now we create the new node that contains the data we want to add
		// E is bisically our last node so by pointing it out to the new node we add the new  node
		E.next = new ListNode(e);
		// increase the size of our list
		size++;
		// return true cuz we complete the process and it is a boolean data type
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

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
		// we first make a new node to store our new data which is new node
		// now we want to connect the new node to the last node which is head.prv . it means head is the first node
		// and head.prev is the last node
		Node nn = new Node(data , head.prev, head);
		// now we want to assign the pointer of the last node which is head.prev to our new node we wanna add
		head.prev.next = nn;
		// now we assign the prev poiter of the head which is our first node to the node we just added
		head.prev = nn;
		// now increase the size of our doubly linked list
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
	// first we create a new empty list to store all of our smaller data than given data
	CDoublyLinkedList smallList = new CDoublyLinkedList();
	// we start our for loop assign cur to head.next becasue it is real node plus whenever we reach head we stop the loop
		// cuz it is a circular list and it goes around itself so make sure it is not equal head
		// cur = cur.next make sure our list keep going forward
	for(Node cur = head.next; cur != head; cur = cur.next) {
		// first we want to skip the node that do node have any data so cur.data != null;
		//now first part we want to make sure our cur.data is comparable
		// also we want to make sure our cur.data is less than zero so it is samller then the given value
		if (cur.data != null && ((Comparable) cur.data).compareTo(data) < 0) {
			// now both of our condition is true we want to add the data to our new list to the end of it
			smallList.addLast(cur.data);

		}
	}
	// we return our small list
	return smallList;





	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
		// first we create a loop start from last node moving backwords, it is a circular list so when ever
		// it reaches the head it stops cur = cur.prev it keeps loops going backword once at a time.
		for(Node cur = head.prev; cur != head; cur = cur.prev){
			// check if the data in the current node matches the data we want to remove
			if(Objects.equals(cur.data, dataToRemove)){
				// now we want to change the pointer of the node before the cur node to point to the node after cur
				// basically skipping the node
				cur.prev.next = cur.next;
				// now we want to assign the prev pointer of the node after cur to point to the node before cur
				cur.next.prev = cur.prev;
				// now cur is being removed and size should decrease
				size--;
				// our process is true so return true
				return true;
			}
		}
		// if our process is not accomplished return false
		return false;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
		// we want to return the last occurence of object o if object o appear mutiple times we want to return the
		// one with the hightest index so first we need to make sure that the list is not empty and has actual value in it
		// otherwise we return -1
		if (isEmpty()) return -1;
		// now we wanna start from the last index in the list
		// because the first element has index of zero we use size -1 we do not want our loop to goes out of the list
		// cuz our list is zero based meaning it starts from zero
		int N = size - 1;
		// we first initialize our cur and then assign it to the node before head wich is our last node
		//make sure when ever our cur reaches the head stops the loop cuz so it can not be equal head
		// keep our loop going by cur = cur.prev and make sure we decrement when to keep track of our loop and keeps going down the list
		for (Node cur = head.prev; cur != head; cur = cur.prev, N--){
			// we used Objects.equals built in method in java it handles the case if cur or object o are null return true or
			// if one of them are null return false without throw null pointer exception
			// it is bisically saying if cur.data == null ? o == null : cur.data.equals(o);
		if (Objects.equals(cur.data, o))
		// if we find the match we reuturn the node with the hightest index
		return N;
	}
		// if the loop finishes without finding a match then the loop finishes and return -1

	return -1;




	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		  // first we check if the other list is null if it is immediatly stop and throw null point exception
			if (other == null) throw new NullPointerException("Other list is null");


			boolean changed = false;
            // this loop go through each node starting from real node and make sure loop stops when cur = head
			for (Node cur = head.next; cur != head; ) {
				// we have to save the next node before removing it cuz it has next and prev pointer and we do not wanna lose track of it
				Node next = cur.next;
				// now we need to handle if the currnet node is not found in the other list
				if (!other.contains(cur.data)) {
					// if the current node is not  found in other list then we remove it
					removeNode(cur);
					// since we removed the node the process has been completed so return true
					changed = true;
				}
				// this keep  the loop moving
				cur = next;
			}
            // this will return if there has been any changed made or not still return it 
			return changed;
		}
		// helper to remove the node
		private void removeNode(Node node) {
		    // we need to assign the next pointer of prev node to the next node after current node to skip and remove currrent

			node.prev.next = node.next;
			// now assig the prev pointer of the node after cur to the node before cur to skip cur and remove it
			node.next.prev = node.prev;
			// decrease size of our list
			size--;
		}

       // we need this helper to handle null point excepiton it goes through each node in doubly linked list
		public boolean contains(Object o) {
		// start from the real node ,cur = cur.next keeps the loop going , when it gets to the head break the for loop and
			for (Node cur = head.next; cur != head; cur = cur.next) {
				// we uses Objects method to handle the null value without throwing an error
				// it checks if the current data matches the data o we are looking in the list
				if (Objects.equals(cur.data, o))
					// if process complete return true
					return true;
			}
			// if process did not complete return false
			return false;
		}









        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		// first if the list has 1 or zero element there is nothing to sort so just return
		if (size <= 1) return;
		// we start from the second node cuz the first node is already considered sorted
		Node sL = head.next.next;
		// Sl means our sortlast meaning everything before this is sorted and every thing after this is not sorted
		// we want to go through unsorted method
		while (sL != head) {
			// cur here is the node we want to place in the sortedList
			Node cur = sL;
			// compN stands for compareNode it scan backword to see where the node belongs
			Node compN = sL.prev;

			// it store the value we want to insert temporary to make sure we do not lose track of the node while
			// shifting other elements
			Object curData = cur.data;

			// as long as we have nto reach the head and also current data is bigger than the value we want to insert as long as there are comparable
			while (compN != head && ((Comparable) compN.data).compareTo(curData) > 0) {
				// this move each large data one step to the right to make space for the node we wanna add

				compN.next.data = compN.data;
				//keep the node moving backword
				compN = compN.prev;
			}

			// when we foud the position we want to insert our data this line of code does it for us
			compN.next.data = curData;
			// shift the node one step forward expanding the sorting region by one node
			sL = sL.next;
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
			
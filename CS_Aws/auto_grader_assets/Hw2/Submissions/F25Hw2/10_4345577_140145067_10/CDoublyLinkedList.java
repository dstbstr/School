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

    // write a method void addLast(Object data) that will insert
    // the piece of data at the end of the current list.
    // Note: this list is allowed to store null data element in its list node.
    public void addLast(Object data) {

        Node nn = new Node(data, this.head.prev, this.head); // Helps create a new node that is between the head and the current last node
        this.head.prev.next = nn; // Connects the last node to the new one
        this.head.prev = nn; // Connects the previous head pointer to the new node
        this.size++; // Increases the size of the node

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

        CDoublyLinkedList result = new CDoublyLinkedList(); // Creates a new empty CDoublyLinked List

        // Travels from the front and the back of the new list
        for (Node curr = this.head.next; curr != this.head; curr = curr.next) {

            if (curr.data == null);
            Comparable currentData = (Comparable) curr.data; // Makes the current node data linked to comparable

            if (currentData.compareTo(data) < 0) {

                result.addFirst(currentData); // Adds to the new created list called 'result'

            }
        }

        return result; //change this as needed.
    }

    // This method should remove the first occurrence of the data from the list,
    //      starting at the *BACK* of the list.
    // It scans the list from back to the front by following the prev links.
    // The method should return true if successful, false otherwise.
    // Note that list node may contain null data element. Please handle this edge case.
    public boolean removeStartingAtBack(Object dataToRemove) {

        // Travels backwards from the list's last element
        for (Node curr = this.head.next; curr != this.head; curr = curr.prev) {

            if ((dataToRemove == null && curr.data == null) || (dataToRemove != null && curr.data.equals(dataToRemove))) {

                // Removes current nodes
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;

                this.size--; // Decreases size of the node
                return true; // Successful removal

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

        int result = this.size - 1; // The index of the node is equal to size -1, if it doesn't match, it will return -1

        // Travels backwards to the end of the node
        for (Node curr = this.head.next; curr != this.head; curr = curr.next) {

            // Compares the nodes and checks if the answer is null
            if ((o == null && curr.data == null) || (o != null && o.equals(curr.data))) {
                return result;
            }
        }

        return -1; //change this as needed.
    }


    // Removes from this list all of its elements that
    //    are NOT contained in the specified linked list other.
    // If any element has been removed from this list,
    //    returns true. Otherwise, returns false.
    // If other list is null, throws NullPointerException.
    // Helper methods are allowed.
    public boolean retainAll(CDoublyLinkedList other) {

        // If the element 'other' is null, NullPointerException in required
        if (other == null) {
            throw new  NullPointerException();
        }

        boolean givenResult = false; // Checks if the element is removed from the node
        Node curr = this.head.next;

        // Travels through the node
        while (curr != other.head) {
            Node next = curr.next; // Saves the next node before the current is possibly removed
            boolean found  = false; // If the data in the element 'other' is found true

            // Searches for the current data in the element 'other' in the list
            for (Node otherCur = other.head.next; otherCur != other.head; otherCur = otherCur.next) {
                if ((curr.data == null && otherCur.data == null) || (curr.data != null && curr.data.equals(otherCur.data))) {

                    found = true;
                    break; // Found the match in the list

                }
            }

            // If the current data for the node isn't found in the 'other', it will be removed from the list
            if (!found) {
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                this.size--;
                givenResult = true;
            }

            curr = curr.next; // Moves to the next node
        }

        return givenResult; //change this as needed.
    }


    // Write this method to sort this list using insertion sort algorithm,
    //      as we have learned in the classroom.
    public void insertionSort() {

        // If the list size is less or equal to 1, it will sort nothing
        if (this.size <= 1) {
            return;
        }

        Node current = this.head.next.next; // Sorts the second node

        // Passes through all the nodes until it is sorted in the correct order
        while (current != this.head) {
            Node next = current.next;
            Node prev = current.prev;

            // Removes the current list from the node temporarily before it is returned
            current.prev.next = current.next;
            current.next.prev = current.prev;

            // Moves the node backwards so that it can find where the current node is placed in the list
            while (prev != this.head && ((Comparable) prev.data).compareTo((Comparable) current.data) > 0) {

                prev = prev.next;
            }

            // After prev is found and matched, it inserts the current node
            current.next = prev.next;
            current.prev = prev;
            prev.next.prev = current;
            prev.next = current;

            current = next; // Move to the next node was originally the current node
        }
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
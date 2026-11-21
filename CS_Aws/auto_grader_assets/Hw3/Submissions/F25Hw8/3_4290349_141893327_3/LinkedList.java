public class LinkedList {

    /**
     * This Node class constructs the nodes we'll be using in the methods below.
     */
    private static class Node {
        Object data;
        Node next;

        Node(Object d) {
            data = d;
            next = null;
        }
    }


    // These are the variables we'll be using for the linked list
    private Node head;
    private Node tail;
    private int size;


    /**
     * Tis linked list uses an NDH data structure without a dymmy head.
     */
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }


    /**
     * This simply returns the size of a given LinkedList.
     * @return
     */
    public int getSize() {
        return this.size;
    }

    /**
     * This checks if a linked list is empty by testing whether this.size == 0 is true or false.
     * It then returns the result of this check.
     * @return
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * This method first leverages the isEmpty() method to check if the list is empty
     * If it is, there is no point in getting the first node cause it doesn't exist.
     *
     * Otherwise, we just return the first node that is at this.head.data (non dummy head).
     * @return
     */
    public Object getFirst() {
        if (isEmpty())
            throw new RuntimeException("List is empty");
        return this.head.data;
    }


    /**
     * This method adds a new node to a given linked list
     * It first creates a new node that will be added later.
     * Then it assigns the next value for the newNode as the current head node.
     *
     * Then it checks whether the tail node is null, if it is tail gets the newNode.
     *
     * Finally the program increases the size by 1 after adding the newNode.
     * @param value
     */
    public void addFirst(Object value) {
        Node newNode = new Node(value);
        newNode.next = this.head;
        this.head = newNode;

        if (this.tail == null)
            this.tail = newNode;

        this.size++;
    }


    /**
     * This method works similar to the add first method except it adds a newNode to the end of the list
     * The method first creates a newNode for later use.
     *
     * It checks whether the list is empty, if it is then both the head and tail become the newNode.
     *
     * Otherwise, the tail's next position (the new tail) is the newNode. This is further solidified when we
     * assign this.tail to newNode.
     *
     * Finally, the program increases the size by 1.
     * @param value
     */
    public void addLast(Object value) {
        Node newNode = new Node(value);

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
        } else {
            this.tail.next = newNode;
            this.tail = newNode;
        }

        this.size++;
    }


    /**
     * This method removes the first node in a given linked list and returns the data from that removed node.
     *
     * It first checks if the list is empty and throws an error if it is.
     *
     * Next, an object variable is assigned the data at the first node and then the first node gets changed.
     *
     * Next if the head is now null (meaning the list is empty) the tail is also set to null to prevent any
     * errors.
     *
     * Finally, the size of the linked list gets shrunk by 1 and the value of that original first node gets
     * returned.
     * @return
     */
    public Object removeFirst() {
        if (isEmpty())
            throw new RuntimeException("Cannot remove from empty list");

        Object value = this.head.data;
        this.head = head.next;

        if (this.head == null)
            this.tail = null;

        this.size--;
        return value;
    }

    /**
     * This method returns a boolean true or false of whether a given linked list is sorted.
     *
     * It first checks if the size is less than or equal to 1, in which case the list is already sorted.
     * Otherwise, it proceeds to set Node's cur and next to this.head and this.head.next.
     *
     * While next isn't null, we set ints a and b to the data from cur and next at a particular iteration.
     * Since the list is sorted smallest to largest left to right, if a is ever greater than b, the list
     * isn't sorted. If this condition is ever reached we return false.
     *
     * If an iteration passed and is sorted, cur gets next and next gets next.next and we continue to
     * repeat the process. Once all the conditions are passed and assuming a false value hasn't been
     * returned, we return true since the whole list was in fact sorted.
     * @return
     */
    public boolean isSorted() {
        if (this.size <= 1) return true;

        Node cur = this.head;
        Node next = this.head.next;

        while (next != null) {
            int a = (Integer) cur.data;
            int b = (Integer) next.data;
            if (a > b) return false;
            cur = next;
            next = next.next;
        }
        return true;
    }

    /**
     * This program takes in two sorted linked lists and returns a sorted merged linked list.
     * This is a helper method for the mergeSort method.
     *
     * First the program creates a new and empty linkedList S that will hold the final output.
     *
     * The first while loop runs while both linked lists A and B are not empty. In this while loop,
     * the program assigns the values of the first node for both A and B to int's fa and fb. If
     * If fa is less than or equal to fb, fa is added to S and if fb is smaller than b gets added instead.
     *
     * Once either A or B is empty, we move to the next while loop that adds the rest of A if it's empty.
     * If A is already empty but B is the one still with nodes, the third while loop adds all the remaining
     * nodes from B.
     *
     * Finally, the program returns S.
     * @param A
     * @param B
     * @return
     */
    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();

        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = (Integer) A.getFirst();
            int fb = (Integer) B.getFirst();

            if (fa <= fb)
                S.addLast(A.removeFirst());
            else
                S.addLast(B.removeFirst());
        }

        while (!A.isEmpty())
            S.addLast(A.removeFirst());

        while (!B.isEmpty())
            S.addLast(B.removeFirst());

        return S;
    }

    /**
     * This method runs the mergeSort algorithm to sort a linked list in O(1) runtime.
     *
     * First the program checks if the list is <= 1 and if so we just return cause the list is already sorted.
     * Otherwise, a new Queue called q is made to work with for merge sort.
     * The first while loop runs while the linked list isn't empty and basically removes each first node
     * from the original linked list while simultaneously making it into a newly made linked list called single.
     * Finally, the while loop takes each single linked list that holds one node from the original linked list
     * and enqueues it into q. This process repeats until we've exhausted the original linked list.
     *
     * The next phase is to split the current queue into two different linked lists called A and B by alternating
     * the dequeue process on q between A and B while q.size() > 1. Part of making this possible is that we cast
     * the two dequeue calls as LinkedLists so that they can be added to the linked lists A and B. Next, the while
     * loop calls merged each time which recombines A and B which are really just nodes in disguise into a sorted
     * sub-linked list. Finally, we enqueue the resulting merged call onto q and repeat the process until q size
     * finally hits 1. The idea here is that q gets smaller and smaller in terms of nodes cause the first node
     * will be a sorted linked list that gets bigger and bigger while the unsorted remaining nodes continue to get
     * smaller and smaller before all that is left in q is one sorted linked list node.
     *
     * In the last portion of the program, we need to make a new linked list called sorted that takes the dequeue
     * output of q cast as a LinkedList. Finally, a while loop adds the first removed nodes from sorted to the end
     * of the original linked list. This occurs until sorted has been exhausted. The reason we do this is that
     * we haven't yet changed the original linked list apart from emptying it, and we still need to make the original
     * linked list into a sorted one.
     *
     * And finally, the whole program concludes leaving behind a sorted linked list one way or another. Piece of cake
     * right?
     */
    public void MergeSort() {
        if (size <= 1) return;

        Queue q = new Queue();

        // Step 1: break into 1‑element lists
        while (!this.isEmpty()) {
            LinkedList single = new LinkedList();
            single.addFirst(this.removeFirst());
            q.enqueue(single);
        }

        // Step 2: repeatedly merge pairs
        while (q.size() > 1) {
            LinkedList A = (LinkedList) q.dequeue();
            LinkedList B = (LinkedList) q.dequeue();
            LinkedList merged = merge(A, B);
            q.enqueue(merged);
        }

        // Step 3: overwrite this list with sorted result
        LinkedList sorted = (LinkedList) q.dequeue();
        while (!sorted.isEmpty())
            this.addLast(sorted.removeFirst());
    }


    public void InsertionSort() {
        if (size <= 1) return;

        LinkedList sorted = new LinkedList();

        while (!this.isEmpty()) {
            int value = (Integer) this.removeFirst();

            LinkedList temp = new LinkedList();

            while (!sorted.isEmpty() && (Integer) sorted.getFirst() < value)
                temp.addLast(sorted.removeFirst());

            temp.addLast(value);

            while (!sorted.isEmpty())
                temp.addLast(sorted.removeFirst());

            while (!temp.isEmpty())
                sorted.addLast(temp.removeFirst());
        }

        while (!sorted.isEmpty())
            this.addLast(sorted.removeFirst());
    }
}

public class LinkedList {

    private ListNode head;
    private int size;

    private class ListNode {
        Object data;
        ListNode next;

        ListNode(Object d, ListNode n) {
            this.data = d;
            this.next = n;
        }
    }
    public void add(int index, Object toAdd) {
        ListNode cur = head;
        int i = 0;
        while (i < index - 1 && cur != null) {
            cur = cur.next;
            i++;
        }
        ListNode nn = new ListNode(toAdd, cur.next);
        cur.next = nn;
        size++;
    }
    void addAll(ArryaList other){

    }
}

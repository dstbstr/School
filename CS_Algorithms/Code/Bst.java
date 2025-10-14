
public class Bst {
    class Node {
        public Node(int value) {
            this.Value = value;
        }
        int Value;
        Node Left = null;
        Node Right = null;
    }
    private class BeginEnd {
        public Node Begin;
        public Node End;
        public BeginEnd(Node b, Node e) {
            Begin = b;
            End = e;
        }
    }

    private Node root = null;

    private void InsertRec(Node node, int val) {
        if(val < node.Value) {
            if(node.Left == null) {
                node.Left = new Node(val);
            } else {
                InsertRec(node.Left, val);
            }
        } else {
            if(node.Right == null) {
                node.Right = new Node(val);
            } else {
                InsertRec(node.Right, val);
            }
        }
    }
    public void Insert(int val) {
        if(root == null) {
            root = new Node(val);
        } else {
            InsertRec(root, val);
        }
    }

    public Node Find(int val) {
        Node curr = root;
        while(curr != null) {
            if(curr.Value == val) return curr;
            curr = val < curr.Value ? curr.Left : curr.Right;
        }
        return null;
    }

    private BeginEnd ToDllRec(Node root) {
        if(root == null) return null;
        Node Begin = root;
        Node End = root;
        if(root.Left != null) {
            BeginEnd left = ToDllRec(root.Left);
            Begin = left.Begin;
            root.Left = left.End;
            left.End.Right = root;
        }
        if(root.Right != null) {
            BeginEnd right = ToDllRec(root.Right);
            End = right.End;
            root.Right = right.Begin;
            right.Begin.Left = root;
        }
        return new BeginEnd(Begin, End);
    }

    public void WalkInOrder(Node root) {
        if(root == null) return;
        WalkInOrder(root.Left);
        System.out.println(root.Value);
        WalkInOrder(root.Right);
    }
    public Node ToDoubleLinkedList() {
        if(root == null) return null;
        return ToDllRec(root).Begin;
    }
}
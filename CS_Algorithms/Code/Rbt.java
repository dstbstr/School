public class Rbt {
    public enum Color {Red, Black}
    public class Node {
        public Node(int value, Color color) {
            this.Value = value;
            this.Color = color;
        }
        public int Value;
        public Color Color;
        public Node Left = null;
        public Node Right = null;
        public Node Parent = null;
    }

    private Node root = null;
    public Rbt() {
        root = null;
    }

    public Rbt Clone() {
        Rbt newTree = new Rbt();
        CloneRec(this.root, newTree);
        return newTree;
    }

    private void CloneRec(Node oldNode, Rbt newTree) {
        if(oldNode == null) return;
        newTree.Insert(oldNode.Value);
        CloneRec(oldNode.Left, newTree);
        CloneRec(oldNode.Right, newTree);
    }

    public Node Find(int val) {
        Node curr = root;
        while(curr != null) {
            if(curr.Value == val) return curr;
            curr = val < curr.Value ? curr.Left : curr.Right;
        }
        return null;
    }

    public void Print() {
        PrintRec(root, "", true);
    }
    private void PrintRec(Node node, String prefix, boolean isTail) {
        if(node == null) return;
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.Value + "(" + (node.Color == Color.Red ? "R" : "B") + ")");
        PrintRec(node.Left, prefix + (isTail ? "    " : "│   "), false);
        PrintRec(node.Right, prefix + (isTail ? "    " : "│   "), true);
    }

    public boolean IsValidRbt() {
        if(root == null) return true;
        if(root.Color != Color.Black) return false;

        return ValidateRecursive(root.Left, 0) && ValidateRecursive(root.Right, 0);
    }

    private boolean ValidateRecursive(Node node, int blackCount) {
        if(node == null) return true;
        if(node.Color == Color.Red && node.Parent.Color == Color.Red) return false;
        if(node.Color == Color.Black) blackCount++;
        return ValidateRecursive(node.Left, blackCount) && ValidateRecursive(node.Right, blackCount);
    }
    /// INSERT

    private Node BasicInsert(int val) {
        Node newNode = new Node(val, Color.Red);
        if(root == null) {
            root = newNode;
            return newNode;
        }

        Node curr = root;
        while(curr != null) {
            if(curr.Value < val) {
                if(curr.Right == null) {
                    curr.Right = newNode;
                    newNode.Parent = curr;
                    break;
                } else {
                    curr = curr.Right;
                }
            } else {
                if(curr.Left == null) {
                    curr.Left = newNode;
                    newNode.Parent = curr;
                    break;
                } else {
                    curr = curr.Left;
                }
            }
        }
        return newNode;
    }


    private enum ZigZag {
        LeftLeft,
        LeftRight,
        RightRight,
        RightLeft
    }

    private ZigZag GetZigZag(Node node) {
        if(node == node.Parent.Left) {
            return node.Parent == node.Parent.Parent.Left ? ZigZag.LeftLeft : ZigZag.RightLeft;
        } else {
            return node.Parent == node.Parent.Parent.Left ? ZigZag.LeftRight : ZigZag.RightRight;
        }
    }

    private Node InsertRule1(Node node, Node uncle) {
        node.Parent.Color = Color.Black;
        uncle.Color = Color.Black;
        node.Parent.Parent.Color = Color.Red;
        return node.Parent.Parent;
    }

    private Node InsertRule2(Node node) {
        if(node == node.Parent.Right && node.Parent == node.Parent.Parent.Left) {
            RotateLeft(node.Parent);
            return node.Left;
        } else {
            RotateRight(node.Parent);
            return node.Right;
        }
    }

    private Node InsertRule3(Node node) {
        SwapColor(node.Parent, node.Parent.Parent);
        if(node == node.Parent.Left && node.Parent == node.Parent.Parent.Left) {
            RotateRight(node.Parent.Parent);
        } else {
            RotateLeft(node.Parent.Parent);
        }
        return node;
    }

    public void Insert(int val) {
        Node newNode = BasicInsert(val);
        while(newNode.Parent != null && newNode.Parent.Color == Color.Red) {
            Node uncle = Uncle(newNode);
            if(uncle != null && uncle.Color == Color.Red) {
                newNode = InsertRule1(newNode, uncle);
            } else {
                ZigZag zz = GetZigZag(newNode);
                if(zz == ZigZag.LeftRight || zz == ZigZag.RightLeft) {
                    newNode = InsertRule2(newNode);
                }
                newNode = InsertRule3(newNode);
            }
        }
        root.Color = Color.Black;
    }

    public void Insert(int... values) {
        for(int val : values) {
            Insert(val);
        }
    }
    
    /// DELETE

    public void Delete(int val) {
        Node x = BasicDelete(val);
        if(x != null) {
            DeleteFixup(x);
        }
    }

    private Node BasicDelete(int val) {
        Node z = Find(val);
        if(z == null) return null;
        Node y = (z.Left == null || z.Right == null) ? z : Successor(z);
        Node x = (z.Left != null) ? z.Left : z.Right;
        if(x != null) {
            x.Parent = y.Parent;
        }
        if(y.Parent == null) {
            root = x;
        } else if( y == y.Parent.Left) {
            y.Parent.Left = x;
        } else {
            y.Parent.Right = x;
        }

        z.Value = y.Value;
        return x;
    }

    private void DeleteFixup(Node x) {
        while(x.Parent != null && x.Color == Color.Black) {
            Node w = Sibling(x);
            if(w.Color == Color.Red) {
                DeleteRule1(x);
            } else {
                Node near = NearNephew(x);
                Node far = FarNephew(x);

                if(near.Color == Color.Black && far.Color == Color.Black) {
                    x = DeleteRule2(w);
                } else if(near.Color == Color.Red && far.Color == Color.Black) {
                    DeleteRule3(near);
                } else {
                    x = DeleteRule4(far);
                }
            }
        }
        if(root != null) {
            root.Color = Color.Black;
        }
    }

    private void DeleteRule1(Node node) {
        if(node == node.Parent.Left) {
            RotateLeft(node.Parent);
        } else {
            RotateRight(node.Parent);
        }
        SwapColor(node.Parent, node.Parent.Parent);
    }

    private Node DeleteRule2(Node w) {
        w.Color = Color.Red;
        return w.Parent;
    }

    private void DeleteRule3(Node near) {
        SwapColor(near, near.Parent);
        if(near == near.Parent.Left) {
            RotateRight(near.Parent);
        } else {
            RotateLeft(near.Parent);
        }
    }

    private Node DeleteRule4(Node far) {
        SwapColor(far.Parent, far.Parent.Parent);
        if(far == far.Parent.Left) {
            RotateRight(far.Parent.Parent);
        } else {
            RotateLeft(far.Parent.Parent);
        }
        far.Color = Color.Black;
        return root;
    }



    private Node Uncle(Node node) {
        if(node.Parent == null || node.Parent.Parent == null) return null;
        if(node.Parent == node.Parent.Parent.Left) {
            return node.Parent.Parent.Right;
        } else {
            return node.Parent.Parent.Left;
        }
    }

    private Node Sibling(Node node) {
        if(node.Parent == null) return null;
        return node == node.Parent.Left ? node.Parent.Right : node.Parent.Left;
    }

    private Node NearNephew(Node node) {
        if(node.Parent == null) return null;
        return node == node.Parent.Left ? node.Parent.Right.Left : node.Parent.Left.Right;
    }

    private Node FarNephew(Node node) {
        if(node.Parent == null) return null;
        return node == node.Parent.Left ? node.Parent.Right.Right : node.Parent.Left.Left;
    }

    private Node Successor(Node node) {
        if(node.Right != null) {
            Node curr = node.Right;
            while(curr.Left != null) {
                curr = curr.Left;
            }
            return curr;
        } else {
            Node curr = node;
            while(curr.Parent != null) {
                if(curr == curr.Parent.Left) {
                    return curr.Parent;
                }
                curr = curr.Parent;
            }
            return null; // no successor
        }
    }

    private void RotateLeft(Node root) {
        Node right = root.Right;
        root.Right = right.Left;
        if(right.Left != null) {
            right.Left.Parent = root;
        }
        right.Parent = root.Parent;
        if(root.Parent == null) {
            this.root = right;
        } else if(root == root.Parent.Left) {
            root.Parent.Left = right;
        } else {
            root.Parent.Right = right;
        }
        right.Left = root;
        root.Parent = right;
    }

    private void RotateRight(Node root) {
        Node left = root.Left;
        root.Left = left.Right;
        if(left.Right != null) {
            left.Right.Parent = root;
        }
        left.Parent = root.Parent;
        if(root.Parent == null) {
            this.root = left;
        } else if(root == root.Parent.Left) {
            root.Parent.Left = left;
        } else {
            root.Parent.Right = left;
        }
        left.Right = root;
        root.Parent = left;
    }

    private void SwapColor(Node a, Node b) {
        Color temp = a.Color;
        a.Color = b.Color;
        b.Color = temp;
    }
}

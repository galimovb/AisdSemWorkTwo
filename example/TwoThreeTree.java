package org.example;
public class TwoThreeTree {
    public Node root;
    int insIterations = 0;
    int searchIterations = 0;
    int remIterations= 0;

    public void insert(int key) {
        if (root == null) {
            root = new Node(key);
            insIterations++;
        } else {
            Node current = root;
            while (true) {
                if (current.isLeaf()) {
                    current.insertToNode(key);
                    if (current.isOverflow()) {
                        splitNode(current);
                    }
                    insIterations++;
                    break;
                } else {
                    if (key <= current.keys[0]) {
                        current = current.children[0];
                    } else if (current.numKeys == 1 || (current.numKeys == 2 && key < current.keys[1])) {
                        current = current.children[1];
                    } else {
                        current = current.children[2];
                    }
                    insIterations++;
                }
            }
        }
    }
    private void splitNode(Node node) {
        Node left = new Node(node.keys[0]);
        Node right = new Node(node.keys[2]);
        Node parent = node.parent;
        if (!node.isLeaf()) {
            left.setChild(node.children[0]);
            left.setChild(node.children[1]);
            right.setChild(node.children[2]);
            right.setChild(node.children[3]);
        }
        if (node.parent == null) {
            root = new Node(node.keys[1]);
            root.setChild(left);
            root.setChild(right);
            left.parent = root;
            right.parent = root;
        } else {
            parent.replaceChild(node, left);
            parent.addChild(right);
            parent.insertToNode(node.keys[1]);
            if (parent.isOverflow()) {
                insIterations++;
                splitNode(node.parent);
            }
        }
        insIterations++;
    }

    public Node search(int value) {
        Node node = root;
        while (node != null) {
            searchIterations++;
            remIterations++;
            if (node.contains(value)) {
                return node;
            } else {
                node = node.getNextNode(value);
            }
        }
        return null;
    }

    public void remove(int k) {
        Node p = root;
        Node node = search(k);
        Node min;
        if (node != null){
            if (node.keys[0] == k) {
                min = p.searchMin(node.children[1]);
            } else{
                min = p.searchMin(node.children[2]);
            }
            remIterations++;
            if (min != null) {
                int z = (k == node.keys[0] ? node.keys[0] : node.keys[1]);
                int buf = z;
                z = min.keys[0];
                min.keys[0] = buf;
                node = min;
            }
            node.removeFromNode(k);
            remIterations++;
            UpdateAfterDelete(node);
        }
    }


    public Node UpdateAfterDelete(Node node) {
        if (node == null){
            return null;
        }
        if (node.numKeys == 0 && node.parent == null) {
            return null;
        }
        if (node.numKeys != 0) {
            if (node.parent != null) {
                remIterations++;
                return UpdateAfterDelete(node.parent);
            } else {
                return node;
            }
        }
        Node parent = node.parent;
        if (parent.children[0].numKeys == 2 || parent.children[1].numKeys == 2 || parent.numKeys == 2) {
            node = parent.redistribute(node);
        } else if (parent.numKeys == 2 && parent.children[2].numKeys == 2) {
            node = parent.redistribute(node);
        } else {
            node = merge(node);
        }
        remIterations++;
        return UpdateAfterDelete(node);
    }


    Node merge(Node leaf) {
        Node par = leaf.parent;
        if (par.children[0] == leaf) {
            par.children[1].insertToNode(par.keys[0]);
            par.children[1].children[2] = par.children[1].children[1];
            par.children[1].children[1] = par.children[1].children[0];

            if (leaf.children[0] != null) {
                par.children[1].children[0]= leaf.children[0];
            } else if (leaf.children[1] != null) {
                par.children[1].children[0]= leaf.children[1];
            }
            if (par.children[1].children[0] != null) {
                par.children[1].children[0].parent = par.children[1];
            }

            par.removeFromNode(par.keys[0]);
            par.children[0] = null;
        }
        else if (par.children[1] == leaf) {
            par.children[0].insertToNode(par.keys[0]);

            if (leaf.children[0] != null) {
                par.children[0].children[2] = leaf.children[0];
            } else if (leaf.children[1] != null) {
                par.children[0].children[2] = leaf.children[1];
            }
            if (par.children[0].children[2] != null) {
                par.children[0].children[2].parent = par.children[0];
            }
            par.removeFromNode(par.keys[0]);
            par.children[1] = null;
        }
        if (par.parent == null) {
            Node tmp;
            if (par.children[0] != null) {
                tmp = par.children[0];
            } else {
                tmp = par.children[1];
            }
            tmp.parent = null;
            par = null;
            return tmp;
        }
        remIterations++;
        return par;

    }
}

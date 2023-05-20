package org.example;

public class Node {
    Node parent;
    int numKeys;
    int[] keys;
    Node[] children;

    public Node(){
        this.keys = new int[3];
        this.numKeys = 0;
        this.children = new Node[4];
        this.parent = null;
    }

    public Node(int k){
        this();
        this.keys[0] = k;
        this.numKeys = 1;
    }

    public boolean isLeaf(){
        return children[0] == null;
    }

    public boolean isOverflow() {
        return numKeys == 3;
    }
    public boolean contains(int value) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == value) {
                return true;
            }
        }
        return false;
    }

    public void insertToNode(int k) {
        if(numKeys == 0){
            keys[0] = k;
            numKeys++;
        }
        else if (numKeys == 1) {
            if (k < keys[0]) {
                keys[1] = keys[0];
                keys[0] = k;
            } else {
                keys[1]= k;
            }
            numKeys++;
        }else {
            if (k < keys[0]) {
                keys[2] = keys[1];
                keys[1] = keys[0];
                keys[0] = k;
            } else if (k < keys[1]) {
                keys[2] = keys[1];
                keys[1] = k;
            } else {
                keys[2] = k;
            }
            numKeys++;
        }
    }
    public void setChild(Node child) {
        if (children[0] == null) {
            children[0] = child;
        } else if (children[1] == null) {
            children[1] = child;
        } else if(children[2] == null){
            children[2] = child;
        }
        child.parent = this;
    }
    public void replaceChild(Node oldChild, Node newChild) {
        if (children[0] == oldChild) {
            children[0] = newChild;
        } else if (children[1] == oldChild) {
            children[1] = newChild;
        } else if (children[2] == oldChild) {
            children[2] = newChild;
        }else if(children[3] == oldChild) {
            children[3] = newChild;
        }
        newChild.parent = oldChild.parent;
    }
    public void addChild(Node child) {
        if (children[0] == null || (child.keys[0] < children[0].keys[0])) {
            children[3] = children[2];
            children[2] = children[1];
            children[1] = children[0];
            children[0] = child;
        } else if (children[1] == null ||(child.keys[0] < children[1].keys[0])){
            children[3] = children[2];
            children[2] = children[1];
            children[1] = child;
        } else if (children[2] == null ||(child.keys[0] < children[2].keys[0])) {
            children[3] = children[2];
            children[2] = child;
        }else if(children[3] == null){
            children[3] = child;
        }
        child.parent = this;
    }

    public Node getNextNode(int key) {
        if (key < keys[0]) {
            return children[0];
        } else if (numKeys == 1 || key < keys[1]) {
            return children[1];
        } else {
            return children[2];
        }
    }

    public void removeFromNode(int value){
        if (numKeys >= 1 && keys[0] == value){
            keys[0] = keys[1];
            keys[1] = keys[2];
            numKeys--;
        } else if(numKeys == 2 && keys[1] == value){
            keys[1] = keys[2];
            numKeys--;
        }
    }

    public Node searchMin(Node node) {
        if (node == null) return null;
        if (node.children[0] == null) return node;
        else return searchMin(node.children[0]);
    }

    public Node redistribute(Node leaf) {
        Node parent = leaf.parent;
        children = parent.children;

        if (parent.numKeys == 2 && children[0].numKeys < 2 && children[1].numKeys < 2 && children[2].numKeys < 2) {
            if (children[0] == leaf) {
                parent.children[0] = parent.children[1];
                parent.children[1] = parent.children[2];
                parent.children[2] = null;
                parent.children[0].insertToNode(parent.keys[0]);
                parent.children[0].children[2] = parent.children[0].children[1];
                parent.children[0].children[1] = parent.children[0].children[0];

                if (leaf.children[0] != null)
                    parent.children[0].children[0] = leaf.children[0];
                else if (leaf.children[1] != null)
                    parent.children[0].children[0] = leaf.children[1];

                if (parent.children[0].children[0] != null)
                    parent.children[0].children[0].parent = parent.children[0];

                parent.removeFromNode(parent.keys[0]);
                return children[0];
            } else if (children[1] == leaf) {
                children[0].insertToNode(parent.keys[0]);
                parent.removeFromNode(parent.keys[0]);

                if (leaf.children[0] != null)
                    children[0].children[2] = leaf.children[0];
                else if (leaf.children[1] != null)
                    children[0].children[2] = leaf.children[1];

                if (children[0].children[2] != null)
                    children[0].children[2].parent = children[0];

                parent.children[1] = parent.children[2];
                parent.children[2] = null;

                return children[1];
            } else if (children[2] == leaf) {
                children[1].insertToNode(parent.keys[1]);
                parent.children[2] = null;
                parent.removeFromNode(parent.keys[1]);

                if (leaf.children[0] != null)
                    children[1].children[2] = leaf.children[0];
                else if (leaf.children[1] != null)
                    children[1].children[2] = leaf.children[1];

                if (children[1].children[2] != null)
                    children[1].children[2].parent = children[1];

                return children[2];
            }
        }
        else if ((parent.numKeys == 2) && (children[0].numKeys == 2 || children[1].numKeys == 2 || children[2].numKeys == 2)) {
            if (children[2] == leaf) {
                if (leaf.children[0] != null) {
                    leaf.children[1] = leaf.children[0];
                    leaf.children[0] = null;
                }

                leaf.insertToNode(parent.keys[1]);

                if (children[1].numKeys == 2) {
                    parent.keys[1] = children[1].keys[1];
                    children[1].removeFromNode(children[1].keys[1]);
                    leaf.children[0] = children[1].children[2];

                    if (leaf.children[0] != null)
                        leaf.children[0].parent = leaf;
                } else if (children[0].numKeys == 2) {
                    parent.keys[1] = children[1].keys[0];
                    leaf.children[0] = children[1].children[1];
                    children[1].children[1] = children[1].children[0];

                    if (leaf.children[0] != null)
                        leaf.children[0].parent = leaf;

                    children[1].keys[0] = parent.keys[0];
                    parent.keys[0] = children[0].keys[1];
                    children[0].removeFromNode(children[0].keys[1]);
                    children[1].children[0] = children[0].children[2];

                    if (children[1].children[0] != null)
                        children[1].children[0].parent = children[1];

                    children[0].children[2] = null;
                }
            } else if (children[1] == leaf) {
                if (children[2].numKeys == 2) {
                    if (leaf.children[0] == null) {
                        leaf.children[0] = leaf.children[1];
                        leaf.children[1] = null;
                    }

                    children[1].insertToNode(parent.keys[1]);
                    parent.keys[1] = children[2].keys[0];
                    children[2].removeFromNode(children[2].keys[0]);
                    children[1].children[1] = children[2].children[0];

                    if (children[1].children[1] != null)
                        children[1].children[1].parent = children[1];

                    children[2].children[0] = children[2].children[1];
                    children[2].children[1] = children[2].children[2];
                    children[2].children[2] = null;
                } else if (children[0].numKeys == 2) {
                    if (leaf.children[1] == null) {
                        leaf.children[1] = leaf.children[0];
                        leaf.children[0] = null;
                    }

                    children[1].insertToNode(parent.keys[0]);
                    parent.keys[0] = children[0].keys[1];
                    children[0].removeFromNode(children[0].keys[1]);
                    children[1].children[0] = children[0].children[2];

                    if (children[1].children[0] != null)
                        children[1].children[0].parent = children[1];

                    children[0].children[2] = null;
                }
            } else if (children[0] == leaf) {
                if (leaf.children[0] != null) {
                    leaf.children[1] = leaf.children[0];
                    leaf.children[0] = null;
                }

                children[0].insertToNode(parent.keys[0]);

                if (children[1].numKeys == 2) {
                    parent.keys[0] = children[1].keys[0];
                    children[1].removeFromNode(children[1].keys[0]);
                    children[0].children[2] = children[1].children[0];

                    if (children[0].children[2] != null)
                        children[0].children[2].parent = children[0];
                } else if (children[2].numKeys == 2) {
                    parent.keys[0] = children[1].keys[1];
                    leaf.children[0] = children[1].children[2];
                    children[1].children[2] = null;

                    if (leaf.children[0] != null)
                        leaf.children[0].parent = leaf;

                    children[1].keys[1] = children[2].keys[0];
                    children[2].removeFromNode(children[2].keys[0]);
                    children[0].children[2] = children[2].children[0];

                    if (children[0].children[2] != null)
                        children[0].children[2].parent = children[0];

                    children[2].children[0] = children[2].children[1];
                    children[2].children[1] = children[2].children[2];
                    children[2].children[2] = null;
                }
            }

        }
        else if (parent.numKeys == 1) {
            leaf.insertToNode(parent.keys[0]);
            if (children[0] == leaf && children[1].numKeys == 2) {
                parent.keys[0] = children[1].keys[0];
                children[1].removeFromNode(children[1].keys[0]);
                if (leaf.children[0] == null) {
                    leaf.children[0] = leaf.children[1];
                }
                leaf.children[1] = children[1].children[0];
                children[1].children[0]  = children[1].children[1];
                children[1].children[1] = children[1].children[2];
                children[1].children[2] = null;
                if (leaf.children[1] != null) {
                    leaf.children[1].parent = leaf;
                }
            } else if (children[1] == leaf && children[0].numKeys == 2) {
                parent.keys[0] = children[0].keys[1];
                children[0].removeFromNode(children[0].keys[1]);
                if (leaf.children[1] == null) {
                    leaf.children[1]= leaf.children[0];
                }
                leaf.children[0] = children[0].children[2];
                children[0].children[2] = null;
                if (leaf.children[0] != null) {
                    leaf.children[0].parent = leaf;
                }
            }
        }
        return parent;
    }
}

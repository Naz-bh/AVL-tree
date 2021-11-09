/**
 * AVL ADT.
 * Supported operations:
 * Insert
 * Delete
 * Find
 * Min
 * Max
 * Depth
 * Print
 **/
public class AVL {
    private AVLNode root;      /* Pointer to the root of the tree */
    private int noOfNodes;     /* No of nodes in the tree */
    private AVLNode deletedNode;   /* It represents the root node after deletion  */

    /*
     * Constructor: Initializes the AVL
     */
    public AVL() {
        root = null;
        noOfNodes = 0;
    }//end-AVL


    // A utility function to get the height of the tree
    public int height(AVLNode N) {
        if (N == null)
            return 0;

        int leftHeight = height(N.left);
        int rightHeight = height(N.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }//end-height

    /*
     * Returns a pointer to the root of the tree
     */
    public AVLNode Root() {
        return root;
    }//end-Root

    /*
     * Returns the number of nodes in the tree
     */
    public int NoOfNodes() {
        return noOfNodes;
    }//end-NoOfNodes

    /*
     * Checks the balance factor in the tree and rotates the tree for insert method
     */

    public AVLNode checkBalanceAndRotate(int data, AVLNode node) {
        int balance = getBalance(node); // balance = leftNode.height - rightNode.height

        // left-left
        if (balance > 1 && data < node.left.key) {
            return rightRotation(node);
        }

        //left-left for duplicate keys
        if (balance > 1 && data == node.key) {
            return rightRotation(node);
        }

        // right-right
        if (balance < -1 && data > node.right.key) {
            return leftRotation(node);
        }

        //right-right for duplicate keys
        if (balance < -1 && data == node.key) {
            return leftRotation(node);
        }

        // left-right
        if (balance > 1 && data > node.left.key) {
            node.left = leftRotation(node.left);
            return rightRotation(node);
        }

        // right-left
        if (balance < -1 && data < node.right.key) {
            node.right = rightRotation(node.right);
            return leftRotation(node);
        }

        return node;
    }//end-checkBalanceAndRotate


    // A utility function to left rotate subtree rooted with x
    public AVLNode leftRotation(AVLNode node) {
        AVLNode temp = node.right;
        if (node == root) root = temp;
        node.right = temp.left;
        temp.left = node;

        return temp;
    }// end-leftRotation

    // A utility function to right rotate subtree rooted with x
    public AVLNode rightRotation(AVLNode node) {
        AVLNode temp = node.left;
        if (node == root) root = temp;
        node.left = temp.right;
        temp.right = node;

        return temp;
    }// end-rightRotation

    /*
     * Inserts the key into the AVL. Returns a pointer to
     * the inserted node
     */
    public AVLNode Insert(int key) {
        root = insert(root, key);
        return root;
    } //end-Insert

    public AVLNode insert(AVLNode node, int key) {

        if (node == null) {
            noOfNodes++;
            return new AVLNode(key);
        }

        if (key < node.key) node.left = insert(node.left, key);

        else if (key > node.key) node.right = insert(node.right, key);

        else {
            node.count++;
            noOfNodes--;
            node.right = insert(node.right, key);
            updateCountForSameKeys(key, node.count);
        }
        return checkBalanceAndRotate(key, node); // check balance and make rotations if necessary
    }//end-insert


    // Get Balance factor of node N
    public int getBalance(AVLNode N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }//end-getBalance

    /*
     * Deletes the key from the tree (if found). Returns
     * 0 if deletion succeeds, -1 if it fails
     */
    public int Delete(int key) {
        // Fill this in
        if (Find(key) == null) return -1;

        deletedNode = delete(root, key);
        return 0;
    } //end-Delete

    public AVLNode delete(AVLNode node, int data) {
        AVLNode temp;
        if (node == null) return null;

        if (data < node.key) node.left = delete(node.left, data);
        else if (data > node.key) node.right = delete(node.right, data);
        else { /* data==node.key */

            if (node.count > 1) {  // When deleting a key, count will be updated here
                node.count--;
                updateCountForSameKeys(data, node.count);
                noOfNodes++;
            }

            if ((node.left == null) || (node.right == null)) { // Node with only one child or no child
                temp = node.left != null ? node.left : node.right; // Temp changes according to left or right child
                noOfNodes--;
                if (temp == null) { //No child case
                    temp = node;
                    deletedNode = temp;
                    node = null;
                } else {  // One child case
                    deletedNode = node;
                    node = temp;
                }
            } else { // Node with two child
                temp = maxValueNode(node.left);
                node.key = temp.key; // We are copied
                node.count = temp.count;
                delete(node.left, temp.key);
            } /* Max value of left subtree is found, and assigned to temp. Then temp is replaced with deleted data  */
        }


        return checkBalanceAndRotate(data, node);
    }//end-delete

    //Finds the maximum value on the right sub tree
    public AVLNode maxValueNode(AVLNode node) {
        AVLNode current = node;

        while (current.right != null)
            current = current.right;

        return current;
    }

    // When we change count of a node, this method updates the count of nodes that has the same key
    public void updateCountForSameKeys(int key, int count) {
        AVLNode p = root;
        while (p != null) {
            if (key == p.key) {
                p.count = count;
                p = p.right;
            } else if (key < p.key) p = p.left;
            else /*key > p.key */ p = p.right;
        }
        AVLNode pp = root;
        while (pp != null) {
            if (key == pp.key) {
                pp.count = count;
                pp = pp.left;
            } else if (key < pp.key) pp = pp.left;
            else /*key > p.key */ pp = pp.right;
        }
    }//end-countIterator

    /*
     * Searches the AVL for a key. Returns a pointer to the
     * node that contains the key (if found) or NULL if unsuccessful
     */
    public AVLNode Find(int key) {
        // Fill this in
        AVLNode p = root;
        while (p != null) {
            if (key == p.key) return p;
            else if (key < p.key) p = p.left;
            else /*key > p.key */ p = p.right;
        }//end-while
        return null;
    } //end-Find

    /*
     * Returns a pointer to the node that contains the minimum key
     */
    public AVLNode Min() {
        // Fill this in
        if (root == null) return null;

        AVLNode p = root;
        while (p.left != null) {
            p = p.left;
        }//end-while
        return p;
    } //end-Min

    /*
     * Returns a pointer to the node that contains the maximum key
     */
    public AVLNode Max() {
        // Fill this in
        if (root == null) return null;

        AVLNode p = root;
        while (p.right != null) {
            p = p.right;
        }//end-while

        return p;
    } //end-Max

    /*
     * Returns the depth of tree. Depth of a tree is defined as
     * the depth of the deepest leaf node. Root is at depth 0
     */
    public int Depth() {
        // Fill this in
        AVLNode node = root;
        return maxDepth(node) - 1;
    } //end-Depth

    int maxDepth(AVLNode root) {
        // Root being null means tree doesn't exist.
        if (root == null) return 0;

        // Get the depth of the left and right subtree
        // using recursion.
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);

        // Choose the larger one and add the root to it.
        if (leftDepth > rightDepth)
            return (leftDepth + 1);
        else
            return (rightDepth + 1);
    }//end-maxDepth

    /*
     * Performs an inorder traversal of the tree and prints [key, count]
     * pairs in sorted order
     */
    public void Print() {
        // Fill this in
        AVLNode p = root;
        inOrder(p);
        System.out.println("");
    } //end-Print

    void inOrder(AVLNode node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.key + "," + node.count + "  ");
            inOrder(node.right);
        }//end-if

    } //end-inOrder

}//end-AVL
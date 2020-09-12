package BST;

/**
 * @author Aurimas Šakalys 20185388
 */
public class BSTNode<T extends Comparable<T>>  {

    public BSTNode(T value){
        this.value = value;
    }

    public void insert(T value) {
        this.insert(new BSTNode<>(value));
    }

    public void insert(BSTNode<T> node) {
        int comparison = this.value.compareTo(node.value);

        if(comparison == 0) return;
        if(comparison < 0) insertLeft(node);
        if(comparison > 0) insertRight(node);
    }

    public BSTNode<T> find(T value) {
        int comparison = this.value.compareTo(value);

        if(comparison == 0) return this;
        if(this.left != null && comparison < 0) return this.left.find(value);
        if(this.right != null && comparison > 0) return this.right.find(value);

        return null;
    }

    public BSTNode<T> find_min() {
        return this.left != null ? this.left.find_min() : this;
    }

    public BSTNode<T> find_max() {
        return this.right != null ? this.right.find_max() : this;
    }

    private void insertLeft(BSTNode<T> node) {
        if(this.left == null) this.updateLeft(node);
        else this.left.insert(node);
    }

    private void updateLeft(BSTNode<T> node) {
        this.left = node;
        this.left.parent = this;
    }

    private void insertRight(BSTNode<T> node) {
        if(this.right == null) this.updateRight(node);
        else this.right.insert(node);
    }

    private void updateRight(BSTNode<T> node) {
        this.right = node;
        this.right.parent = this;
    }


    public void delete(T value) {
        int comparison = this.value.compareTo(value);

        if(this.left != null && comparison < 0) {
            this.left.delete(value);
        }
        else if(this.right != null && comparison > 0) {
            this.right.delete(value);
        }
        else if (comparison == 0) {

            if (this.right != null && this.left != null) {
                boolean searchRight = this.right.getHeight() > this.left.getHeight();
                BSTNode<T> successor = searchRight ? this.right.find_min() : this.left.find_max();
                this.value = successor.value;
                successor.delete(successor.value);
            } else if (this.left != null) this.replaceInParent(this.left);
            else if (this.right != null) this.replaceInParent(this.right);
            else this.replaceInParent(null);
        }
    }

    private void replaceInParent(BSTNode<T> value) {
        if(this.parent != null) {
            if(this.parent.left == this) this.parent.left = value;
            else this.parent.right = value;
        }
        if(value != null) value.parent = this.parent;
    }



    public BSTNode<T> getLeft() { return this.left; }
    public BSTNode<T> getRight() { return this.right; }
    public BSTNode<T> getParent() { return this.parent; }

    public boolean hasLeft() { return this.left != null; }
    public boolean hasRight() { return this.right != null; }

    public boolean hasChildren() { return hasLeft() || hasRight(); }

    public T getValue() { return this.value; }

    public int getHeight() {
        int leftHeight = this.left != null ? this.left.getHeight() : 0;
        int rightHeight = this.right != null ? this.right.getHeight() : 0;
        int significantHeight = Math.max(leftHeight, rightHeight);
        return significantHeight + 1;
    }


    @Override
    public String toString() {
        return "BST.BSTNode{" +
                "value=" + value +
                ", left=" + (hasLeft() ? getLeft().value : 'X') +
                ", right=" + (hasRight() ? getRight().value : 'X') +
                '}';
    }

    private BSTNode<T> parent;
    private BSTNode<T> left;
    private BSTNode<T> right;
    private T value;
}

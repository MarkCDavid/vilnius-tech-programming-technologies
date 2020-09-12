package BST;

public class BST<T extends Comparable<T>> {

    BSTNode<T> root;

    public void insert(T value) {
        if(this.root == null) this.root = new BSTNode<>(value);
        else this.root.insert(new BSTNode<>(value));
    }

    public BSTNode<T> find(T value) {
        if(this.root == null) return null;
        else return this.root.find(value);
    }

    public void delete(T value){
        if(this.root != null) {
            if (!this.root.hasChildren() && this.root.getValue() == value) root = null;
            else {
                this.root.delete(value);
                if (this.root.hasLeft() && this.root.getLeft().getParent() == null) this.root = this.root.getLeft();
                if (this.root.hasRight() && this.root.getRight().getParent() == null) this.root = this.root.getRight();
            }
        }
    }

    public int height() {
        return this.root == null ? 0 : this.root.getHeight();
    }

    @Override
    public String toString() {
        if(this.root == null) return "<EMPTY>";
        return BSTPainter.toString(this.root);
    }
}

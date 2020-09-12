package BST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BSTPainter {

    private final static String NO_CONNECTION     = "   ";
    private final static String PARENT_CONNECTION = "++ ";
    private final static String CHILD_CONNECTION  = " ++";
    private final static String BRANCH_CONNECTION = " | ";

    public static <T extends Comparable<T>> String toString(BSTNode<T> node) {
        List<String> treeDisplay = prepareTreeDisplay(node.getHeight());
        BSTPainter.buildTreeDisplay(treeDisplay, node);
        return String.join("\n", treeDisplay);
    }

    private static List<String> prepareTreeDisplay(int treeHeight) {
        int displayHeight = ((int) Math.pow(2, treeHeight)) - 1;
        return new ArrayList<>(Collections.nCopies(displayHeight, ""));
    }

    private static <T extends Comparable<T>> void buildTreeDisplay(List<String> treeDisplay, BSTNode<T> node) {

        String nodeDisplay = node.getValue().toString();
        String filler =  " ".repeat(nodeDisplay.length());

        int parentIndex = treeDisplay.size() / 2;
        int branchLength = (parentIndex / 2) + 1;

        int leftChildIndex = node.hasLeft() ? parentIndex - branchLength : -1;
        int rightChildIndex = node.hasRight() ? parentIndex + branchLength : -1;

        int branchStart = node.hasLeft() ? leftChildIndex : parentIndex;
        int branchEnd = node.hasRight() ? rightChildIndex : parentIndex;

        boolean hasChildren = node.hasChildren();

        for(int i = 0; i < treeDisplay.size(); i++){

            String connection = getBranchString(i, Between(i, branchStart, branchEnd), hasChildren, parentIndex, leftChildIndex, rightChildIndex);
            String display = i == parentIndex ? nodeDisplay : filler;
            treeDisplay.set(i, treeDisplay.get(i) + display + connection);
        }

        if(node.hasLeft()) buildTreeDisplay(treeDisplay.subList(0, parentIndex), node.getLeft());
        if(node.hasRight()) buildTreeDisplay(treeDisplay.subList(parentIndex + 1, treeDisplay.size()), node.getRight());
    }

    private static String getBranchString(int currentIndex, boolean between, boolean hasChildren, int parentIndex, int leftChildIndex, int rightChildIndex) {
        String connection = NO_CONNECTION;
        if(hasChildren && between) {
            if(currentIndex == leftChildIndex || currentIndex == rightChildIndex) connection = CHILD_CONNECTION;
            else if(currentIndex == parentIndex) connection = PARENT_CONNECTION;
            else connection = BRANCH_CONNECTION;
        }
        return connection;
    }

    private static boolean Between(int value, int a, int b) {
        return b > a ? value >= a && value <= b : value >= b && value <= a;
    }

}



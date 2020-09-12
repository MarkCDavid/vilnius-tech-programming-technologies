package BST;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        BST<Integer> binarySearchTree = new BST<>();

        while (true) {
            System.out.println(binarySearchTree);
            int action = Prompt.OptionsPrompt(userActions);

            if(action == 4) return;
            int value = Prompt.IntegerPrompt();

            if(action == 1) binarySearchTree.insert(value);

            if(action == 2) {
                BSTNode<Integer> found = binarySearchTree.find(value);
                if(found == null) System.out.println("Value " + value + " not found in the tree!");
                else System.out.println(found);
            }

            if(action == 3) binarySearchTree.delete(value);
        }
    }

    public static Map<Integer, String> userActions;
    static {
        userActions = new HashMap<>();
        userActions.put(1, "Insert");
        userActions.put(2, "Find");
        userActions.put(3, "Delete");
        userActions.put(4, "Exit");
    }

}

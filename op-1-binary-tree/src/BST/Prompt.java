package BST;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Prompt {

    public static String StringPrompt() {
            System.out.print("Enter string: ");
            Scanner scanner = new Scanner(System.in);
            return scanner.next();
    }

    public static int IntegerPrompt() {
        while (true) {
            System.out.print("Enter integer: ");
            Scanner scanner = new Scanner(System.in);
            try {
                return scanner.nextInt();
            }
            catch (InputMismatchException ex) {
                System.out.println("Invalid input!");
            }
        }
    }

    public static int OptionsPrompt(Map<Integer, String> options) {
        while (true) {

            System.out.println("=== Options === ");
            for(Integer key: options.keySet()) {
                System.out.println(key + ") " + options.get(key));
            }
            System.out.print("Select: ");

            Scanner scanner = new Scanner(System.in);
            try {
                int userInput = scanner.nextInt();
                if(options.containsKey(userInput)) return userInput;
                System.out.println("No such option!");
            }
            catch (InputMismatchException ex) {
                System.out.println("Invalid input!");
            }
        }
    }
}

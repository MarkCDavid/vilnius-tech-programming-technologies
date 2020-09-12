package UI;

import javax.swing.*;
import BST.BST;

import java.text.Format;

public class BSTForm {

    public JPanel mainPanel;

    BST<Integer> tree;

    public BSTForm() {

        insertField.setDocument(new JTextFieldLimit(4));
        deleteField.setDocument(new JTextFieldLimit(4));
        findField.setDocument(new JTextFieldLimit(4));

        insertButton.addActionListener(actionEvent -> {
            treePainter.found = null;
            Integer value = getFieldValue(insertField);
            if(value == null) return;
            tree.insert(value);
            canvasPanel.repaint();
        });

        deleteButton.addActionListener(actionEvent -> {
            treePainter.found = null;
            Integer value = getFieldValue(deleteField);
            if(value == null) return;
            tree.delete(value);
            canvasPanel.repaint();
        });

        findButton.addActionListener(actionEvent -> {
            treePainter.found = null;
            Integer value = getFieldValue(findField);
            if(value == null) return;
            treePainter.found = tree.find(value);
            canvasPanel.repaint();
        });

        tree = new BST<Integer>();
        treePainter.tree = tree;

    }

    private static Integer getFieldValue(JTextField field) {
        try {
            return Integer.valueOf(field.getText());
        }
        catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, "Invalid entry");
            return null;
        }
        finally {
            field.setText("");
        }
    }

    private JButton insertButton;
    private JButton deleteButton;
    private JButton findButton;

    private JFormattedTextField insertField;
    private JFormattedTextField deleteField;
    private JFormattedTextField findField;

    private TreePainter treePainter;
    private JPanel canvasPanel;
}

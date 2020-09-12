package UI;

import BST.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TreePainter extends JComponent {

    private final Color NODE_COLOR = Color.decode("#41B3A3");
    private final Color NODE_BORDER_COLOR = Color.decode("#85DCB");
    private final Color NODE_EDGE_COLOR = Color.decode("#E8A87C");
    private final Color NODE_FOUND_COLOR = Color.decode("#E27D60");
    private final Color BACKGROUND_COLOR = Color.decode("#C38D9E");


    public TreePainter() {
        setBackground(BACKGROUND_COLOR);
    }

    BST<Integer> tree;
    BSTNode<Integer> found;
    int radius = 50;

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        BSTNode<Integer> root = tree.getRoot();
        if(root != null) {
            paintTree(graphics2D, graphics.getClip().getBounds().width / 2, radius, radius,  (radius * 2 * root.getHeight()), tree.getRoot());
        }
    }

    private void paintTree(Graphics2D graphics, int x, int y, int radius, int horizontalDistance, BSTNode<Integer> node) {

        drawTreeNode(graphics, x, y, radius, node);
        if(node.hasLeft()){
            int nextX = x - horizontalDistance;
            int nextY = y + radius * 2;
            drawTreeEdge(graphics, x, y, nextX, nextY, radius);
            paintTree(graphics, nextX, nextY, radius, horizontalDistance/2, node.getLeft());
        }

        if(node.hasRight()){
            int nextX = x + horizontalDistance;
            int nextY = y + radius * 2;
            drawTreeEdge(graphics, x, y, nextX, nextY, radius);
            paintTree(graphics, nextX, nextY, radius, horizontalDistance/2, node.getRight());
        }
    }

    private void drawTreeNode(Graphics2D graphics, int x, int y, int radius, BSTNode<Integer> node)
    {
        drawTreeNodeCircle(graphics, x, y, radius, NODE_COLOR);

        if(found != null && found.getValue().equals(node.getValue())) drawTreeNodeCircleBorder(graphics, x, y, radius, NODE_FOUND_COLOR, 5);
        else drawTreeNodeCircleBorder(graphics, x, y, radius, NODE_BORDER_COLOR, 3);

        drawTreeNodeText(graphics, x, y, radius, node.getValue());
    }

    private void drawTreeNodeCircle(Graphics2D graphics, int x, int y, int radius, Color color){
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(1));
        drawCenteredCircle(graphics, x, y, radius, true);
    }

    private void drawTreeNodeCircleBorder(Graphics2D graphics, int x, int y, int radius, Color color, int width){
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(width));
        drawCenteredCircle(graphics, x, y, radius, false);
    }

    private void drawTreeNodeText(Graphics2D graphics, int x, int y, int radius, Integer value){
        Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(value.toString(), graphics);
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(1));
        graphics.drawString(value.toString(), x - (int)(bounds.getWidth())/2, y + (int)(bounds.getHeight())/2);
    }

    private void drawTreeEdge(Graphics2D graphics, int fromX, int fromY, int toX, int toY, int radius) {
        graphics.setColor(NODE_EDGE_COLOR);
        Direction direction = new Direction(new Point(fromX, fromY), new Point(toX, toY));
        direction.scale(radius * 0.75);
        fromX += (int)direction.x;
        fromY += (int)direction.y;
        toX -= (int)direction.x;
        toY -= (int)direction.y;
        graphics.setStroke(new BasicStroke(3));
        graphics.drawLine(fromX, fromY, toX, toY);
    }

    private void drawCenteredCircle(Graphics2D graphics, int x, int y, int radius, boolean fill) {
        x = x - (radius / 2);
        y = y - (radius / 2);
        if(fill) graphics.fillOval(x, y, radius, radius);
        else graphics.drawOval(x, y, radius, radius);
    }

    private static class Direction {
        double x;
        double y;

        public Direction(Point from, Point to) {
            Point direction = new Point(to.x - from.x, to.y - from.y);
            double distance = Point.distance(to.x, to.y, from.x, from.y);
            x = direction.x / distance;
            y= direction.y / distance;
        }

        public void scale(double scale){
            x *= scale;
            y *= scale;
        }


    }

}

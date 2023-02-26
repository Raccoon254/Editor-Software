package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class PaintCanvas extends JComponent implements MouseListener, MouseMotionListener {
    private int x1, y1, x2, y2;
    private boolean isDragging;
    static ArrayList<ShapeInfo> shapes = new ArrayList<ShapeInfo>();
    static ArrayList<ShapeInfo> undoList = new ArrayList<ShapeInfo>();
    static ArrayList<ShapeInfo> copyList = new ArrayList<ShapeInfo>();

    private int shapeType; // 1 for rectangle, 2 for triangle, 3 for ellipse
    private int fillType; // 1 for fill, 2 for outline only, 3 for both fill and outline
    private Color fillColor, outlineColor;
    private int outlineThickness;
    private int currentShapeIndex = -1;
    public static int test=5;
    boolean isMoving;

    private static class ShapeInfo {
        public Shape shape;
        public int fillType;
        public Color fillColor;
        public Color outlineColor;
        public int outlineThickness;

        public ShapeInfo(Shape shape, int fillType, Color fillColor, Color outlineColor, int outlineThickness) {
            this.shape = shape;
            this.fillType = fillType;
            this.fillColor = fillColor;
            this.outlineColor = outlineColor;
            this.outlineThickness = outlineThickness;
        }
    }
    public PaintCanvas() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(500, 500));
        addMouseListener(this);
        addMouseMotionListener(this);
        isDragging = false;
        isMoving = false;
        outlineThickness = 5;
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Clear the canvas by filling it with the background color
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Loop over all the shapes in the list
        for (ShapeInfo shapeInfo : shapes) {
            Shape shape = shapeInfo.shape;
            int fillType = shapeInfo.fillType;
            Color fillColor = shapeInfo.fillColor;
            Color outlineColor = shapeInfo.outlineColor;
            int outlineThickness = shapeInfo.outlineThickness;

            // Set the fill and stroke based on the fill type and parameters
            if (fillType == 1) {
                g2.setColor(fillColor);
                g2.fill(shape);
            } else if (fillType == 2) {
                g2.setStroke(new BasicStroke(outlineThickness));
                g2.setColor(outlineColor);
                g2.draw(shape);
            } else if (fillType == 3) {
                g2.setStroke(new BasicStroke(outlineThickness));
                g2.setColor(outlineColor);
                g2.draw(shape);
                g2.setColor(fillColor);
                g2.fill(shape);
            }
        }

        // Draw the current shape being dragged, if any
        if (isDragging) {
            Shape shape;
            shapeType= currentShapeSelected();
            fillType = fillTypeSelected();
            fillColor = fillColorSelected();
            outlineColor = outlineColorSelected();
            if (shapeType == 1) {
                shape = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            } else if (shapeType == 2) {
                shape = new Polygon(new int[] { x1, x2, (x1 + x2) / 2 }, new int[] { y1, y2, y2 }, 3);
            } else {
                shape = new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            }

            if (fillType == 1) {
                g2.setColor(fillColor);
                g2.fill(shape);
            } else if (fillType == 2) {
                g2.setStroke(new BasicStroke(outlineThickness));
                g2.setColor(outlineColor);
                g2.draw(shape);
            } else if (fillType == 3) {
                g2.setStroke(new BasicStroke(outlineThickness));
                g2.setColor(outlineColor);
                g2.draw(shape);
                g2.setColor(fillColor);
                g2.fill(shape);
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        isDragging = mouseModeSelected();
        System.out.println("the selected mouse mode is "+mouseModeSelected());
    }

    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        isDragging = mouseModeSelected();
        // Create a new Shape object based on the current parameters
        if(isDragging){
            Shape shape;
            if (shapeType == 1) {
                shape = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            } else if (shapeType == 2) {
                shape = new Polygon(new int[] { x1, x2, (x1 + x2) / 2}, new int[] { y1, y2, y2 }, 3);
            } else {
                shape = new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
            }

            // Add the shape to the list of shapes
            shapes.add(new ShapeInfo(shape, fillType, fillColor, outlineColor, outlineThickness));


            repaint();
        }
    }


    public void mouseDragged(MouseEvent e) {
        if (isDragging) {
            x2 = e.getX();
            y2 = e.getY();
            repaint();
        }
    }

    // Other mouse events are not used but must be implemented
    public void mouseClicked(MouseEvent e) {
        if(!isDragging){
            int x = e.getX();
            int y = e.getY();

            // Check if the mouse is hovering over any shape
            for (int i = shapes.size() - 1; i >= 0; i--) {
                if (shapes.get(i).shape.contains(x, y)) {
                    // If a shape is found, select it
                    System.out.println("the selected shape is index "+i);
                    currentShapeIndex = i;
                    break;
                }
            }

            // If no shape is hovered, deselect any previously selected shape
            if (currentShapeIndex == -1) {
                repaint();
            }
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isDragging) {
            int x = e.getX();
            int y = e.getY();

            // Check if the mouse is hovering over any shape
            for (int i = shapes.size() - 1; i >= 0; i--) {
                if (shapes.get(i).shape.contains(x, y)) {
                    // If a shape is found, select it
                    currentShapeIndex = i;
                    break;
                }
            }

            // If no shape is hovered, deselect any previously selected shape

            if (currentShapeIndex == -1) {
                repaint();
                return;
            }

            // Move the selected shape if shifting is true
            if (isMoving) {
                ShapeInfo selectedShape = shapes.get(currentShapeIndex);
                int xc = x - selectedShape.shape.getBounds().width / 2;
                int yc = y - selectedShape.shape.getBounds().height / 2;
                repaint();
            }
        }
    }




    private int currentShapeSelected() {
        int number = 1;
        int listSize = DialogSelectionList.getDialogSelections().size();
        if (listSize > 0) {
            String selectedValue = DialogSelectionList.getDialogSelections().get(listSize - 1)[1];
            switch (selectedValue){
                case "RECTANGLE":
                    number = 1;
                    break;
                case "TRIANGLE":
                    number = 2;
                    break;
                case "ELLIPSE":
                    number = 3;
                    break;

            }
        }
        return number;
    }
    private int fillTypeSelected() {
        int number = 3;
        int listSize = ShadingType.getDialogSelections().size();
        if (listSize > 0) {
            String selectedValue = ShadingType.getDialogSelections().get(listSize - 1)[1];
            switch (selectedValue){
                case "FILLED_IN":
                    number = 1;
                    break;
                case "OUTLINE":
                    number = 2;
                    break;
                case "OUTLINE_AND_FILLED_IN":
                    number = 3;
                    break;
            }
        }
        return number;
    }
    private boolean mouseModeSelected() {
        boolean number = true;
        int listSize = StartAndEndPointMode.getDialogSelections().size();
        if (listSize > 0) {
            String selectedValue = StartAndEndPointMode.getDialogSelections().get(listSize - 1)[1];
            switch (selectedValue){
                case "DRAW":
                    number = true;
                    break;
                case "SELECT":
                case "MOVE":
                    number = false;
                    break;
            }
        }
        return number;
    }
    private boolean mouseMove() {
        boolean number = true;
        int listSize = StartAndEndPointMode.getDialogSelections().size();
        if (listSize > 0) {
            String selectedValue = StartAndEndPointMode.getDialogSelections().get(listSize - 1)[1];
            switch (selectedValue){
                case "DRAW":
                case "SELECT": {
                    number = false;}
                break;
                case "MOVE":
                    number = true;
                    break;
            }
        }
        return number;
    }

    private Color fillColorSelected() {
        Color ColorToBeReturned= Color.GREEN;
        int listSize = PrimaryColor.getDialogSelections().size();
        if (listSize > 0) {
            String selectedValue = PrimaryColor.getDialogSelections().get(listSize - 1)[1];
            switch (selectedValue){
                case "BLACK":
                    ColorToBeReturned = Color.BLACK;
                    break;
                case "BLUE":
                    ColorToBeReturned = Color.BLUE;
                    break;
                case "CYAN":
                    ColorToBeReturned = Color.CYAN;
                    break;
                case "DARK_GRAY":
                    ColorToBeReturned = Color.DARK_GRAY;
                    break;
                case "GRAY":
                    ColorToBeReturned = Color.GRAY;
                    break;
                case "GREEN":
                    ColorToBeReturned = Color.GREEN;
                    break;
                case "LIGHT_GRAY":
                    ColorToBeReturned = Color.LIGHT_GRAY;
                    break;
                case "MAGENTA":
                    ColorToBeReturned = Color.MAGENTA;
                    break;
                case "ORANGE":
                    ColorToBeReturned = Color.ORANGE;
                    break;
                case "PINK":
                    ColorToBeReturned = Color. PINK;
                    break;
                case "RED":
                    ColorToBeReturned = Color.RED;
                    break;
                case "YELLOW":
                    ColorToBeReturned = Color.YELLOW;
                    break;
                case "WHITE":
                    ColorToBeReturned = Color.WHITE;
                    break;
            }
        }
        return ColorToBeReturned;
    }

    private Color outlineColorSelected() {
        Color ColorToBeReturned= Color.MAGENTA;
        int listSize = SecondaryColor.getDialogSelections().size();
        if (listSize > 0) {
            String selectedValue = SecondaryColor.getDialogSelections().get(listSize - 1)[1];
            switch (selectedValue){
                case "BLACK":
                    ColorToBeReturned = Color.BLACK;
                    break;
                case "BLUE":
                    ColorToBeReturned = Color.BLUE;
                    break;
                case "CYAN":
                    ColorToBeReturned = Color.CYAN;
                    break;
                case "DARK_GRAY":
                    ColorToBeReturned = Color.DARK_GRAY;
                    break;
                case "GRAY":
                    ColorToBeReturned = Color.GRAY;
                    break;
                case "GREEN":
                    ColorToBeReturned = Color.GREEN;
                    break;
                case "LIGHT_GRAY":
                    ColorToBeReturned = Color.LIGHT_GRAY;
                    break;
                case "MAGENTA":
                    ColorToBeReturned = Color.MAGENTA;
                    break;
                case "ORANGE":
                    ColorToBeReturned = Color.ORANGE;
                    break;
                case "PINK":
                    ColorToBeReturned = Color. PINK;
                    break;
                case "RED":
                    ColorToBeReturned = Color.RED;
                    break;
                case "YELLOW":
                    ColorToBeReturned = Color.YELLOW;
                    break;
                case "WHITE":
                    ColorToBeReturned = Color.WHITE;
                    break;
            }
        }
        return ColorToBeReturned;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
    }

    public void setFillType(int fillType) {
        this.fillType = fillType;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    public void setOutlineThickness(int outlineThickness) {
        this.outlineThickness = outlineThickness;
    }

    public void undo() {
        if (!shapes.isEmpty()) {
            // Remove the last shape from the shapes list and add it to the undo list
            ShapeInfo removedShape = shapes.remove(shapes.size() - 1);
            undoList.add(removedShape);
            System.out.println("Shape removed");
            // Repaint the canvas to remove the shape from the screen
            repaint();
        }
    }

    public void redo() {
        if (!undoList.isEmpty()) {
            // Remove the last shape from the undo list and add it back to the shapes list
            ShapeInfo addedShape = undoList.remove(undoList.size() - 1);
            shapes.add(addedShape);
            // Repaint the canvas to add the shape back to the screen
            repaint();
        }
    }

    public void copy(){
        if (!shapes.isEmpty()) {
            // Remove the last shape from the shapes list and add it to the undo list
            ShapeInfo removedShape = shapes.get(shapes.size() - 1);
            copyList.add(1,removedShape);
            System.out.println("Shape copied");
            // Repaint the canvas to remove the shape from the screen
            repaint();
        }
    }
    public void paste(){
        if (!copyList.isEmpty()) {
            ShapeInfo addedShape = copyList.get(undoList.size() - 1);
            shapes.add(addedShape);
            // Repaint the canvas to add the shape back to the screen
            repaint();
        }
    }
    public void delete(){
        if (!shapes.isEmpty()) {
            ShapeInfo removedShape = shapes.remove(shapes.size() - 1);
            repaint();
        }
    }


}

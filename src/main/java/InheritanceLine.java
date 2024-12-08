import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InheritanceLine extends JComponent implements Line {
    private Point startPoint;
    private Point endPoint;
    private boolean resizing = false;
    private boolean drawing = false;  // Flag to check if we're in drawing mode
    private String description;
    private DescriptionLabel descriptionLabel;
    private DiagramEditorPanel diagramEditorPanel;
    public boolean resizingStart, resizingEnd;
    public boolean moving;
    private Point initialClick;

    public InheritanceLine(Point startPoint, DiagramEditorPanel diagramEditorPanel) {
        this.startPoint = startPoint;
        this.endPoint = startPoint;
        this.moving = false;
        this.diagramEditorPanel = diagramEditorPanel;

        // Use final variables for MouseListener, MouseMotionListener
        final Point[] start = {startPoint};
        final Point[] end = {startPoint};
    }
    private void removeLine() {
        diagramEditorPanel.remove(this);
        diagramEditorPanel.revalidate();
        diagramEditorPanel.repaint();
    }

    // Example method to show a description dialog
    private void setDescriptionDialog() {
        String newDescription = JOptionPane.showInputDialog(this, "Enter Description:");
        if (newDescription != null) {
            setDescription(newDescription);
        }
    }
    public void setDescription(String description) {
        this.description = description;
        if (descriptionLabel == null) {
            // Create a new DescriptionLabel if it doesn't exist
            descriptionLabel = new DescriptionLabel(description, new Point((startPoint.x + endPoint.x) / 2, (startPoint.y + endPoint.y) / 2));
            diagramEditorPanel.add(descriptionLabel);  // Add it to the panel
            diagramEditorPanel.revalidate();
            diagramEditorPanel.repaint();
        } else {
            // Update the existing label's text
            descriptionLabel.setText(description);
        }
    }

    // Method to draw the line while dragging
    // Method to draw the line while dragging
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);

        // Draw the line from startPoint to the current endPoint
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Only draw the arrowhead at the end if not in drawing mode
        if (!drawing) {
            drawHollowArrow(g2d, endPoint); // Draw arrowhead at endPoint only
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Delegate the drawing task to the draw method
        draw(g);
    }

    // Draws the hollow arrowhead at the end of the line
    private void drawHollowArrow(Graphics2D g2d, Point end) {
        // Calculate the direction vector of the line
        double angle = Math.atan2(end.y - startPoint.y, end.x - startPoint.x);
        int arrowSize = 15;

        // Points of the hollow triangle
        int x1 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));

        // Draw the triangle outline
        int[] xPoints = {end.x, x1, x2};
        int[] yPoints = {end.y, y1, y2};
        g2d.drawPolygon(xPoints, yPoints, 3);
    }


    // Additional helper methods (isOnLine, isOnStart, isOnEnd, etc.) can be kept as is

    public boolean isOnLine(Point point) {
        double lineLength = startPoint.distance(endPoint);
        double d1 = startPoint.distance(point) + point.distance(endPoint);
        return Math.abs(d1 - lineLength) < 5;
    }
    public void startResizing(Point p) {
        if (isOnStart(p)) {
            resizingStart = true;
        } else if (isOnEnd(p)) {
            resizingEnd = true;
        }
    }
    public void startMoving(Point p) {
        if (!isOnStart(p) && !isOnEnd(p)) {  // Ensure it's not on the start/end point
            moving = true;
            initialClick = p;
        }
    }

    public boolean isOnStart(Point point) {
        return startPoint.distance(point) < 5;
    }

    public boolean isOnEnd(Point point) {
        return endPoint.distance(point) < 5;
    }
    // In InheritanceLine class, change resize to updateEndPoint
    public void updateEndPoint(Point point) {
        this.endPoint = point;// Update the end point during the dragging
       this.drawing=false;
       repaint();  // Trigger a repaint to update the drawn line
    }

    public void stopResizing() {
        if (endPoint == null) {
            System.out.println("End point is null. Exiting stopResizing safely.");
            return; // Exit safely if the end point hasn't been set
        }

        // Ensure the end point is within bounds or snapped to a valid location (if required)
        System.out.println("Resizing stopped at: " + endPoint);
        // Perform any final adjustments to the line or notify other components
    }


    public void moveLine(Point point) {
        updateEndPoint(point);
    }
}
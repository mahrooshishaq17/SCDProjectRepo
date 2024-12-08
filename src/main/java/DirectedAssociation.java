import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class DirectedAssociation extends JComponent implements Line {
    private Point startPoint;
    private Point endPoint;
    private DiagramEditorPanel diagramEditorPanel;
    private String description;
    private DescriptionLabel descriptionLabel;
    public boolean resizingStart, resizingEnd;
    public boolean moving;
    private Point initialClick;

    public DirectedAssociation(Point startPoint, DiagramEditorPanel diagramEditorPanel) {
        this.startPoint = startPoint;
        this.endPoint = startPoint;
        this.diagramEditorPanel = diagramEditorPanel;
        this.moving=false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLACK);

        // Draw the line
        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // Draw the filled diamond
        drawArrow(g2d, endPoint);
    }

    private void drawArrow(Graphics2D g2d, Point end) {
        // Calculate the angle of the line
        double angle = Math.atan2(end.y - startPoint.y, end.x - startPoint.x);
        int size = 15; // Adjust this size for a larger or smaller diamond

        // Calculate the vertices of the diamond
        int x1 = (int) (end.x - size * Math.cos(angle - Math.PI / 4)); // Left vertex
        int y1 = (int) (end.y - size * Math.sin(angle - Math.PI / 4));

        int x2 = (int) (end.x - size * Math.cos(angle + Math.PI / 4)); // Right vertex
        int y2 = (int) (end.y - size * Math.sin(angle + Math.PI / 4));

        int x3 = (int) (end.x - size * Math.cos(angle + Math.PI)); // Bottom vertex
        int y3 = (int) (end.y - size * Math.sin(angle + Math.PI));

        int x4 = (int) (end.x - size * Math.cos(angle)); // Top vertex (tip of the diamond)
        int y4 = (int) (end.y - size * Math.sin(angle));

        // Create the points array for the diamond
        int[] xPoints = {x1, end.x, x2, end.x}; // Left, right, bottom, top
        int[] yPoints = {y1, end.y, y2, end.y}; // Left, right, bottom, top

        // Draw the hollow diamond (outline)
        g2d.drawPolygon(xPoints, yPoints, 4);

        // To fill the diamond, you can use g2d.fillPolygon instead of g2d.drawPolygon
        // g2d.fillPolygon(xPoints, yPoints, 4);
    }


    public void updateEndPoint(Point point) {
        this.endPoint = point;
        repaint();
    }

    public void stopResizing() {
        // Finalize resizing logic if necessary
    }
    @Override
    public boolean isOnLine(Point point) {
        double lineLength = startPoint.distance(endPoint); // Calculate the length of the line
        double d1 = startPoint.distance(point) + point.distance(endPoint); // Distance via the given point
        return Math.abs(d1 - lineLength) < 5; // Return true if the point is approximately on the line
    }
    @Override
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


}

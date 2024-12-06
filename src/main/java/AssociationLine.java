import java.awt.*;
public class AssociationLine implements Line {
    private Point startPoint, endPoint;
    public boolean resizingStart, resizingEnd;
    public boolean moving;
    private Point initialClick;
    private String description;
    private DescriptionLabel descriptionLabel;
    private DiagramEditorPanel diagramEditorPanel;  // Store reference to the panel


    public AssociationLine(Point start, Point end, DiagramEditorPanel diagramEditorPanel) {
        this.startPoint = start;
        this.endPoint = end;
        this.moving = false;
        this.diagramEditorPanel=diagramEditorPanel;
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

    public String getDescription() {
        return description;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    public boolean isOnStart(Point p) {
        return distance(startPoint, p) < 10;
    }

    public boolean isOnEnd(Point p) {
        return distance(endPoint, p) < 10;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public void setStartPoint(Point start) {
        this.startPoint = start;
    }

    public void setEndPoint(Point end) {
        this.endPoint = end;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void resize(Point p) {
        if (resizingStart) {
            setStartPoint(p);
        } else if (resizingEnd) {
            setEndPoint(p);
        }
    }

    public void startResizing(Point p) {
        if (isOnStart(p)) {
            resizingStart = true;
        } else if (isOnEnd(p)) {
            resizingEnd = true;
        }
    }

    public void stopResizing() {
        resizingStart = false;
        resizingEnd = false;
    }

    // Methods for moving
    public void startMoving(Point p) {
        if (!isOnStart(p) && !isOnEnd(p)) {  // Ensure it's not on the start/end point
            moving = true;
            initialClick = p;
        }
    }

    public void move(Point p) {
        if (moving) {
            int deltaX = p.x - initialClick.x;
            int deltaY = p.y - initialClick.y;
            startPoint.x += deltaX;
            startPoint.y += deltaY;
            endPoint.x += deltaX;
            endPoint.y += deltaY;
            initialClick = p;
        }
    }

    public void stopMoving() {
        moving = false;
    }
    public boolean isOnLine(Point p) {
        // Check if the point is on the line within a threshold
        double distanceToLine = distanceToLine(startPoint, endPoint, p);
        return distanceToLine < 10; // Threshold for proximity to line
    }

    private double distanceToLine(Point p1, Point p2, Point p) {
        // Calculate the perpendicular distance from point p to the line p1-p2
        double numerator = Math.abs((p2.y - p1.y) * p.x - (p2.x - p1.x) * p.y + p2.x * p1.y - p2.y * p1.x);
        double denominator = Math.sqrt(Math.pow(p2.y - p1.y, 2) + Math.pow(p2.x - p1.x, 2));
        return numerator / denominator;
    }


}

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class CompositionLine extends JComponent implements Line {
    private Point startPoint;
    private Point endPoint;
    private DiagramEditorPanel diagramEditorPanel;
    private String description;
    private DescriptionLabel descriptionLabel;

    public CompositionLine(Point startPoint, DiagramEditorPanel diagramEditorPanel) {
        this.startPoint = startPoint;
        this.endPoint = startPoint;
        this.diagramEditorPanel = diagramEditorPanel;
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
        drawFilledDiamond(g2d, endPoint);
    }

    private void drawFilledDiamond(Graphics2D g2d, Point end) {
        double angle = Math.atan2(end.y - startPoint.y, end.x - startPoint.x);
        int size = 10;

        // Diamond vertices
        int x1 = (int) (end.x - size * Math.cos(angle - Math.PI / 4));
        int y1 = (int) (end.y - size * Math.sin(angle - Math.PI / 4));
        int x2 = (int) (end.x - size * Math.cos(angle + Math.PI / 4));
        int y2 = (int) (end.y - size * Math.sin(angle + Math.PI / 4));
        int x3 = (int) (end.x - size * Math.cos(angle + 3 * Math.PI / 4));
        int y3 = (int) (end.y - size * Math.sin(angle + 3 * Math.PI / 4));

        // Draw filled diamond
        int[] xPoints = {end.x, x1, x3, x2};
        int[] yPoints = {end.y, y1, y3, y2};
        g2d.fillPolygon(xPoints, yPoints, 4);
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


}

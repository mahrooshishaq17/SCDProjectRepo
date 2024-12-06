import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;

public class ComponentDragListener extends MouseAdapter {
    private Point initialClick;
    private JComponent targetComponent;

    public ComponentDragListener() {
        // Default constructor
    }

    @Override
    public void mousePressed(MouseEvent e) {
        targetComponent = (JComponent) e.getSource();
        initialClick = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (targetComponent != null) {
            // Get the parent container
            JComponent parent = (JComponent) targetComponent.getParent();

            // Current mouse position
            int deltaX = e.getX() - initialClick.x;
            int deltaY = e.getY() - initialClick.y;

            // Move the component
            Point newLocation = targetComponent.getLocation();
            newLocation.translate(deltaX, deltaY);

            // Ensure the component stays within the parent bounds
            if (parent != null) {
                newLocation.x = Math.max(0, Math.min(parent.getWidth() - targetComponent.getWidth(), newLocation.x));
                newLocation.y = Math.max(0, Math.min(parent.getHeight() - targetComponent.getHeight(), newLocation.y));
            }

            targetComponent.setLocation(newLocation);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Optionally, you could snap the component to a grid or log the final position
        targetComponent = null;
    }
}

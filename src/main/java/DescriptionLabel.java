import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DescriptionLabel extends JLabel {
    private Point position;
    private boolean dragging;
    private boolean resizing;
    private Point dragStart;

    public DescriptionLabel(String text, Point position) {
        super(text);
        this.position = position;
        setLocation(position);
        setSize(getPreferredSize());
        setOpaque(true);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isOnEdge(e.getPoint())) {
                    resizing = true; // Start resizing if near the edge
                } else {
                    dragging = true; // Start dragging otherwise
                    dragStart = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
                resizing = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    moveLabel(e);
                } else if (resizing) {
                    resizeLabel(e);
                }
            }
        });
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setSize(getPreferredSize()); // Resize to fit new text
    }

    private boolean isOnEdge(Point point) {
        // Check if the mouse is near the bottom-right edge for resizing
        int margin = 10;
        int x = point.x;
        int y = point.y;
        return (x >= getWidth() - margin && x <= getWidth()) &&
                (y >= getHeight() - margin && y <= getHeight());
    }

    private void moveLabel(MouseEvent e) {
        int deltaX = e.getX() - dragStart.x;
        int deltaY = e.getY() - dragStart.y;
        position.x += deltaX;
        position.y += deltaY;
        setLocation(position);
        dragStart = e.getPoint();
    }

    private void resizeLabel(MouseEvent e) {
        int newWidth = Math.max(50, e.getX());
        int newHeight = Math.max(20, e.getY());
        setSize(newWidth, newHeight);
        revalidate();
        repaint();
    }
}

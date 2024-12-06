import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
public class InterfaceComponent extends JComponent {
    private int width, height;
    private Point dragStartPoint = null;
    private boolean resizing = false;

    private String interfaceName = "New Interface";
    private final List<String> attributes = new ArrayList<>();
    private final List<String> operations = new ArrayList<>();

    public InterfaceComponent(int x, int y, int diameter) {
        this.setBounds(x, y, diameter, diameter + 100); // Space for circle + attributes and operations
        this.width = diameter;
        this.height = diameter + 100;

       // this.setBackground(Color.LIGHT_GRAY);
        this.setOpaque(false);
        initializeContextMenu();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isOnResizeCorner(e.getPoint())) {
                    resizing = true;
                } else {
                    dragStartPoint = e.getPoint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
                dragStartPoint = null;

                handleTextInput(e); // Handle input on mouse release
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {
                    resizeInterface(e);
                } else if (dragStartPoint != null) {
                    moveInterface(e);
                }
            }
        });
    }
    private void initializeContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem addAttributeItem = new JMenuItem("Add Attribute");
        addAttributeItem.addActionListener(e -> {
            String input = getInputWithAccessSpecifier("Enter Attribute:");
            if (input != null && !input.isBlank()) {
                attributes.add(input);
                //updateInterfaceSize();
                repaint();
            }
        });

        JMenuItem addOperationItem = new JMenuItem("Add Operation");
        addOperationItem.addActionListener(e -> {
            String input = getInputWithAccessSpecifier("Enter Operation:");
            if (input != null && !input.isBlank()) {
                operations.add(input);
                //updateInterfaceSize();
                repaint();
            }
        });

        contextMenu.add(addAttributeItem);
        contextMenu.add(addOperationItem);

        // Attach context menu to this component
        this.setComponentPopupMenu(contextMenu);
    }
    private void updateInterfaceSize() {
        int totalHeight = width + 20 + (attributes.size() + operations.size()) * 20;
        this.height = Math.max(totalHeight, width); // Ensure height accommodates content
        setSize(width, this.height);
        revalidate();
        repaint();
    }



    private boolean isOnResizeCorner(Point p) {
        return p.x >= width - 10 && p.y >= height - 10;
    }

    private void resizeInterface(MouseEvent e) {
        int newDiameter = Math.max(e.getX(), e.getY());
        if (newDiameter > 60) {
            width = newDiameter;
            height = newDiameter + 100; // Adjust for attributes and operations

            setSize(width, height);
            repaint();
        }
    }

    private void moveInterface(MouseEvent e) {
        Point newLocation = getLocation();
        newLocation.translate(e.getX() - dragStartPoint.x, e.getY() - dragStartPoint.y);
        setLocation(newLocation);
    }

    private void handleTextInput(MouseEvent e) {
        if (e.getClickCount() == 2) { // Double-click to edit
            if (e.getY() < width) {
                // Inside the circle
                String input = JOptionPane.showInputDialog(this, "Enter Interface Name:", interfaceName);
                if (input != null && !input.isBlank()) {
                    interfaceName = input;
                    revalidate();
                    repaint();
                }
            } else if (e.getY() < width + 50) {
                // Attributes section
                String input = getInputWithAccessSpecifier("Enter Attribute:");
                if (input != null && !input.isBlank()) {
                    attributes.add(input);
                    repaint();
                }
            } else {
                // Operations section
                String input = getInputWithAccessSpecifier("Enter Operation:");
                if (input != null && !input.isBlank()) {
                    operations.add(input);
                    repaint();
                }
            }
        }
    }

    private String getInputWithAccessSpecifier(String prompt) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JComboBox<String> accessSpecifierBox = new JComboBox<>(new String[]{"+ Public", "- Private", "# Protected"});
        JTextField inputField = new JTextField();

        panel.add(new JLabel(prompt));
        panel.add(accessSpecifierBox);
        panel.add(inputField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Input", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String accessSpecifier = (String) accessSpecifierBox.getSelectedItem();
            String accessSymbol = accessSpecifier.split(" ")[0];
            String input = inputField.getText().trim();

            if (!input.isEmpty()) {
                return accessSymbol + " " + input;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int circleDiameter = width;

        // Clear the background
        //g2d.setColor(getBackground());
       // g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw the circle
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(0, 0, circleDiameter, circleDiameter);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(0, 0, circleDiameter, circleDiameter);

        // Draw the interface name inside the circle
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        drawCenteredText(g2d, interfaceName, 0, circleDiameter);

        // Draw attributes below the circle
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        int attributesEndY = drawSection(g2d, attributes, circleDiameter, circleDiameter + 50);

        // Draw dividing line between attributes and operations
        g2d.setColor(Color.BLACK);
        g2d.drawLine(10, attributesEndY + 5, width - 10, attributesEndY + 5); // Add padding for the line

        // Draw operations further down
        drawSection(g2d, operations, attributesEndY + 10, height);
    }



    private void drawCenteredText(Graphics2D g2d, String text, int yStart, int yEnd) {
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        int x = (width - textWidth) / 2;
        int y = yStart + (yEnd - yStart + textHeight) / 2 - metrics.getDescent();

        g2d.drawString(text, x, y);
    }

    private int drawSection(Graphics2D g2d, List<String> lines, int yStart, int yEnd) {
        int y = yStart + 20; // Padding
        for (String line : lines) {
            g2d.drawString(line, 10, y);
            y += 15;
            if (y >= yEnd) break;
        }
        return y;
    }
}
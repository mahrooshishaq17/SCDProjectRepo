import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class SquareComponent extends JComponent {
    private int width, height;
    private Point dragStartPoint = null;
    private boolean resizing = false;

    private String className = "Class Name";
    private final List<String> attributes = new ArrayList<>();
    private final List<String> methods = new ArrayList<>();

    public SquareComponent(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.width = width;
        this.height = height;
        this.setBackground(Color.getHSBColor(0, 0.0f, 97.3f));
        this.setOpaque(true);

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
                    resizeSquare(e);
                } else if (dragStartPoint != null) {
                    moveSquare(e);
                }
            }
        });
    }
    public void addAttribute(String attribute) {
        attributes.add(attribute);
        updateClassSize();
    }

    // Method to add a method
    public void addMethod(String method) {
        methods.add(method);
        updateClassSize();
    }

    // Method to update class size
    private void updateClassSize() {
        // Calculate new height based on the number of attributes and methods
        int newHeight = 90 + (attributes.size() + methods.size()) * 30; // Adjust according to feature size

        // Update the height directly
        this.height = newHeight;

        // Revalidate and repaint to reflect new size
        setSize(width, height);
        revalidate();
        repaint();
    }

    private void setHeight() {
        // Each attribute/method takes up a certain amount of vertical space
        int verticalSpaceForAttributes = 30;  // Height per attribute
        int verticalSpaceForMethods = 30;     // Height per method

        // Calculate the total height based on the number of attributes and methods
        int newHeight = 90 + (attributes.size() * verticalSpaceForAttributes) + (methods.size() * verticalSpaceForMethods);

        // Update the height of the component
        this.height = newHeight;
    }

    private boolean isOnResizeCorner(Point p) {
        return p.x >= width - 10 && p.y >= height - 10;
    }

    private void resizeSquare(MouseEvent e) {
        int newWidth = e.getX();
        int newHeight = e.getY();
        if (newWidth > 60 && newHeight > 60) {
            width = newWidth;
            height = newHeight;

            // Adjust height if resized
            updateClassSize();

            setSize(width, height);
            repaint();
        }
    }



    private void moveSquare(MouseEvent e) {
        Point newLocation = getLocation();
        newLocation.translate(e.getX() - dragStartPoint.x, e.getY() - dragStartPoint.y);
        setLocation(newLocation);
    }

    private void handleTextInput(MouseEvent e) {
        int compartmentHeight = height / 3;

        if (e.getClickCount() == 2) { // Double-click to edit
            if (e.getY() < compartmentHeight) {
                // Top compartment (Class Name)
                String input = JOptionPane.showInputDialog(this, "Enter Class Name:", className);
                if (input != null && !input.isBlank()) {
                    className = input;
                    System.out.println("Class Name updated: " + className);
                    revalidate();
                    repaint();
                }
            } else if (e.getY() < 2 * compartmentHeight) {
                // Middle compartment (Attributes)
                String input = getInputWithAccessSpecifier("Enter Attribute:");
                if (input != null && !input.isBlank()) {
                    addAttribute(input);// Update the display
                }
            } else {
                // Bottom compartment (Methods)
                String input = getInputWithAccessSpecifier("Enter Method:");
                if (input != null && !input.isBlank()) {
                    addMethod(input);
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
        int compartmentHeight = height / 3; // Divide square into 3 compartments
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);  // Draw background

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, width - 1, height - 1);  // Draw border

        // Draw compartment dividers (only horizontal lines between compartments)
        g2d.drawLine(0, compartmentHeight, width, compartmentHeight);  // Class name divider
        g2d.drawLine(0, 2 * compartmentHeight, width, 2 * compartmentHeight);  // Attributes divider

        // Draw the class name
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(className, 10, compartmentHeight / 2);

        // Draw attributes
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int y = compartmentHeight + 20; // Adjust y starting position
        for (String attribute : attributes) {
            g2d.drawString(attribute, 10, y);
            y += 15;  // Increment y for the next attribute
        }

        // Draw methods
        y = 2 * compartmentHeight + 20; // Start drawing methods below attributes
        for (String method : methods) {
            g2d.drawString(method, 10, y);
            y += 15;  // Increment y for the next method
        }
    }


    private void drawCenteredText(Graphics2D g2d, String text, int yStart, int yEnd) {
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();

        int x = (width - textWidth) / 2;
        int y = yStart + (yEnd - yStart + textHeight) / 2 - metrics.getDescent();

        g2d.drawString(text, x, y);
    }

    private void drawMultiLineText(Graphics2D g2d, List<String> lines, int yStart, int yEnd) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineHeight = metrics.getHeight(); // Ensure consistent spacing
        int y = yStart + lineHeight; // Start after padding

        for (String line : lines) {
            if (y + lineHeight > yEnd) break;
            System.out.println("Drawing line at y: " + y + " -> " + line);// Prevent overflow
            g2d.drawString(line, 10, y); // Adjust x-offset for alignment
            y += lineHeight; // Move down by the line height
        }
    }

}


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UMLDiagramDrawer extends JPanel {
    private JPanel drawingPanel;
    private ArrayList<UMLComponent> components;
    private UMLComponent selectedComponent;
    private boolean resizing;
    private boolean moving;
    private Point dragStart;
    private boolean packageNamed;
    private UMLComponent movingComponent;
    private JButton packageButton;
    private JButton importButton;
    private JButton accessButton;
    private ArrayList<UMLRelationship> relationships;
    private Point lineStartPoint;
    private Point lineEndPoint;
    private boolean drawingLine;
    private String currentLineType; // Track whether it's an import or access line

    public UMLDiagramDrawer() {
        setBackground(Color.WHITE); // Set background color
        setBorder(BorderFactory.createTitledBorder("Package Diagram Editor")); // Add border with title
        setLayout(null); // Assuming free placement of components for drawing

        components = new ArrayList<>();
        relationships = new ArrayList<>();
        selectedComponent = null;
        resizing = false;
        moving = false;
        packageNamed = false;
        drawingLine = false;

        setLayout(new BorderLayout());

        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        packageButton = new JButton("Package");
        importButton = new JButton("Import");
        accessButton = new JButton("Access");
        packageButton.setVisible(false);
        accessButton.setVisible(false);
        importButton.setVisible(false);

        packageButton.addActionListener(e -> handlePackageButtonClick());
        topBar.add(packageButton);
        importButton.addActionListener(e -> handleImportButtonClick());
        topBar.add(importButton);
        accessButton.addActionListener(e -> handleAccessButtonClick());
        topBar.add(accessButton);

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (UMLComponent component : components) {
                    component.draw(g, component == selectedComponent);
                }

                for (UMLRelationship relationship : relationships) {
                    relationship.draw(g);
                }

                // If drawing a line (import or access), draw it as a dashed line
                if (drawingLine && lineStartPoint != null && lineEndPoint != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
                    g2d.drawLine(lineStartPoint.x, lineStartPoint.y, lineEndPoint.x, lineEndPoint.y);
                    drawArrowHead(g2d, lineStartPoint, lineEndPoint);
                }
            }
        };
        drawingPanel.setBackground(Color.WHITE);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Handle right-click interactions
                    for (UMLComponent component : components) {
                        if (component.contains(e.getPoint())) {
                            if (component instanceof UMLPackage) {
                                // Show the add options for UMLPackage
                                showAddOptions(e);
                            } else {
                                // Show delete menu for non-package components
                                showDeleteMenu(e, component);
                            }
                            return;
                        }
                    }
                    // Check for relationships to delete
                    for (UMLRelationship relationship : relationships) {
                        if (isPointNearLine(relationship, e.getPoint())) {
                            showDeleteMenu(e, relationship);
                            return;
                        }
                    }
                } else {
                    // Handle left-click or other interactions
                    if (drawingLine) {
                        lineStartPoint = e.getPoint(); // Set start point to the mouse press location
                        lineEndPoint = null; // Reset the end point
                    } else {
                        resizing = false;
                        moving = false;
                        dragStart = e.getPoint();
                        for (UMLComponent component : components) {
                            if (component instanceof UMLPackage) {
                                UMLPackage pkg = (UMLPackage) component;
                                if (pkg.isOnResizeHandle(e.getPoint()) && packageNamed) {
                                    selectedComponent = pkg;
                                    resizing = true;
                                    return;
                                } else if (pkg.contains(e.getPoint())) {
                                    selectedComponent = pkg;
                                    if (SwingUtilities.isLeftMouseButton(e)) {
                                        if (!packageNamed) {
                                            renameComponent(pkg, "Package");
                                        }
                                    }
                                    moving = true;
                                    movingComponent = pkg;
                                    dragStart = e.getPoint();
                                    return;
                                }
                            } else if (component instanceof UMLClass && component.contains(e.getPoint())) {
                                selectedComponent = component;
                                if (SwingUtilities.isLeftMouseButton(e)) {
                                    renameComponent((UMLClass) component, "Class");
                                }
                                moving = true;
                                movingComponent = component;
                                return;
                            }
                        }
                        selectedComponent = null;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawingLine && lineStartPoint != null) {
                    lineEndPoint = e.getPoint(); // Set end point to the mouse release location
                    if (lineEndPoint != null) {
                        // Add the relationship using the start and end points
                        relationships.add(new UMLRelationship(lineStartPoint, lineEndPoint, currentLineType));
                    }
                    drawingLine = false; // Reset drawing mode
                    lineStartPoint = null; // Reset start point
                    lineEndPoint = null; // Reset end point
                    repaint(); // Trigger repaint to show the line
                }
            }
        });
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (moving && movingComponent != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    movingComponent.move(dx, dy);
                    dragStart = e.getPoint();
                    repaint();
                }
                if (resizing && selectedComponent instanceof UMLPackage) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    UMLPackage pkg = (UMLPackage) selectedComponent;
                    pkg.resize(dx, dy);
                    dragStart = e.getPoint();
                    repaint();
                }
                if (drawingLine && lineStartPoint != null) {
                    lineEndPoint = e.getPoint();
                    drawingPanel.repaint(); // Trigger repaint to show updated line
                }
            }
        });

        add(drawingPanel, BorderLayout.CENTER);
    }

    public JButton getPackageButton() {
        return packageButton;
    }

    public JButton getImportButton() {
        return importButton;
    }

    public JButton getAccessButton() {
        return accessButton;
    }
    public void showPackageButtons(boolean show) {
        packageButton.setVisible(show);
        importButton.setVisible(show);
        accessButton.setVisible(show);
    }
    private void handleImportButtonClick() {
        // Reset drawing state for a new line
        drawingLine = true;
        currentLineType = "<<imports>>"; // Set line type to imports
        repaint(); // Trigger repaint to start drawing the line
    }

    private void handleAccessButtonClick() {
        drawingLine = true; // Enable drawing mode
        currentLineType = "<<access>>"; // Specify the relationship type
        lineStartPoint = null; // Reset start point
        lineEndPoint = null; // Reset end point
        repaint(); // Trigger repaint to reflect the changes
    }

    private void handlePackageButtonClick() {
        addDefaultPackage();
    }

    private void addDefaultPackage() {
        UMLPackage newPackage = new UMLPackage("New Package", 100, 100, 200, 150);
        components.add(newPackage);
        repaint();
    }

    private void showDeleteMenu(MouseEvent e, Object target) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Delete");

        deleteMenuItem.addActionListener(event -> {
            if (target instanceof UMLComponent) {
                components.remove(target);
            } else if (target instanceof UMLRelationship) {
                relationships.remove(target);
            }
            repaint(); // Update the drawing panel after deletion
        });

        popupMenu.add(deleteMenuItem);
        popupMenu.show(drawingPanel, e.getX(), e.getY());
    }

    private boolean isPointNearLine(UMLRelationship relationship, Point p) {
        // Define a tolerance for how close the point must be to the line
        final int TOLERANCE = 5;

        int x1 = relationship.getStart().x;
        int y1 = relationship.getStart().y;
        int x2 = relationship.getEnd().x;
        int y2 = relationship.getEnd().y;

        double distance = Math.abs((y2 - y1) * p.x - (x2 - x1) * p.y + x2 * y1 - y2 * x1) /
                Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
        return distance <= TOLERANCE;
    }

    private void renameComponent(UMLComponent component, String componentType) {
        String newName = JOptionPane.showInputDialog(this, "Enter " + componentType + " name:", component.getName());
        if (newName != null && !newName.trim().isEmpty()) {
            component.setName(newName);
            if (component instanceof UMLPackage) {
                packageNamed = true;
            }
            repaint();
        }
    }


    private void showAddOptions(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addClassMenuItem = new JMenuItem("Add Class");
        JMenuItem addPackageMenuItem = new JMenuItem("Add Package");
        JMenuItem deleteMenuItem = new JMenuItem("Delete");

        // Add Class Option
        addClassMenuItem.addActionListener(ae -> addClassToPackage(e.getPoint()));

        // Add Package Option
        addPackageMenuItem.addActionListener(ae -> addPackageToPackage(e.getPoint()));

        // Delete Option
        deleteMenuItem.addActionListener(ae -> {
            if (selectedComponent instanceof UMLPackage) {
                components.remove(selectedComponent);
                repaint(); // Refresh the panel after deletion
            }
        });

        popupMenu.add(addClassMenuItem);
        popupMenu.add(addPackageMenuItem);
        popupMenu.add(deleteMenuItem);

        popupMenu.show(drawingPanel, e.getX(), e.getY());
    }


    private void addClassToPackage(Point p) {
        if (selectedComponent instanceof UMLPackage) {
            UMLClass newClass = new UMLClass("New Class", p.x, p.y);
            ((UMLPackage) selectedComponent).addClass(newClass);
            components.add(newClass);
            renameComponent(newClass, "Class");
            repaint();
        }
    }

    private void addPackageToPackage(Point p) {
        if (selectedComponent instanceof UMLPackage) {
            UMLPackage newPackage = new UMLPackage("New Package", p.x, p.y, 200, 150);
            ((UMLPackage) selectedComponent).addPackage(newPackage);
            components.add(newPackage);
            renameComponent(newPackage, "Package");
            repaint();
        }
    }

    private void drawArrowHead(Graphics2D g2d, Point start, Point end) {
        int arrowSize = 10;
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int x1 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));
        g2d.drawLine(end.x, end.y, x1, y1);
        g2d.drawLine(end.x, end.y, x2, y2);
    }

    public BufferedImage exportToImage() {
        // Get the width and height of the component
        int width = this.getWidth();
        int height = this.getHeight();

        // Create a new BufferedImage with the width and height of the panel
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Get the graphics object of the image
        Graphics2D g2d = image.createGraphics();

        // Call the paintComponent method to paint the content onto the image
        this.paint(g2d);  // The paint method of JPanel is responsible for rendering the content
        g2d.dispose();    // Dispose of the graphics object to release resources

        return image;     // Return the generated image
    }

    public void clearAll() {
        components.clear();
        relationships.clear();
        removeAll();
        // Optionally reset other states (e.g., zoom level, selection)
        revalidate();
        repaint();
    }
}


abstract class UMLComponent {
    protected int x, y;
    protected String name;

    public UMLComponent(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = "Unnamed";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void draw(Graphics g, boolean isSelected);

    public abstract boolean contains(Point p);

    public abstract void resize(int dx, int dy);

    public abstract void move(int dx, int dy);
}

class UMLPackage extends UMLComponent {
    private int width, height;
    private final int RESIZE_HANDLE_SIZE = 10;
    private ArrayList<UMLComponent> containedComponents;

    public UMLPackage(String name, int x, int y, int width, int height) {
        super(x, y);
        this.name = name;
        this.width = width;
        this.height = height;
        this.containedComponents = new ArrayList<>();
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y - 20, 80, 20);
        g.drawString(name, x + 5, y - 5);
        g.drawRect(x, y, width, height);

        if (isSelected) {
            g.setColor(Color.BLUE);
            g.drawRect(x - 1, y - 1, width + 2, height + 2);
        }

        for (UMLComponent component : containedComponents) {
            component.draw(g, false);
        }
    }

    @Override
    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

    @Override
    public void resize(int dx, int dy) {
        width += dx;
        height += dy;
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void addClass(UMLClass newClass) {
        containedComponents.add(newClass);
    }

    public void addPackage(UMLPackage newPackage) {
        containedComponents.add(newPackage);
    }

    public boolean isOnResizeHandle(Point p) {
        return p.x >= x + width - RESIZE_HANDLE_SIZE && p.x <= x + width && p.y >= y + height - RESIZE_HANDLE_SIZE && p.y <= y + height;
    }
}

class UMLClass extends UMLComponent {
    int width = 100;
    int height = 60;

    public UMLClass(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(name, x + 5, y + 15);

        if (isSelected) {
            g.setColor(Color.BLUE);
            g.drawRect(x - 1, y - 1, width + 2, height + 2);
        }
    }

    @Override
    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

    @Override
    public void resize(int dx, int dy) {
        width += dx;
        height += dy;
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
class UMLRelationship {
    private Point start;
    private Point end;
    private String label;

    public UMLRelationship(Point start, Point end, String label) {
        this.start = start;
        this.end = end;
        this.label = label;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Set dashed stroke for the relationship line
        float[] dashPattern = {10, 5}; // Dash length = 10, Gap length = 5
        g2d.setStroke(new BasicStroke(
                2, // Line width
                BasicStroke.CAP_BUTT, // End-cap style
                BasicStroke.JOIN_BEVEL, // Line join style
                0, // Miter limit
                dashPattern, // Dash pattern
                0 // Dash phase (offset)
        ));
        g2d.setColor(Color.BLACK);

        // Draw the dashed line
        g2d.drawLine(start.x, start.y, end.x, end.y);

        // Draw the arrowhead
        drawArrowHead(g2d, start, end);

        // Draw the label at the midpoint of the line
        int labelX = (start.x + end.x) / 2;
        int labelY = (start.y + end.y) / 2;
        g2d.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font for the label
        g2d.setColor(Color.BLUE); // Set color for the label
        g2d.drawString(label, labelX, labelY - 5); // Offset the label slightly above the line
    }

    // Helper method to draw an arrowhead
    private void drawArrowHead(Graphics2D g2d, Point start, Point end) {
        int arrowSize = 10;
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int x1 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));
        g2d.drawLine(end.x, end.y, x1, y1);
        g2d.drawLine(end.x, end.y, x2, y2);
    }
    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

}


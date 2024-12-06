/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UMLDiagramDrawer extends JFrame {
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

    public UMLDiagramDrawer() {
        setTitle("UML Diagram Drawer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        components = new ArrayList<>();
        selectedComponent = null;
        resizing = false;
        moving = false;
        packageNamed = false;

        setLayout(new BorderLayout());

        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] diagramTypes = {"Select Diagram", "Package Diagram"};
        JComboBox<String> diagramSelector = new JComboBox<>(diagramTypes);
        diagramSelector.addActionListener(e -> {
            if ("Package Diagram".equals(diagramSelector.getSelectedItem())) {
                packageButton.setVisible(true);
            } else {
                packageButton.setVisible(false);
            }
        });
        topBar.add(diagramSelector);

        packageButton = new JButton("Package");
        importButton =new JButton("Import");
        accessButton =new JButton("Access");
        packageButton.setVisible(false);
        importButton.setVisible(false);
        accessButton.setVisible(false);
        packageButton.addActionListener(e -> handlePackageButtonClick());
        topBar.add(packageButton);
        importButton.addActionListener(e -> handleimportButtonClick());
        topBar.add(importButton);
        accessButton.addActionListener(e -> handleaccessButtonClick());
        topBar.add(accessButton);




        add(topBar, BorderLayout.NORTH);

        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (UMLComponent component : components) {
                    component.draw(g, component == selectedComponent);
                }
            }
        };
        drawingPanel.setBackground(Color.WHITE);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                resizing = false;
                moving = false;
                dragStart = e.getPoint();
                for (UMLComponent component : components) {
                    if (component instanceof UMLPackage) {
                        UMLPackage pkg = (UMLPackage) component;
                        if (pkg.isOnResizeHandle(e.getPoint()) && packageNamed) {
                            selectedComponent = pkg;
                            resizing = true;  // Enter resizing state
                            return;
                        } else if (pkg.contains(e.getPoint())) {
                            selectedComponent = pkg;
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                if (!packageNamed) {
                                    renameComponent(pkg, "Package");
                                }
                            } else if (SwingUtilities.isRightMouseButton(e)) {
                                showAddOptions(e);
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

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
                moving = false;
                dragStart = null;
                movingComponent = null;
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (moving && movingComponent != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    movingComponent.move(dx, dy);  // Move the component
                    dragStart = e.getPoint();
                    repaint();
                }
                if (resizing && selectedComponent instanceof UMLPackage) {
                    // If resizing, adjust the size of the UMLPackage
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    UMLPackage pkg = (UMLPackage) selectedComponent;
                    pkg.resize(dx, dy);  // Resize the component
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });

        add(drawingPanel, BorderLayout.CENTER);
    }

    private void handleaccessButtonClick() {

    }

    private void handleimportButtonClick() {
    }

    private void handlePackageButtonClick() {
        addDefaultPackage();
    }

    private void addDefaultPackage() {
        UMLPackage newPackage = new UMLPackage("New Package", 100, 100, 200, 150);
        components.add(newPackage);
        repaint();
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

        addClassMenuItem.addActionListener(ae -> addClassToPackage(e.getPoint()));
        addPackageMenuItem.addActionListener(ae -> addPackageToPackage(e.getPoint()));

        popupMenu.add(addClassMenuItem);
        popupMenu.add(addPackageMenuItem);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UMLDiagramDrawer frame = new UMLDiagramDrawer();
            frame.setVisible(true);
        });
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
            g.fillRect(x + width - RESIZE_HANDLE_SIZE, y + height - RESIZE_HANDLE_SIZE, RESIZE_HANDLE_SIZE, RESIZE_HANDLE_SIZE);
        }

        for (UMLComponent component : containedComponents) {
            component.draw(g, false);
        }
    }

    @Override
    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }

    public boolean isOnResizeHandle(Point p) {
        return p.x >= x + width - RESIZE_HANDLE_SIZE && p.x <= x + width
                && p.y >= y + height - RESIZE_HANDLE_SIZE && p.y <= y + height;
    }

    @Override
    public void resize(int dx, int dy) {
        this.width += dx;
        this.height += dy;
    }

    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void addClass(UMLClass umlClass) {
        containedComponents.add(umlClass);
    }

    public void addPackage(UMLPackage umlPackage) {
        containedComponents.add(umlPackage);
    }
}

class UMLClass extends UMLComponent {
    private int width = 150, height = 100;

    public UMLClass(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.drawString(name, x + 5, y + 20);

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
}
*/




/*
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UMLDiagramDrawer extends JFrame {
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

    public UMLDiagramDrawer() {
        setTitle("UML Diagram Drawer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        components = new ArrayList<>();
        relationships = new ArrayList<>();
        selectedComponent = null;
        resizing = false;
        moving = false;
        packageNamed = false;

        setLayout(new BorderLayout());

        JPanel topBar = new JPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        String[] diagramTypes = {"Select Diagram", "Package Diagram"};
        JComboBox<String> diagramSelector = new JComboBox<>(diagramTypes);
        diagramSelector.addActionListener(e -> {
            if ("Package Diagram".equals(diagramSelector.getSelectedItem())) {
                packageButton.setVisible(true);
                accessButton.setVisible(true);
                importButton.setVisible(true);
            } else {
                packageButton.setVisible(false);
            }
        });
        topBar.add(diagramSelector);

        packageButton = new JButton("Package");
        importButton = new JButton("Import");
        accessButton = new JButton("Access");
        packageButton.setVisible(false);
        importButton.setVisible(false);
        accessButton.setVisible(false);
        packageButton.addActionListener(e -> handlePackageButtonClick());
        topBar.add(packageButton);
        importButton.addActionListener(e -> handleimportButtonClick());
        topBar.add(importButton);
        accessButton.addActionListener(e -> handleaccessButtonClick());
        topBar.add(accessButton);

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
            }
        };
        drawingPanel.setBackground(Color.WHITE);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                resizing = false;
                moving = false;
                dragStart = e.getPoint();
                for (UMLComponent component : components) {
                    if (component instanceof UMLPackage) {
                        UMLPackage pkg = (UMLPackage) component;
                        if (pkg.isOnResizeHandle(e.getPoint()) && packageNamed) {
                            selectedComponent = pkg;
                            resizing = true;  // Enter resizing state
                            return;
                        } else if (pkg.contains(e.getPoint())) {
                            selectedComponent = pkg;
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                if (!packageNamed) {
                                    renameComponent(pkg, "Package");
                                }
                            } else if (SwingUtilities.isRightMouseButton(e)) {
                                showAddOptions(e);
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

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
                moving = false;
                dragStart = null;
                movingComponent = null;
            }
        });

        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (moving && movingComponent != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    movingComponent.move(dx, dy);  // Move the component
                    dragStart = e.getPoint();
                    repaint();
                }
                if (resizing && selectedComponent instanceof UMLPackage) {
                    // If resizing, adjust the size of the UMLPackage
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    UMLPackage pkg = (UMLPackage) selectedComponent;
                    pkg.resize(dx, dy);  // Resize the component
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });

        add(drawingPanel, BorderLayout.CENTER);
    }

    private void handleaccessButtonClick() {
        if (selectedComponent != null && components.size() > 1) {
            UMLComponent targetComponent = components.get(0); // For demo: You can allow user to select target component
            relationships.add(new UMLRelationship(selectedComponent, targetComponent, "<<access>>"));
            repaint();
        }
    }

    private void handleimportButtonClick() {
        if (selectedComponent != null && components.size() > 1) {
            UMLComponent targetComponent = components.get(0); // For demo: You can allow user to select target component
            relationships.add(new UMLRelationship(selectedComponent, targetComponent, "<<imports>>"));
            repaint();
        }
    }

    private void handlePackageButtonClick() {
        addDefaultPackage();
    }

    private void addDefaultPackage() {
        UMLPackage newPackage = new UMLPackage("New Package", 100, 100, 200, 150);
        components.add(newPackage);
        repaint();
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

        addClassMenuItem.addActionListener(ae -> addClassToPackage(e.getPoint()));
        addPackageMenuItem.addActionListener(ae -> addPackageToPackage(e.getPoint()));

        popupMenu.add(addClassMenuItem);
        popupMenu.add(addPackageMenuItem);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UMLDiagramDrawer frame = new UMLDiagramDrawer();
            frame.setVisible(true);
        });
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

    public boolean isOnResizeHandle(Point p) {
        return p.x >= x + width - RESIZE_HANDLE_SIZE && p.x <= x + width
                && p.y >= y + height - RESIZE_HANDLE_SIZE && p.y <= y + height;
    }

    public void addClass(UMLClass newClass) {
        containedComponents.add(newClass);
    }

    public void addPackage(UMLPackage newPackage) {
        containedComponents.add(newPackage);
    }
}

class UMLClass extends UMLComponent {
    private final int WIDTH = 100, HEIGHT = 100;

    public UMLClass(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    @Override
    public void draw(Graphics g, boolean isSelected) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, WIDTH, HEIGHT);
        g.drawString(name, x + 5, y + 15);

        if (isSelected) {
            g.setColor(Color.BLUE);
            g.drawRect(x - 1, y - 1, WIDTH + 2, HEIGHT + 2);
        }
    }

    @Override
    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + WIDTH && p.y >= y && p.y <= y + HEIGHT;
    }

    @Override
    public void resize(int dx, int dy) {
        // Not resizable for simplicity
    }

    @Override
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
}

class UMLRelationship { private UMLComponent source, target;
    private String type;

    public UMLRelationship(UMLComponent source, UMLComponent target, String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (type.equals("<<import>>")) {
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] {10.0f, 10.0f}, 0.0f));
            g2d.setColor(Color.RED);
            g2d.drawLine(source.x + 50, source.y + 50, target.x + 50, target.y + 50);
            // Add arrowhead
            drawArrow(g2d, target.x + 50, target.y + 50);
        }
        if (type.equals("<<access>>")) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.GREEN);
            g2d.drawLine(source.x + 50, source.y + 50, target.x + 50, target.y + 50);
            // Add arrowhead
            drawArrow(g2d, target.x + 50, target.y + 50);
        }
    }

    private void drawArrow(Graphics2D g2d, int x, int y) {
        int arrowSize = 5;
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(x, y);
        arrowHead.addPoint(x - arrowSize, y - arrowSize);
        arrowHead.addPoint(x + arrowSize, y - arrowSize);
        g2d.fill(arrowHead);
    }
}
*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UMLDiagramDrawer extends JFrame {
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
        setTitle("UML Diagram Drawer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

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

        String[] diagramTypes = {"Select Diagram", "Package Diagram"};
        JComboBox<String> diagramSelector = new JComboBox<>(diagramTypes);
        diagramSelector.addActionListener(e -> {
            if ("Package Diagram".equals(diagramSelector.getSelectedItem())) {
                packageButton.setVisible(true);
                accessButton.setVisible(true);
                importButton.setVisible(true);
            } else {
                packageButton.setVisible(false);
                accessButton.setVisible(false);
                importButton.setVisible(false);
            }
        });
        topBar.add(diagramSelector);

        packageButton = new JButton("Package");
        importButton = new JButton("Import");
        accessButton = new JButton("Access");
        packageButton.setVisible(false);
        importButton.setVisible(false);
        accessButton.setVisible(false);
        packageButton.addActionListener(e -> handlePackageButtonClick());
        topBar.add(packageButton);
        importButton.addActionListener(e -> handleImportButtonClick());
        topBar.add(importButton);
        accessButton.addActionListener(e -> handleAccessButtonClick());
        topBar.add(accessButton);

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
                if (drawingLine) {
                    lineStartPoint = e.getPoint();
                    lineEndPoint = e.getPoint(); // Initialize to the same point initially
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
                                } else if (SwingUtilities.isRightMouseButton(e)) {
                                    showAddOptions(e);
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

            @Override
            public void mouseReleased(MouseEvent e) {
                // Check if we are in the process of drawing a line
                if (drawingLine) {
                    // Ensure both start and end points are not null
                    if (lineStartPoint != null && lineEndPoint != null) {
                        // Add the new relationship to the relationships list
                        // This assumes UMLRelationship is a class that takes start and end points and a line type
                        relationships.add(new UMLRelationship(lineStartPoint, lineEndPoint, currentLineType));

                        // Debugging message (optional)
                        System.out.println("Line drawn from " + lineStartPoint + " to " + lineEndPoint);
                    } else {
                        // Debugging message for incomplete line drawing
                        System.out.println("Incomplete line: Missing start or end point.");
                    }

                    // Reset the state
                    drawingLine = false; // Stop the drawing process
                    lineStartPoint = null; // Reset the start point
                    lineEndPoint = null; // Reset the end point

                    // Repaint the panel to finalize the drawn line
                    drawingPanel.repaint();
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

    private void handleImportButtonClick() {
        // Reset drawing state for a new line
        drawingLine = true;
        currentLineType = "<<imports>>"; // Set line type to imports
        repaint(); // Trigger repaint to start drawing the line
    }

    private void handleAccessButtonClick() {
        // Reset drawing state for a new line
        drawingLine = true;
        currentLineType = "<<access>>"; // Set line type to access
        repaint(); // Trigger repaint to start drawing the line
    }

    private void handlePackageButtonClick() {
        addDefaultPackage();
    }

    private void addDefaultPackage() {
        UMLPackage newPackage = new UMLPackage("New Package", 100, 100, 200, 150);
        components.add(newPackage);
        repaint();
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

        addClassMenuItem.addActionListener(ae -> addClassToPackage(e.getPoint()));
        addPackageMenuItem.addActionListener(ae -> addPackageToPackage(e.getPoint()));

        popupMenu.add(addClassMenuItem);
        popupMenu.add(addPackageMenuItem);
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UMLDiagramDrawer().setVisible(true));
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
    private int width = 100;
    private int height = 60;

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
}

class UMLRelationship {
    private UMLComponent start;
    private UMLComponent end;
    private String label;

    public UMLRelationship(UMLComponent start, UMLComponent end, String label) {
        this.start = start;
        this.end = end;
        this.label = label;
    }

    public UMLRelationship(Point importStartPoint, Point importEndPoint, String label) {

    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(start.x + 50, start.y + 30, end.x + 50, end.y + 30);
        g.drawString(label, (start.x + end.x) / 2, (start.y + end.y) / 2);
    }
}








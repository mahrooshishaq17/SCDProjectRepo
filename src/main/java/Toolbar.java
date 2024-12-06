import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends JPanel {
    private List<JButton> currentButtons;
    private DiagramEditorPanel diagramEditorPanel;
    private AssociationLine currentAssociationLine;
    private InheritanceLine  currentInheritanceLine;
    private AggregationLine currentAggregationLine;
    private CompositionLine currentCompositionLine;
    private String activeTool=null;


    public Toolbar(DiagramEditorPanel diagramEditorPanel) {
        this.diagramEditorPanel = diagramEditorPanel;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createTitledBorder("Toolbar"));
        currentButtons = new ArrayList<>();
        addRightClickMenu();
    }

    public void updateToolsForDiagram(String diagramType) {
        // Clear existing buttons
        removeAll();
        currentButtons.clear();

        // Add buttons based on diagram type
        if ("Class Diagram".equals(diagramType)) {
            addToolButton("Class");
            addToolButton("Abstract Class");
            addToolButton("Interface");
            addToolButton("Association");
            addToolButton("Inheritance");
            addToolButton("Aggregation");
            addToolButton("Composition");
        } else if ("Package Diagram".equals(diagramType)) {
            addToolButton("Package");
            addToolButton("Access");
            addToolButton("Import");


        }

        // Refresh UI
        revalidate();
        repaint();
    }

    private void addToolButton(String label) {
        JButton button = new JButton(label);
        currentButtons.add(button);
        add(button);
        button.addActionListener(e -> handleButtonClick(label));
        this.revalidate();
        this.repaint();
    }

    private void handleButtonClick(String label) {
        if(activeTool==null || !activeTool.equals(label))
            activeTool=label;
        else
            activeTool=null;
        switch (label) {
            case "Class":

                SquareComponent square = new SquareComponent(50, 50, 150, 90); // Initial size and position
                diagramEditorPanel.add(square); // Add to diagram editor
                diagramEditorPanel.revalidate(); // Refresh layout
                diagramEditorPanel.repaint();   // Repaint to show the changes
                break;
            // System.out.println("Class button clicked");
            case "Interface":
                InterfaceComponent interfaceComponent = new InterfaceComponent(70, 70, 90);
                diagramEditorPanel.add(interfaceComponent);
                diagramEditorPanel.revalidate();
                diagramEditorPanel.repaint();
            case "Association":
                // Code to handle "Association" button click
                if (activeTool != null)
                { startAssociationLine();}

                // System.out.println("Association button clicked");
                break;
            case "Inheritance":
                if (activeTool != null) startInheritanceLine();
                break;
            case "Aggregation":
                if (activeTool != null) startAggregationLine();
                break;
            case "Composition":
                if (activeTool != null) startCompositionLine();
                break;

            // Code to handle "Inheritance" button click
            //System.out.println("Inheritance button clicked");
            //break;
            case "Package":
                // Code to handle "Package" button click

                //System.out.println("Package button clicked");
                break;
            case "Import":
                // Code to handle "Dependency" button click
                System.out.println("Dependency button clicked");
                break;
            case "Access":
                break;
            default:
                System.out.println("Unknown button clicked: " + label);
        }
    }
    private void startAssociationLine() {
        // Reset any existing lines
        currentAssociationLine = null;

        // Use mouse listener to start drawing the line
        diagramEditorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (AssociationLine line : diagramEditorPanel.getAssociationLines()) {
                    if (line.isOnStart(e.getPoint()) || line.isOnEnd(e.getPoint()) || line.isOnLine(e.getPoint())) {
                        // Set the clicked line as the current line
                        currentAssociationLine = line;
                        currentAssociationLine.startResizing(e.getPoint());
                        currentAssociationLine.startMoving(e.getPoint());
                        return;
                    }
                }

                // If no existing line is clicked, create a new one
                currentAssociationLine = new AssociationLine(e.getPoint(), e.getPoint(), diagramEditorPanel);
                diagramEditorPanel.add(currentAssociationLine);
                diagramEditorPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentAssociationLine != null) {
                    currentAssociationLine.stopResizing();
                    currentAssociationLine.stopMoving();  // Stop moving when released
                    diagramEditorPanel.repaint(); // Repaint after resizing/moving
                }
            }
        });

        diagramEditorPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentAssociationLine != null) {
                    if (currentAssociationLine.resizingStart || currentAssociationLine.resizingEnd) {
                        currentAssociationLine.resize(e.getPoint());
                    } else if (currentAssociationLine.moving) {
                        currentAssociationLine.move(e.getPoint());
                    }
                    diagramEditorPanel.repaint();
                }
            }
        });
    }
    private void startInheritanceLine() {
        currentInheritanceLine = null; // Reset before starting a new line
        diagramEditorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    currentInheritanceLine = new InheritanceLine(e.getPoint(), diagramEditorPanel);
                    diagramEditorPanel.add(currentInheritanceLine);
                    diagramEditorPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && currentInheritanceLine != null) {
                    currentInheritanceLine.stopResizing();
                    currentInheritanceLine = null; // Reset after use
                    diagramEditorPanel.repaint();
                }
            }
        });

        diagramEditorPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentInheritanceLine != null) {
                    currentInheritanceLine.updateEndPoint(e.getPoint());
                    diagramEditorPanel.repaint();
                }
            }
        });
    }

    private void startAggregationLine() {
        currentAggregationLine = null; // Reset before starting a new line
        diagramEditorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    currentAggregationLine = new AggregationLine(e.getPoint(), diagramEditorPanel);
                    diagramEditorPanel.add(currentAggregationLine);
                    diagramEditorPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && currentAggregationLine != null) {
                    currentAggregationLine.stopResizing();
                    currentAggregationLine = null; // Reset after use
                    diagramEditorPanel.repaint();
                }
            }
        });

        diagramEditorPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentAggregationLine != null) {
                    currentAggregationLine.updateEndPoint(e.getPoint());
                    diagramEditorPanel.repaint();
                }
            }
        });
    }
    private void startCompositionLine() {
        currentCompositionLine = null; // Reset before starting a new line
        diagramEditorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    currentCompositionLine = new CompositionLine(e.getPoint(), diagramEditorPanel);
                    diagramEditorPanel.add(currentCompositionLine);
                    diagramEditorPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && currentCompositionLine != null) {
                    currentCompositionLine.stopResizing();
                    currentCompositionLine = null; // Reset after use
                    diagramEditorPanel.repaint();
                }
            }
        });

        diagramEditorPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentCompositionLine != null) {
                    currentCompositionLine.updateEndPoint(e.getPoint());
                    diagramEditorPanel.repaint();
                }
            }
        });
    }


    private void addRightClickMenu() {
        diagramEditorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // boolean contextShown=false;
                    // Check if the click is on an InheritanceLine
                    for (InheritanceLine line : diagramEditorPanel.getInheritanceLines()) {
                        if (line.isOnLine(e.getPoint())) {
                            currentInheritanceLine = line;
                            showContextMenu(e.getPoint(),line);
                            // contextShown=true;
                            return; // Exit early since an inheritance line was found
                        }
                    }
                    // Check if the click is on an AssociationLine
                    for (AssociationLine line : diagramEditorPanel.getAssociationLines()) {
                        if (line.isOnLine(e.getPoint())) {
                            currentAssociationLine = line;
                            showContextMenu(e.getPoint(), line);
                            return; // Exit early since an association line was found
                        }
                    }
                    for (AggregationLine line : diagramEditorPanel.getAggregationLines()) {
                        if (line.isOnLine(e.getPoint())) {
                            currentAggregationLine = line;
                            showContextMenu(e.getPoint(),line);
                            // contextShown=true;
                            return; // Exit early since an inheritance line was found
                        }
                    }
                    for (CompositionLine line : diagramEditorPanel.getCompositionLines()) {
                        if (line.isOnLine(e.getPoint())) {
                            showContextMenu(e.getPoint(), line);
                            return;
                        }
                    }
                }
            }
        });
    }

    private void showContextMenu(Point point, Line line) {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem removeItem = new JMenuItem("Remove " + line.getClass().getSimpleName());
        removeItem.addActionListener(e -> removeLine(line));

        JMenuItem descriptionItem = new JMenuItem("Write Description");
        descriptionItem.addActionListener(e -> writeDescription(line, diagramEditorPanel));

        contextMenu.add(removeItem);
        contextMenu.add(descriptionItem);
        contextMenu.show(diagramEditorPanel, point.x, point.y);
    }
    private void removeLine(Line line) {
        if (line instanceof AssociationLine) {
            diagramEditorPanel.removeAssociationLine((AssociationLine) line);
        } else if (line instanceof InheritanceLine) {
            diagramEditorPanel.removeInheritanceLine((InheritanceLine) line);
        } else if (line instanceof AggregationLine) {
            diagramEditorPanel.removeAggregationLine((AggregationLine) line);
        } else if (line instanceof CompositionLine) {
            diagramEditorPanel.removeCompositionLine((CompositionLine) line);
        }
        diagramEditorPanel.repaint();
    }

    private void writeDescription(Line line, DiagramEditorPanel diagramEditorPanel) {
        String description = JOptionPane.showInputDialog(diagramEditorPanel, "Enter description for the " + line.getClass().getSimpleName() + ":");
        if (description != null && !description.trim().isEmpty()) {
            line.setDescription(description);  // Set the description for the line
        }
    }


    private void removeInheritance(InheritanceLine line) {
        diagramEditorPanel.removeInheritanceLine(line);
        diagramEditorPanel.repaint();
    }





}
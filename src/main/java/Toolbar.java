import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends JPanel {
    private List<JButton> currentButtons;
    private DiagramEditorPanel diagramEditorPanel;


    public Toolbar(DiagramEditorPanel diagramEditorPanel) {
        this.diagramEditorPanel = diagramEditorPanel;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createTitledBorder("Toolbar"));
        currentButtons = new ArrayList<>();
    }

    public void updateToolsForDiagram(String diagramType) {
        // Clear existing buttons
        removeAll();
        currentButtons.clear();

        // Add buttons based on diagram type
        if ("Class Diagram".equals(diagramType)) {
            addToolButton("Class");
            addToolButton("Association");
            addToolButton("Inheritance");
        } else if ("Package Diagram".equals(diagramType)) {
            addToolButton("Package");
            addToolButton("Dependency");
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
        switch (label) {
            case "Class":
                // Code to handle "Class" button click

//                String className = JOptionPane.showInputDialog(
//                        this,
//                        "Enter the name of the class:",
//                        "Class Name",
//                        JOptionPane.PLAIN_MESSAGE
//                );

               // if (className != null && !className.trim().isEmpty()) {
                    // Create a new SquareComponent with the class name
                    SquareComponent square = new SquareComponent(50, 50, 150, 90); // Initial size and position
                    diagramEditorPanel.add(square); // Add to diagram editor
                    diagramEditorPanel.revalidate(); // Refresh layout
                    diagramEditorPanel.repaint();   // Repaint to show the changes
               // } else {
//                    JOptionPane.showMessageDialog(
//                            this,
//                            "Class name cannot be empty!",
//                            "Error",
//                            JOptionPane.ERROR_MESSAGE
//                    );

                break;
            // System.out.println("Class button clicked");
            case "Association":
                // Code to handle "Association" button click
                System.out.println("Association button clicked");
                break;
            case "Inheritance":
                // Code to handle "Inheritance" button click
                System.out.println("Inheritance button clicked");
                break;
            case "Package":
                // Code to handle "Package" button click
                System.out.println("Package button clicked");
                break;
            case "Dependency":
                // Code to handle "Dependency" button click
                System.out.println("Dependency button clicked");
                break;
            default:
                System.out.println("Unknown button clicked: " + label);
        }
    }
}

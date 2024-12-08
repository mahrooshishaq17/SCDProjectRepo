import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
public class ProjectManagerPanel extends JPanel {
    private DiagramEditorPanel diagramEditorPanel;
    private UMLDiagramDrawer diagramDrawer;
    private MainFrame mainFrame;

    public ProjectManagerPanel(DiagramEditorPanel diagramEditorPanel, UMLDiagramDrawer diagramDrawer, MainFrame mainFrame) {
        this.diagramEditorPanel = diagramEditorPanel; // Reference to the main diagram editor
        this.diagramDrawer = diagramDrawer; // Reference to the UML diagram drawer for package diagrams
        this.mainFrame = mainFrame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createTitledBorder("Project Manager"));

        JButton newProjectButton = new JButton("New Project");
        JButton openProjectButton = new JButton("Open Project");
        JButton saveProjectButton = new JButton("Save Project");

        // Add buttons with spacing
        newProjectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openProjectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveProjectButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10)); // Add spacing
        add(newProjectButton);
        add(Box.createVerticalStrut(10)); // Add spacing
        add(openProjectButton);
        add(Box.createVerticalStrut(10)); // Add spacing
        add(saveProjectButton);

        // Add action listeners
        newProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNewProject();
            }
        });

        openProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOpenProject();
            }
        });

        saveProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveProject();
            }
        });
    }

    // Method to handle "New Project" action
    private void handleNewProject() {
        String currentDiagramType = mainFrame.getCurrentDiagramType(); // Get the current diagram type

        int confirm = JOptionPane.showConfirmDialog(
                ProjectManagerPanel.this,
                "Do you want to save the current project before starting a new one?",
                "Confirm New Project",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (confirm == JOptionPane.CANCEL_OPTION || confirm == JOptionPane.CLOSED_OPTION) {
            return; // User canceled
        }

        if (confirm == JOptionPane.YES_OPTION) {
            handleSaveProject(); // Save the project before clearing
        }

        // Clear the respective diagram contents based on the selected type
        if ("Class Diagram".equals(currentDiagramType)) {
            // Clear Class Diagram
            diagramEditorPanel.clearAll();
            diagramEditorPanel.removeAll();
            diagramEditorPanel.revalidate();
            diagramEditorPanel.repaint();
        } else if ("Package Diagram".equals(currentDiagramType)) {
            // Clear Package Diagram components
            diagramDrawer.removeAll();
            diagramDrawer.clearAll(); // Custom method to clear UMLDiagramDrawer contents
            diagramDrawer.revalidate();
            diagramDrawer.repaint();
        }
    }


    // Method to handle "Open Project" action
    private void handleOpenProject() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Load project data from the selected file (stubbed example)
            JOptionPane.showMessageDialog(this, "Project loaded from: " + file.getAbsolutePath());

            // Clear and populate the diagram editor with loaded data (pseudo-code)
            diagramEditorPanel.removeAll(); // Clear current contents
            diagramEditorPanel.revalidate();
            diagramEditorPanel.repaint();

            // Logic to populate the editor from the file would go here
        }
    }

    // Method to handle "Save Project" action
    // Method to handle "Save Project" action
    private void handleSaveProject() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG Image", "jpg"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".jpg")) {
                file = new File(file.getAbsolutePath() + ".jpg");
            }

            try {
                BufferedImage diagramImage;
                String currentDiagramType = mainFrame.getCurrentDiagramType();

                if ("Class Diagram".equals(currentDiagramType)) {
                    diagramImage = diagramEditorPanel.exportToImage();
                } else if ("Package Diagram".equals(currentDiagramType)) {
                    diagramImage = diagramDrawer.exportToImage();
                } else {
                    throw new IllegalStateException("Unknown diagram type: " + currentDiagramType);
                }

                if (diagramImage != null) {
                    ImageIO.write(diagramImage, "jpg", file);
                    JOptionPane.showMessageDialog(this, "Project saved as JPG: " + file.getAbsolutePath());
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving the diagram: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
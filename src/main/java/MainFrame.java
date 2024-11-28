import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private DiagramEditorPanel diagramEditorPanel;
    private Toolbar toolbar;
    private PropertiesPanel propertiesPanel;
    private ProjectManagerPanel projectManagerPanel;
    private JComboBox<String> diagramTypeSelector;

    public MainFrame() {
        setTitle("UML Editor");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize Panels
        diagramEditorPanel = new DiagramEditorPanel();
        propertiesPanel = new PropertiesPanel();
        projectManagerPanel = new ProjectManagerPanel();

        // Initialize Toolbar
        toolbar = new Toolbar(diagramEditorPanel);

        // Diagram Type Selector
        diagramTypeSelector = new JComboBox<>(new String[]{"Class Diagram", "Package Diagram"});
        diagramTypeSelector.addActionListener(new DiagramTypeChangeListener());

        // Add Components to Frame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(diagramTypeSelector, BorderLayout.WEST);
        topPanel.add(toolbar, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(propertiesPanel, BorderLayout.EAST);
        add(projectManagerPanel, BorderLayout.WEST);
        add(diagramEditorPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Listener to Handle Diagram Type Changes
    private class DiagramTypeChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedDiagram = (String) diagramTypeSelector.getSelectedItem();
            toolbar.updateToolsForDiagram(selectedDiagram);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}



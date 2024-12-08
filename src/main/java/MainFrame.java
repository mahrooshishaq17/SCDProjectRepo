import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//final good working code
public class MainFrame extends JFrame {
    private DiagramEditorPanel diagramEditorPanel;
    private Toolbar toolbar;
   // private PropertiesPanel propertiesPanel;
    private ProjectManagerPanel projectManagerPanel;
    private JComboBox<String> diagramTypeSelector;
    private UMLDiagramDrawer diagramDrawer;

    public MainFrame() {
        setTitle("UML Editor");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize Panels
        diagramEditorPanel = new DiagramEditorPanel();
        //propertiesPanel = new PropertiesPanel();
        projectManagerPanel = new ProjectManagerPanel();
        diagramDrawer = new UMLDiagramDrawer();

        // Initialize Toolbar
        toolbar = new Toolbar(diagramEditorPanel);

        // Diagram Type Selector
        diagramTypeSelector = new JComboBox<>(new String[]{"Class Diagram", "Package Diagram"});
        diagramTypeSelector.addActionListener(new DiagramTypeChangeListener());

        // Add Components to Frame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(diagramTypeSelector, BorderLayout.WEST);
        topPanel.add(toolbar, BorderLayout.CENTER);  // Add toolbar initially

        add(topPanel, BorderLayout.NORTH);
        //add(propertiesPanel, BorderLayout.EAST);
        add(projectManagerPanel, BorderLayout.WEST);
        setVisible(true);
    }

    // Listener to Handle Diagram Type Changes
    private class DiagramTypeChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedDiagram = (String) diagramTypeSelector.getSelectedItem();

            // Get the topPanel (which holds the diagram selector and toolbar/buttons)
            JPanel topPanel = (JPanel) getContentPane().getComponent(0);

            // Clear topPanel before adding new components
            topPanel.removeAll();

            if ("Class Diagram".equals(selectedDiagram)) {
                // Remove package diagram and add class diagram components
                remove(diagramDrawer);
                add(diagramEditorPanel, BorderLayout.CENTER);

                toolbar.updateToolsForDiagram("Class Diagram");

                // Add the diagram type selector and toolbar back
                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
                topPanel.add(toolbar, BorderLayout.CENTER);  // Show class diagram toolbar
            } else if ("Package Diagram".equals(selectedDiagram)) {
                // Remove class diagram and add package diagram components
                remove(diagramEditorPanel);
                add(diagramDrawer, BorderLayout.CENTER);

                // Show package diagram buttons
                diagramDrawer.showPackageButtons(true);

                // Add the diagram type selector and package buttons to topPanel
                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
                JPanel packageButtonPanel = new JPanel();

                packageButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
                packageButtonPanel.setBackground(new Color(230, 230, 230));
                packageButtonPanel.setBorder(BorderFactory.createTitledBorder("Toolbar"));

                packageButtonPanel.add(diagramDrawer.getPackageButton());
                packageButtonPanel.add(diagramDrawer.getImportButton());
                packageButtonPanel.add(diagramDrawer.getAccessButton());

                topPanel.add(packageButtonPanel, BorderLayout.CENTER);  // Show package buttons
            }

            // Ensure the layout is refreshed and components are visible
            topPanel.revalidate();
            topPanel.repaint();
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}



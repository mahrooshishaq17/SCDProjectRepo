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


    private String CurrentDiagram;


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

        diagramDrawer = new UMLDiagramDrawer();

        projectManagerPanel = new ProjectManagerPanel(diagramEditorPanel,diagramDrawer,this);


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

    public String getCurrentDiagramType() {
        return CurrentDiagram;
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

                CurrentDiagram="Class Diagram";

                // Remove package diagram and add class diagram components
                remove(diagramDrawer);
                add(diagramEditorPanel, BorderLayout.CENTER);

                toolbar.updateToolsForDiagram("Class Diagram");

                // Add the diagram type selector and toolbar back
                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
                topPanel.add(toolbar, BorderLayout.CENTER);  // Show class diagram toolbar
            } else if ("Package Diagram".equals(selectedDiagram)) {


                CurrentDiagram="Package Diagram";

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










//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class MainFrame extends JFrame {
//    private DiagramEditorPanel diagramEditorPanel;
//    private Toolbar toolbar;
//    private ProjectManagerPanel projectManagerPanel;
//    private JComboBox<String> diagramTypeSelector;
//    private UMLDiagramDrawer diagramDrawer;
//
//    public MainFrame() {
//        setTitle("UML Editor");
//        setSize(1200, 800);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout(10, 10));
//
//        // Initialize Panels
//        diagramEditorPanel = new DiagramEditorPanel();  // Diagram editor panel
//        projectManagerPanel = new ProjectManagerPanel(diagramEditorPanel);  // Pass diagramEditorPanel to ProjectManagerPanel
//        diagramDrawer = new UMLDiagramDrawer();  // UML diagram drawer for package diagrams
//
//        // Initialize Toolbar
//        toolbar = new Toolbar(diagramEditorPanel);  // Pass diagramEditorPanel to Toolbar
//
//        // Diagram Type Selector
//        diagramTypeSelector = new JComboBox<>(new String[]{"Class Diagram", "Package Diagram"});
//        diagramTypeSelector.addActionListener(new DiagramTypeChangeListener());
//
//        // Add Components to Frame
//        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.add(diagramTypeSelector, BorderLayout.WEST);
//        topPanel.add(toolbar, BorderLayout.CENTER);  // Add toolbar initially
//
//        add(topPanel, BorderLayout.NORTH);  // Add top panel with toolbar
//        add(projectManagerPanel, BorderLayout.WEST);  // Add project manager panel to the left
//        setVisible(true);
//    }
//
//    // Listener to Handle Diagram Type Changes
//    private class DiagramTypeChangeListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String selectedDiagram = (String) diagramTypeSelector.getSelectedItem();
//
//            // Get the topPanel (which holds the diagram selector and toolbar/buttons)
//            JPanel topPanel = (JPanel) getContentPane().getComponent(0);
//
//            // Clear topPanel before adding new components
//            topPanel.removeAll();
//
//            if ("Class Diagram".equals(selectedDiagram)) {
//                // Remove package diagram and add class diagram components
//                remove(diagramDrawer);
//                add(diagramEditorPanel, BorderLayout.CENTER);
//
//                toolbar.updateToolsForDiagram("Class Diagram");
//
//                // Add the diagram type selector and toolbar back
//                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
//                topPanel.add(toolbar, BorderLayout.CENTER);  // Show class diagram toolbar
//            } else if ("Package Diagram".equals(selectedDiagram)) {
//                // Remove class diagram and add package diagram components
//                remove(diagramEditorPanel);
//                add(diagramDrawer, BorderLayout.CENTER);
//
//                // Show package diagram buttons
//                diagramDrawer.showPackageButtons(true);
//
//                // Add the diagram type selector and package buttons to topPanel
//                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
//                JPanel packageButtonPanel = new JPanel();
//
//                packageButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
//                packageButtonPanel.setBackground(new Color(230, 230, 230));
//                packageButtonPanel.setBorder(BorderFactory.createTitledBorder("Toolbar"));
//
//                packageButtonPanel.add(diagramDrawer.getPackageButton());
//                packageButtonPanel.add(diagramDrawer.getImportButton());
//                packageButtonPanel.add(diagramDrawer.getAccessButton());
//
//                topPanel.add(packageButtonPanel, BorderLayout.CENTER);  // Show package buttons
//            }
//
//            // Ensure the layout is refreshed and components are visible
//            topPanel.revalidate();
//            topPanel.repaint();
//            revalidate();
//            repaint();
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(MainFrame::new);
//    }
//}




//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//public class MainFrame extends JFrame {
//    private DiagramEditorPanel diagramEditorPanel;
//    private Toolbar toolbar;
//    private ProjectManagerPanel projectManagerPanel;
//    private JComboBox<String> diagramTypeSelector;
//    private UMLDiagramDrawer diagramDrawer;
//    private String CurrentDiagramType;
//
//    public MainFrame() {
//        setTitle("UML Editor");
//        setSize(1200, 800);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout(10, 10));
//
//        CurrentDiagramType=null;
//        // Initialize Panels
//        diagramEditorPanel = new DiagramEditorPanel();  // Diagram editor panel
//        projectManagerPanel = new ProjectManagerPanel(diagramEditorPanel, diagramDrawer,this);  // Pass diagramDrawer to ProjectManagerPanel
//        diagramDrawer = new UMLDiagramDrawer();  // UML diagram drawer for package diagrams
//
//        // Initialize Toolbar
//        toolbar = new Toolbar(diagramEditorPanel);  // Pass diagramEditorPanel to Toolbar
//
//        // Diagram Type Selector
//        diagramTypeSelector = new JComboBox<>(new String[]{"Class Diagram", "Package Diagram"});
//        diagramTypeSelector.addActionListener(new DiagramTypeChangeListener());
//
//        // Add Components to Frame
//        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.add(diagramTypeSelector, BorderLayout.WEST);
//        topPanel.add(toolbar, BorderLayout.CENTER);  // Add toolbar initially
//
//        add(topPanel, BorderLayout.NORTH);  // Add top panel with toolbar
//        add(projectManagerPanel, BorderLayout.WEST);  // Add project manager panel to the left
//        setVisible(true);
//    }
//    public String getCurrentDiagramType() {
//        return CurrentDiagramType;
//    }
//
//
//    // Listener to Handle Diagram Type Changes
//    private class DiagramTypeChangeListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String selectedDiagram = (String) diagramTypeSelector.getSelectedItem();
//
//            // Get the topPanel (which holds the diagram selector and toolbar/buttons)
//            JPanel topPanel = (JPanel) getContentPane().getComponent(0);
//
//            // Clear topPanel before adding new components
//            topPanel.removeAll();
//
//            if ("Class Diagram".equals(selectedDiagram)) {
//                // Remove package diagram and add class diagram components
//                CurrentDiagramType="Class Diagram";
//                remove(diagramDrawer);
//                add(diagramEditorPanel, BorderLayout.CENTER);
//
//                toolbar.updateToolsForDiagram("Class Diagram");
//
//                // Add the diagram type selector and toolbar back
//                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
//                topPanel.add(toolbar, BorderLayout.CENTER);  // Show class diagram toolbar
//            } else if ("Package Diagram".equals(selectedDiagram)) {
//                CurrentDiagramType="Package Diagram";
//                // Remove class diagram and add package diagram components
//                remove(diagramEditorPanel);
//                //diagramEditorPanel=null;
//                add(diagramDrawer, BorderLayout.CENTER);
//
//                // Show package diagram buttons
//                diagramDrawer.showPackageButtons(true);
//
//                // Add the diagram type selector and package buttons to topPanel
//                topPanel.add(diagramTypeSelector, BorderLayout.WEST);
//                JPanel packageButtonPanel = new JPanel();
//
//                packageButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
//                packageButtonPanel.setBackground(new Color(230, 230, 230));
//                packageButtonPanel.setBorder(BorderFactory.createTitledBorder("Toolbar"));
//
//                packageButtonPanel.add(diagramDrawer.getPackageButton());
//                packageButtonPanel.add(diagramDrawer.getImportButton());
//                packageButtonPanel.add(diagramDrawer.getAccessButton());
//
//                topPanel.add(packageButtonPanel, BorderLayout.CENTER);  // Show package buttons
//            }
//
//            // Ensure the layout is refreshed and components are visible
//            topPanel.revalidate();
//            topPanel.repaint();
//            revalidate();
//            repaint();
//        }
//
//    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(MainFrame::new);
//   }
//}







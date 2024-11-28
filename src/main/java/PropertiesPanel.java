import javax.swing.*;
import java.awt.*;

public class PropertiesPanel extends JPanel {
    public PropertiesPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Properties"));

        // Add scrollable content
        JTextArea propertiesArea = new JTextArea(10, 20); // Adjust dimensions
        JScrollPane scrollPane = new JScrollPane(propertiesArea);

        add(scrollPane, BorderLayout.CENTER);
    }
}

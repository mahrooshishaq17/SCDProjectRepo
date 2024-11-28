import javax.swing.*;
import java.awt.*;

public class ProjectManagerPanel extends JPanel {
    public ProjectManagerPanel() {
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
    }
}

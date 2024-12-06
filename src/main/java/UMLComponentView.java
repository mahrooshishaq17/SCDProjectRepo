import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class UMLComponentView extends JPanel {
    private String type;

    public UMLComponentView(String type) {
        this.type = type;
        setBackground(type.equals("Class") ? Color.CYAN : Color.ORANGE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel label = new JLabel(type);
        add(label);
    }

    UMLComponentView() {
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class DiagramEditorPanel extends JPanel {
    private List<AssociationLine> associationLines = new ArrayList<>();
    private List<DescriptionLabel> descriptionLabels = new ArrayList<>();// List to store DescriptionLabels
    private List<InheritanceLine> inheritanceLines = new ArrayList<>();
    private List<AggregationLine> AggregationLines=new ArrayList<>();
    private List<CompositionLine> CompositionLines=new ArrayList<>();
    private List<DirectedAssociation> DirectedAssociations=new ArrayList<>();



    public DiagramEditorPanel() {
        setBackground(Color.WHITE); // Set background color
        setBorder(BorderFactory.createTitledBorder("Class Diagram Editor")); // Add border with title
        setLayout(null); // Assuming free placement of components for drawing
    }

    public List<AssociationLine> getAssociationLines() {
        return associationLines;
    }
    public List<AggregationLine> getAggregationLines() {
        return AggregationLines;
    }
    public List<CompositionLine> getCompositionLines() {
        return CompositionLines;
    }
    public List<DirectedAssociation> getDirectedAssociations()
    {
        return DirectedAssociations;
    }public void removeAssociationLine(AssociationLine line) {
        associationLines.remove(line);  // Remove the association line from the list
        repaint();  // Repaint the panel to reflect the changes
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw all association lines
        for (AssociationLine line : associationLines) {
            line.draw(g);  // Draw each line
        }
//        for (DescriptionLabel label : descriptionLabels) {
//            label.paint(g);
//        }
        for (InheritanceLine line : inheritanceLines) {
            line.draw(g);
        }
        for (AggregationLine line : AggregationLines) {
            line.draw(g);
        }
        for (CompositionLine line : CompositionLines) {
            line.draw(g);
        }
        for (DirectedAssociation line : DirectedAssociations) {
            line.draw(g);
        }
    }

    public void add(AssociationLine line) {
        associationLines.add(line);
        repaint();
    }
    public void add(DescriptionLabel label) {
       descriptionLabels.add(label);  // Add the label to the list
        super.add(label);
        label.setVisible(true);
        revalidate();
        repaint();
    }
    public List<InheritanceLine> getInheritanceLines() {
        return inheritanceLines;
    }

    public void add(InheritanceLine line) {
        inheritanceLines.add(line);
        repaint();
    }

    public void removeInheritanceLine(InheritanceLine line) {
        inheritanceLines.remove(line);
        repaint();
    }
    public void add(AggregationLine line) {
        AggregationLines.add(line);
        repaint();
    }

    public void removeAggregationLine(AggregationLine line) {
        AggregationLines.remove(line);
        repaint();
    }
    public void add(CompositionLine line) {
        CompositionLines.add(line);
        repaint();
    }

    public void removeCompositionLine(CompositionLine line) {
        CompositionLines.remove(line);
        repaint();
    }

    public void add(DirectedAssociation line) {
        DirectedAssociations.add(line);
        repaint();
    }

    public void removeDirectedAssociation(DirectedAssociation line) {
        DirectedAssociations.remove(line);
        repaint();
    }



    public BufferedImage exportToImage() {
        int width = this.getWidth();
        int height = this.getHeight();


        // Create a new BufferedImage with the width and height of the panel
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Get the graphics object of the image
        Graphics2D g2d = image.createGraphics();

        // Call the paintComponent method to paint the content onto the image
        this.paint(g2d);  // The paint method of JPanel is responsible for rendering the content
        g2d.dispose();    // Dispose of the graphics object to release resources

        return image;     // Return the generated image
    }

    public void clear() {
        associationLines.clear();
        inheritanceLines.clear();
        AggregationLines.clear();
        CompositionLines.clear();
        descriptionLabels.clear();
        removeAll();
        // Optionally reset other states (e.g., zoom level, selection)
        revalidate();
        repaint();
    }
    public void clearAll() {
        associationLines.clear();
        inheritanceLines.clear();
        AggregationLines.clear();
        CompositionLines.clear();
        DirectedAssociations.clear();
        descriptionLabels.clear();
        removeAll();
        // Optionally reset other states (e.g., zoom level, selection)
        revalidate();
        repaint();
    }
}

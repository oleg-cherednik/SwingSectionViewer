package cop.swing.controls.sections;

import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

/**
 * {@link Section} it's a simple one part (item or line) in {@link SectionViewer}. It containes at least one part
 * (<b>main part</b>). This part created externally and contains main business part. Additionally it's possible to
 * create other parts, such as control part (with some buttons, e.g. add, delete) or position part to show position
 * of the current section within {@link SectionViewer}.
 *
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public abstract class Section extends JPanel {
    private static final Composite ALPHA = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);

    private final Rectangle2D.Double rect = new Rectangle2D.Double();

    private Border border;
    private boolean selected;
    private Dimension dim = getPreferredSize();
    private Image image;

    protected SectionViewer<? extends Section> viewer;

    protected Section() {
        border = getBorder();
    }

    public final void setViewer(SectionViewer<? extends Section> viewer) {
        this.viewer = viewer;
    }

    public final SectionViewer<? extends Section> getViewer() {
        return viewer;
    }

    public void update() {
        if (viewer == null)
            return;

        setBackground(viewer.getSectionBackground(this));
        revalidate();
    }

    void setSelected(boolean selected) {
        if (this.selected == selected)
            return;

        this.selected = selected;
//        super.setBorder(selected ? null : border);
        image = selected ? SectionViewer.createImage(this, ALPHA) : null;

//        for (Component component : getComponents())
//            component.setVisible(!selected);
    }

    // ========== Component ==========

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);

        for (Component component : getComponents())
            component.setBackground(color);
    }

    @Override
    public Dimension getPreferredSize() {
        return selected ? dim : (dim = super.getPreferredSize());
    }

    @Override
    public void paint(Graphics g) {
        if (viewer == null)
            return;

        super.paint(g);
//        setBackground(selected ? Color.magenta : Color.gray);

        if (!selected)
            return;

        rect.width = getWidth() - 1;
        rect.height = getHeight() - 1;

        if (viewer.isDragModeOn()) {
            rect.width = getWidth() - 1;
            rect.height = getHeight() - 1;

            g.setColor(getBackground());
            g.fillRect(0, 0, (int)rect.width, (int)rect.height);
            g.setColor(SectionViewer.SELECTION_COLOR);
            ((Graphics2D)g).draw(rect);
        }
//        else if (image != null)
//            g.drawImage(image, 0, 0, null);

//        viewer.selectedBorder.paintBorder(this, g, 0, 0, (int)rect.width, (int)rect.height);
    }

    // ========== JComponent ==========

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
        this.border = border;
    }
}

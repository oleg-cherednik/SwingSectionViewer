package cop.swing.demo;

import cop.swing.controls.sections.Section;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSection extends Section<LocalSection.MainPart> {
    private final int id;
    private final MainPart delegate;

    public LocalSection(int id) {
        this.id = id;
        delegate = new MainPart(id);
    }

    // ========== Section ==========

    @Override
    public MainPart getDelegate() {
        return delegate;
    }

    // ========== Object ==========

    @Override
    public String toString() {
        return "Section: #" + id;
    }

    // ========== Main Part ==========

    public static class MainPart extends JPanel {
        private static final long serialVersionUID = -3900609325808062356L;

        public MainPart(int id) {
            setLayout(new BorderLayout(0, 0));
            add(new JLabel("Section: " + id), BorderLayout.CENTER);
            setBorder(BorderFactory.createEtchedBorder());
        }

        public Dimension getPreferredSize() {
            return super.getPreferredSize();
        }

    }
}

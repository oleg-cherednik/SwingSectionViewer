package cop.swing.demo;

import cop.swing.controls.sections.Section;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.BorderLayout;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSection extends Section {
    private final int id;

    public LocalSection(int id) {
        this.id = id;
        setLayout(new BorderLayout(0, 0));
        add(new JLabel("Section: " + id), BorderLayout.CENTER);
        setBorder(BorderFactory.createEtchedBorder());
    }

    // ========== Object ==========

    @Override
    public String toString() {
        return "Section: #" + id;
    }
}

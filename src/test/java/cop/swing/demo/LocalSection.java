package cop.swing.demo;

import cop.swing.controls.sections.Section;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSection extends Section {
    private int id;
    private final JLabel labelId;

    public LocalSection(int id) {
        this.id = id;
        labelId = new JLabel(String.format("<html><font color=\"red\">%d</font></html>", id));
        init();
    }

    private void init() {
        JLabel title = new JLabel(String.format("<html>Rule for section %d</b></html>", id));
        JLabel price = new JLabel(String.format("<html><font color=\"blue\">price:</font> $%d</html>", id));
        JCheckBox active = new JCheckBox();

        active.setSelected(id % 2 != 0);
//        active.setEnabled(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        labelId.setPreferredSize(new Dimension(30, 0));
        labelId.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(labelId, gbc);

        gbc.gridheight = 1;
        add(title, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        add(Box.createHorizontalGlue(), gbc);

        gbc.weightx = 0;
        gbc.gridwidth = 1;
        add(price, gbc);
        add(new JLabel("<html><font color=\"blue\">active:</font></html>"), gbc);
        add(active, gbc);

//        BorderFactory.createLineBorder(SELECTION_COLOR, 4)
//        setBorder(BorderFactory.createLineBorder(Color.red, 4));//createEmptyBorder(4, 4, 4, 4));
    }

    // ========== Object ==========

    @Override
    public String toString() {
        return "Section: #" + id;
    }
}

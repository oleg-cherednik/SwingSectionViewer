package cop.swing.demo;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public class SectionViewerDemo extends JDialog implements ActionListener {
    private final JButton button1 = new JButton("Button 1");
    private final JButton button2 = new JButton("Button 2");
//    private final LayoutOrganizerPanel panel = new LayoutOrganizerPanel();
    private final JPanel section1  = new LocalSection.MainPart(1);
    private final JPanel section2 = new LocalSection.MainPart(2);
    private final LocalSectionViewer sectionViewer = new LocalSectionViewer();

    public SectionViewerDemo() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(5, 5));

        add(button1, BorderLayout.NORTH);
        add(sectionViewer, BorderLayout.CENTER);
        add(button2, BorderLayout.SOUTH);

//        panel.setBackground(Color.yellow);
//        panel.addComp(section1, section2);

        section1.setBackground(Color.blue);
        section1.setBackground(Color.cyan);

//        add(sectionViewer, gbc);

        sectionViewer.addNewSection();
        sectionViewer.addNewSection();
        sectionViewer.addNewSection();

        sectionViewer.getPreferredSize();


        setSize(800, 500);
//        pack();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    // ========== ActionListener ==========

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    // ========== static ==========

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SectionViewerDemo().setVisible(true);
            }
        });
    }
}

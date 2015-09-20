package cop.swing.controls.scroll;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import java.awt.Component;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public class JScrollPaneExt extends JScrollPane {
    public JScrollPaneExt() {
    }

    public JScrollPaneExt(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
    }

    public JScrollPaneExt(Component view) {
        super(view);
    }

    public JScrollPaneExt(int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
    }

    public void setBlockAutoScroll(boolean blockAutoScroll) {
        if (viewport instanceof JViewportExt)
            ((JViewportExt)viewport).setBlockAutoScroll(blockAutoScroll);
    }

    // ========== JScrollPane ==========

    @Override
    protected JViewport createViewport() {
        return new JViewportExt(true);
    }

    @Override
    public void setViewport(JViewport viewport) {
        boolean blockAutoScroll = viewport instanceof JViewportExt && ((JViewportExt)viewport).isBlockAutoScroll();

        super.setViewport(viewport);

        if (viewport instanceof JViewportExt)
            ((JViewportExt)viewport).setBlockAutoScroll(blockAutoScroll);
    }

    @Override
    public JScrollBar createVerticalScrollBar() {
        return new ScrollBar(JScrollBar.VERTICAL);
    }
}

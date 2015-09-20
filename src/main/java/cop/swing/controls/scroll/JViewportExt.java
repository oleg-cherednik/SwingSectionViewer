package cop.swing.controls.scroll;

import cop.swing.controls.sections.SectionViewer;

import javax.swing.JViewport;
import java.awt.Point;

/**
 * This {@link JViewport} can block scrolling after view chantes its size (default behaviour is to scroll viewer to the
 * bottom). E.g. if {@link #blockAutoScroll} is set to <t>true</t>, then only manual scrolling is available (from GUI
 * by moveing scroll bars and inviking special methods on {@link JViewport}). By default this block is on.
 *
 * @author Oleg Cherednik
 * @since 09.01.2013
 */
final class JViewportExt extends JViewport {
    private static final String SECTION_VIEWER = SectionViewer.class.getName();
    private static final String JVIEWPORT = JViewport.class.getName();

    private boolean blockAutoScroll;

    public JViewportExt() {
        this(true);
    }

    public JViewportExt(boolean blockAutoScroll) {
        this.blockAutoScroll = blockAutoScroll;
    }

    public void setBlockAutoScroll(boolean blockAutoScroll) {
        this.blockAutoScroll = blockAutoScroll;
    }

    public boolean isBlockAutoScroll() {
        return blockAutoScroll;
    }

    // ========== JViewport ==========

    @Override
    public void setViewPosition(Point point) {
        if (blockAutoScroll) {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();

            if (SECTION_VIEWER.equalsIgnoreCase(stack[3].getClassName()) || !JVIEWPORT.equalsIgnoreCase(
                    stack[2].getClassName()) || !"scrollRectToVisible".equalsIgnoreCase(stack[2].getMethodName()))
                super.setViewPosition(point);
        } else
            super.setViewPosition(point);
    }
}

package cop.swing.controls.sections;

import cop.swing.controls.layouts.LayoutNode;
import cop.swing.controls.layouts.LayoutOrganizer;
import cop.swing.panels.LayoutOrganizerPanel;
import cop.swing.providers.ColorProvider;
import cop.swing.providers.ParentColorProvider;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;

import static java.awt.AWTEvent.KEY_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_EVENT_MASK;
import static java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK;
import static javax.swing.SwingUtilities.convertPointFromScreen;
import static javax.swing.SwingUtilities.convertPointToScreen;
import static javax.swing.SwingUtilities.isDescendingFrom;
import static javax.swing.SwingUtilities.isLeftMouseButton;

/**
 * This viewer can contains lots of sections, that can be quite complicated in general. In this situation we have
 * performance problem with GUI.
 *
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public abstract class SectionViewer<S extends Section> extends JScrollPane implements AWTEventListener {
    static final long EVENT_MASK = MOUSE_MOTION_EVENT_MASK | MOUSE_EVENT_MASK | KEY_EVENT_MASK;
    private static final Composite ALPHA_HALF = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);

    public static final Color SELECTION_COLOR = Color.red;

    public final Border selectedBorder = BorderFactory.createLineBorder(SELECTION_COLOR, 2);

    protected final SectionContainer<S> sections;
    protected final LayoutOrganizerPanel panel;

    //    private final Point point = new Point();
    private final Rectangle bounds = new Rectangle();
    private final Point delta = new Point();    // delta between draggable region start and mouse position
    // mouse point, top left panel's corner is [0;0]
    private final Point eventPoint = new Point();    // event or mouse point in drag mode (this is base point)
    private final ColorProvider sectionBackgroundColorProvider;

    private int pos = -1;
    private boolean draggable;
    private S selectedSection;
    private S prvSelectedSection;
    private boolean dragModeOn;    // true means that drag mode is currently turned on
    private Image dragImage;    // this image is shown under cursor in drag mode

    protected SectionViewer() {
        this(null);
    }

    protected SectionViewer(LayoutOrganizer layoutOrganizer) {
        setViewportView(panel = new LayoutOrganizerPanel(layoutOrganizer));
        sections = new SectionContainer<S>(this);
        sectionBackgroundColorProvider = getBackgroundColorProvider();
    }

    public void setLayoutOrganizer(LayoutOrganizer layoutOrganizer) {
        panel.setLayoutOrganizer(layoutOrganizer);
    }

    protected ColorProvider getBackgroundColorProvider() {
        return new ParentColorProvider(this);
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
        updateListener();
    }

    private void updateListener() {
        getToolkit().removeAWTEventListener(this);
//        scrollPane.getViewport().removeChangeListener(sections);

//        mainPanelDecorator.addComponentListener(sections);

        if (draggable)
            getToolkit().addAWTEventListener(this, EVENT_MASK);

//        scrollPane.getViewport().addChangeListener(sections);
    }

    boolean isDragModeOn() {
        return dragModeOn;
    }

    public final List<S> getSections() {
        return sections.getSections();
    }

    public final boolean isEmpty() {
        return sections.isEmpty();
    }

    public final int getSectionsAmount() {
        return sections.size();
    }

    public final int getFirstVisibleSection() {
        return sections.getPosition(sections.getSections().get(0));
    }

    public final void setSectionsBackground(Color color) {
        sections.setBackground(color);
    }

    public void addSection(S section) {
        if (section == null)
            return;

        sections.add(section);
        panel.addComp(section);
        update();

    }

    public void addSections(Collection<S> sections) {
        if (CollectionUtils.isEmpty(sections))
            return;

        this.sections.addAll(sections);

        panel.setComp(this.sections.getSections());
        panel.addComp(panel.getLayoutOrganizer().modifyNode(LayoutNode.GLUE).create());

        update();
    }

    public final Color getSectionBackground(Section section) {
        int pos = sections.getPosition(section);
        int total = sections.size();
        return sectionBackgroundColorProvider.getBackground(pos, total);
    }

    public void removeSection(S section) {
        if (sections.getPosition(section) != -1) {
            sections.remove(section);
            panel.remove(section);
            update();
        }
    }

    public void update() {
        if (sections != null)
            for (Section section : sections.getSections())
                section.update();

        revalidate();
        repaint();
    }

    public final void setFirstVisiblePosition(int pos) {
        S section = sections.get(pos);

        if (section == null/* || scrollPane.getViewport() == null*/)
            return;

        section.getBounds(bounds);
//        point.x = bounds.x;
//        point.y = bounds.y;

//        JViewport viewport = scrollPane.getViewport();
//        point.y = Math.min(viewport.getViewSize().height - viewport.getVisibleRect().height, point.y);
//        viewport.setViewPosition(point);
    }

    private boolean isEventEnabled(AWTEvent event) {
        if (event.getSource() instanceof Component && !((Component)event.getSource()).isShowing())
            return false;
        if (event instanceof ComponentEvent)
            if (!(event instanceof KeyEvent) && !isDescendingFrom(((ComponentEvent)event).getComponent(), this))
                return false;
        return isShowing() && draggable;
    }

    protected void onMouseEvent(MouseEvent event) {
        updateEventPoint(event);

        if (event.getID() == MouseEvent.MOUSE_PRESSED)
            mousePressed(event);
        else if (event.getID() == MouseEvent.MOUSE_RELEASED)
            mouseReleased(event);
        else if (event.getID() == MouseEvent.MOUSE_MOVED)
            mouseMoved(event);
        else if (event.getID() == MouseEvent.MOUSE_DRAGGED)
            mouseDragged(event);
    }

//    private static MouseEvent updateEventPoint(MouseEvent event) {
//        return new MouseEvent((Component)event.getSource(), event.getID(), event.getWhen(), event.getModifiers(), event.getX(), event.getY(),
//                event.getClickCount(), event.isPopupTrigger(), event.getButton());
//    }

    protected void onKeyEvent(KeyEvent event) {
        selectUnderMouseSection(event.isControlDown());

        if (!event.isControlDown())
            setDragMode(false);
    }

    protected void selectUnderMouseSection(boolean ctrl) {
        if (ctrl) {
            if (selectedSection == prvSelectedSection)
                return;
            if (prvSelectedSection != null)
                prvSelectedSection.setSelected(false);
            if (selectedSection != null)
                selectedSection.setSelected(true);
            prvSelectedSection = selectedSection;
        } else {
            if (prvSelectedSection != null)
                prvSelectedSection.setSelected(false);
            prvSelectedSection = null;
        }
    }

    /**
     * Locates the visible section that contains the specified position.
     *
     * @param point position
     * @return {@link Section} object or {@code null}
     */
    private S getSectionAt(Point point) {
        if (selectedSection != null && selectedSection.getBounds(bounds).isEmpty())
            return selectedSection;

        Component comp = panel.findComponentAt(point.x, point.y);
        return comp instanceof Section ? (S)comp : null;
    }

    /**
     * Before turn drag mode on in <t>eventPoint</t> property current mouse positin should be sored,
     * <t>uderMouseSection</t> should be not null and contain section under cursor (this section is currentyl dragged)
     *
     * @param on <t>true</t> to turn drag mode on
     */

    private void setDragMode(boolean on) {
        if (!draggable || dragModeOn == on)
            return;

        if (on) {
            selectedSection.setBackground(SELECTION_COLOR);
            dragImage = createImage(selectedSection, ALPHA_HALF);
            delta.x = eventPoint.x - bounds.x;
            delta.y = eventPoint.y - bounds.y;
            pos = sections.getPosition(selectedSection);
        } else
            dragImage = null;

        dragModeOn = on;

        update();
        repaint();
    }

    public void clear() {
        sections.clear();
        panel.removeAll();
    }

    private void updateEventPoint(MouseEvent event) {
        eventPoint.x = event.getX();
        eventPoint.y = event.getY();
        convertPoint((Component)event.getSource(), eventPoint, panel);
    }

    // ========== AWTEventListener ==========

    @Override
    public void eventDispatched(AWTEvent event) {
        if (!isEventEnabled(event))
            return;
        if (event instanceof MouseEvent)
            onMouseEvent((MouseEvent)event);
        else if (event instanceof KeyEvent)
            onKeyEvent((KeyEvent)event);
    }

    // ========== MouseListener ==========

    protected void mousePressed(MouseEvent event) {
        if (selectedSection == null || prvSelectedSection == null)
            return;
        if (event.isControlDown() && isLeftMouseButton(event))
            setDragMode(true);
    }

    protected void mouseReleased(MouseEvent event) {
        setDragMode(false);
    }

    // ========== MouseMotionListener ==========

    protected void mouseMoved(MouseEvent event) {
        selectedSection = getSectionAt(eventPoint);
        selectUnderMouseSection(event.isControlDown());
    }

    protected void mouseDragged(MouseEvent event) {
        if (!dragModeOn)
            return;

        int pos = sections.getPosition(getSectionAt(eventPoint));

        if (pos != this.pos && pos >= 0) {
            sections.move(selectedSection, this.pos = pos);
            panel.setComp(sections.getSections());
        }

        update();
        repaint();
    }

    private int getComponentPosition(S section) {
        int pos = -1;

        for (Component component : panel.getComponents()) {
            if (!(component instanceof Section))
                pos++;
            if (component == section)
                return pos;
        }

        return -1;
    }

    // ========== JComponent ==========

    @Override
    public void updateUI() {
        super.updateUI();

        if (panel != null)
            panel.updateUI();
    }

    // ========== Component ==========

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        for (S section : sections.getSections())
            section.setVisible(visible);

        updateListener();
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);

        if (panel != null)
            panel.setBackground(color);

        update();
        repaint();
//        for (Component component : getComponents())
//            component.setBackground(color);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (!dragModeOn)
            return;

        int x = (int)eventPoint.getX() - delta.x;
        int y = (int)eventPoint.getY() - delta.y;

        if (dragImage != null)
            g.drawImage(dragImage, x, y, null);
        else {
            // TODO it seems that it's useless, I don't see green color
            Color color = g.getColor();
            g.setColor(Color.green);
            int width = selectedSection.getWidth();
            int height = selectedSection.getHeight();
            g.fillRect(x, y, width, height);
            g.setColor(color);
        }
    }

    // ========== Container ==========

    /** Do use {@link #removeSection(Section)} instead */
    @Override
    @Deprecated
    public void remove(Component comp) {
        super.remove(comp);
    }

    // ========== static ==========

    /**
     * Convert a <code>point</code> in <code>src</code> coordinate system to <code>dest</code> coordinate system.
     * Basically this method do the same thing as {@link SwingUtilities#convertPoint(Component, Point, Component)}
     * except creating new {@link Point} object instead of using given one.
     *
     * @param src   source coordinate system
     * @param point point
     * @param dest  destination coordinate system
     * @return converted point
     * @see {@link SwingUtilities#convertPoint(Component, Point, Component)}
     */
    protected static Point convertPoint(Component src, Point point, Component dest) {
        convertPointToScreen(point, src);
        convertPointFromScreen(point, dest);
        return point;
    }

    protected static Rectangle convertRect(Component src, Rectangle rect, Component dest, Point point) {
        point.x = rect.x;
        point.y = rect.y;

        convertPoint(src, point, dest);

        rect.x = point.x;
        rect.y = point.y;

        return rect;
    }

    protected static Image createImage(Component component, Composite composite) {
        if (component == null)
            return null;

        int width = component.getWidth() - 1;
        int height = component.getHeight() - 1;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setComposite(composite);
        component.paintAll(g);

        return image;
    }
}

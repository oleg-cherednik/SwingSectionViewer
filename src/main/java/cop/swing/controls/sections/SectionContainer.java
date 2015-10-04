package cop.swing.controls.sections;

import org.apache.commons.collections.CollectionUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
final class SectionContainer<S extends Section> implements ComponentListener, ChangeListener {
    private final Rectangle cmpBounds = new Rectangle();
    private final Rectangle bounds = new Rectangle();
    private final Point point = new Point();

    private final List<S> sections = new LinkedList<S>();
    private final SectionViewer<S> viewer;

    public SectionContainer(SectionViewer<S> viewer) {
        this.viewer = viewer;
    }

    public int size() {
        return sections.size();
    }

    public List<S> getSections() {
        return sections.isEmpty() ? Collections.<S>emptyList() : Collections.unmodifiableList(sections);
    }

    public void move(S section, int index) {
        if (!sections.contains(section))
            return;

        SectionViewer<? extends Section> viewer = section.getViewer();

        try {
            sections.remove(section);
            sections.add(index, section);
            section.setViewer(this.viewer);
        } catch(Exception ignored) {
            section.setViewer(viewer);
        }
    }

    /**
     * Returns current position of given section. If section is not found in current container or it's {@code null}, then -1 returns.
     *
     * @param section existed section
     * @return given section position for current container
     */
    public int getPosition(S section) {
        return section != null ? sections.indexOf(section) : -1;
    }

    public void add(S section) {
        section.setViewer(viewer);
        sections.add(section);
    }

    public void add(int index, S section) {
        SectionViewer<? extends Section> viewer = section.getViewer();

        try {
            sections.add(index, section);
            section.setViewer(this.viewer);
        } catch(Exception ignored) {
            section.setViewer(viewer);
        }
    }

    public void addAll(Collection<S> sections) {
        if (CollectionUtils.isNotEmpty(sections))
            for (S section : sections)
                add(section);
    }

    public boolean contains(Section section) {
        if (section != null)
            for (Section sec : sections)
                if (sec == section)
                    return true;

        return false;
    }

    public S get(int index) {
        return sections.size() > index ? sections.get(index) : null;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean remove(S section) {
        return sections.remove(section);
    }

    public void setBackground(Color color) {
        for (S section : sections)
            section.setBackground(color);
    }

    public void clear() {
        sections.clear();
    }

//    private void updateVisibleSections(Component comp) {
//        comp.getBounds(cmpBounds);
//
//        for (S section : sections) {
//            SectionViewer.convertRect(section.getParent(), section.getBounds(bounds), comp, point);
//
//            if (bounds.intersects(cmpBounds))
//                visibleSections.add(section);
//            else if (!visibleSections.isEmpty())
//                break;
//        }
//    }

    // ========== ComponentListener ==========

    @Override
    public void componentResized(ComponentEvent event) {
        Component comp = event.getComponent();

//        if (!(comp instanceof JScrollPane))
//            updateVisibleSections(comp);
    }

    @Override
    public void componentMoved(ComponentEvent event) {
    }

    @Override
    public void componentShown(ComponentEvent event) {
    }

    @Override
    public void componentHidden(ComponentEvent event) {
    }

    // ========== ChangeListener ==========

    @Override
    public void stateChanged(ChangeEvent event) {
//        updateVisibleSections((Component)event.getSource());
    }
}

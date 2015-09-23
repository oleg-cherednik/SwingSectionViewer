package cop.swing.controls.sections;

import org.apache.commons.collections.CollectionUtils;

import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
final class SectionContainer<T extends Component, S extends Section<T>> implements ComponentListener, ChangeListener {
    public static final int UNLIMITED = 0;

    private final Rectangle cmpBounds = new Rectangle();
    private final Rectangle bounds = new Rectangle();
    private final Point point = new Point();

    private final List<S> sections = new ArrayList<S>();
    private final List<S> visibleSections = new ArrayList<S>();
    private final SectionViewer<T, S> viewer;
    private final int maxSections;

    public SectionContainer(SectionViewer<T, S> viewer, int maxSections) {
        this.maxSections = maxSections > UNLIMITED ? maxSections : Integer.MAX_VALUE;
        this.viewer = viewer;
    }

    public int size() {
        return sections.size();
    }

    public List<S> getSections() {
        return sections.isEmpty() ? Collections.<S>emptyList() : Collections.unmodifiableList(sections);
    }

    public List<S> getVisibleSections() {
        return visibleSections.isEmpty() ? getSections() : Collections.unmodifiableList(visibleSections);
    }

    public int getSectionPosition(S section) {
        return sections.indexOf(section);
    }

    public void move(int index, S section) {
        if (!sections.contains(section))
            return;

        SectionViewer<T, Section<T>> viewer = section.getViewer();

        try {
            sections.remove(section);
            sections.add(index, section);
            section.setViewer((SectionViewer<T, Section<T>>)this.viewer);
        } catch(Exception ignored) {
            section.setViewer(viewer);
        }
    }

    public int getMaxSections() {
        return maxSections;
    }

    public void add(S section) {
        if (sections.size() >= maxSections)
            return;

        section.setViewer((SectionViewer<T, Section<T>>)viewer);
        sections.add(section);
    }

    public void add(int index, S section) {
        if (sections.size() >= maxSections)
            return;

        SectionViewer<T, Section<T>> viewer = section.getViewer();

        try {
            sections.add(index, section);
            section.setViewer((SectionViewer<T, Section<T>>)this.viewer);
        } catch(Exception ignored) {
            section.setViewer(viewer);
        }
    }

    public void addAll(Collection<S> sections) {
        if (CollectionUtils.isNotEmpty(sections))
            for (S section : sections)
                add(section);
    }

    public S get(int index) {
        return sections.size() > index ? sections.get(index) : null;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public boolean isFull() {
        return sections.size() >= maxSections;
    }

    public boolean remove(S section) {
        boolean res = sections.remove(section);
        visibleSections.remove(section);
        return res;
    }

    public void setBackground(Color color) {
        for (S section : sections)
            section.setBackground(color);
    }

    public void clear() {
        sections.clear();
        visibleSections.clear();
    }

    private void updateVisibleSections(Component comp) {
        comp.getBounds(cmpBounds);
        visibleSections.clear();

        for (S section : sections) {
            SectionViewer.convertRect(section.getParent(), section.getBounds(bounds), comp, point);

            if (bounds.intersects(cmpBounds))
                visibleSections.add(section);
            else if (!visibleSections.isEmpty())
                break;
        }
    }

    // ========== ComponentListener ==========

    public void componentResized(ComponentEvent event) {
        Component comp = event.getComponent();

        if (!(comp instanceof JScrollPane))
            updateVisibleSections(comp);
    }

    public void componentMoved(ComponentEvent event) {
    }

    public void componentShown(ComponentEvent event) {
    }

    public void componentHidden(ComponentEvent event) {
    }

    // ========== ChangeListener ==========

    public void stateChanged(ChangeEvent event) {
        updateVisibleSections((Component)event.getSource());
    }
}

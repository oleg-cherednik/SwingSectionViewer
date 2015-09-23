package cop.swing.demo;

import cop.swing.controls.sections.SectionViewer;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSectionViewer extends SectionViewer<LocalSection.MainPart, LocalSection> {
    private int counter;

    protected LocalSectionViewer() {
        super(5, UNLIMITED);
        createMainLayout();
        activate();
        setDraggable(true);
    }
    // ========== CreateFactory ==========

    @Override
    public LocalSection create() {
        return new LocalSection(counter++);
    }
}

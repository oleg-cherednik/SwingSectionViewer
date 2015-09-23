package cop.swing.demo;

import cop.swing.controls.sections.SectionViewer;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSectionViewer extends SectionViewer<LocalSection.MainPart, LocalSection> {
    protected LocalSectionViewer() {
        super(5, UNLIMITED);
        createMainLayout();
        setDraggable(true);
    }
}

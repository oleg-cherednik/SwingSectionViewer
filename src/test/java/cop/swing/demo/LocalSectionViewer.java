package cop.swing.demo;

import cop.swing.controls.layouts.SingleColumnLayout;
import cop.swing.controls.layouts.SingleRowLayout;
import cop.swing.controls.sections.SectionViewer;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSectionViewer extends SectionViewer<LocalSection.MainPart, LocalSection> {
    protected LocalSectionViewer() {
        super(UNLIMITED);
        createMainLayout();
        setDraggable(true);
    }

    public void setSpace(int space) {
        if (panel.getLayoutOrganizer() instanceof SingleColumnLayout)
            ((SingleColumnLayout)panel.getLayoutOrganizer()).setSpace(space);
        else if (panel.getLayoutOrganizer() instanceof SingleRowLayout)
            ((SingleRowLayout)panel.getLayoutOrganizer()).setSpace(space);

        updateUI();
    }

    public void setAlignment(int alignment) {
        if (panel.getLayoutOrganizer() instanceof SingleColumnLayout)
            ((SingleColumnLayout)panel.getLayoutOrganizer()).setAlignment(alignment);
        else if (panel.getLayoutOrganizer() instanceof SingleRowLayout)
            ((SingleRowLayout)panel.getLayoutOrganizer()).setAlignment(alignment);

        updateUI();
    }
}

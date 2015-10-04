package cop.swing.demo;

import cop.swing.controls.sections.SectionViewer;
import cop.swing.providers.ColorProvider;
import cop.swing.providers.GroupColorProvider;

import java.awt.Color;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
class LocalSectionViewer extends SectionViewer<LocalSection> {
    protected LocalSectionViewer() {
        setDraggable(true);
    }

    // ========== SectionViewer ==========

    @Override
    protected ColorProvider getBackgroundColorProvider() {
        return new GroupColorProvider(Color.lightGray, Color.gray);
    }
}

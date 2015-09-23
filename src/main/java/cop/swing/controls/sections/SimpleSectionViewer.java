package cop.swing.controls.sections;

import java.awt.Component;

/**
 * Default implementation of the {@link SectionViewer}. It contains only one part - <b>main part</b>.
 *
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public abstract class SimpleSectionViewer<T extends Component, S extends Section<T>> extends SectionViewer<T, S> {
    protected SimpleSectionViewer(int space, int maxSections) {
        super(space, maxSections);
    }

    @Override
    public S create() {
        return null;
    }
}

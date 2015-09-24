package cop.swing.controls.sections;

/**
 * Default implementation of the {@link SectionViewer}. It contains only one part - <b>main part</b>.
 *
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public abstract class SimpleSectionViewer<S extends Section> extends SectionViewer<S> {
    protected SimpleSectionViewer(int maxSections) {
        super(maxSections);
    }
}

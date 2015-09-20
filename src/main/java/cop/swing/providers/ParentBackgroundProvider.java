package cop.swing.providers;

import java.awt.Color;
import java.awt.Component;

/**
 * This provider returns parent's background color.
 *
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public class ParentBackgroundProvider implements BackgroundProvider {
    private final Component component;

    public ParentBackgroundProvider(Component component) {
        this.component = component;
    }

    // ========== BackgroundProvider ==========

    @Override
    public Color getBackground(int pos, int total) {
        return component.getBackground();
    }
}

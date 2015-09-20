package cop.swing.providers;

import java.awt.Color;

/**
 * @author Oleg Cherednik
 * @since 18.07.2015
 */
public interface BackgroundProvider {
    Color getBackground(int pos, int total);
}

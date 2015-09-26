package cop.swing.providers;

import org.apache.commons.lang3.ArrayUtils;

import java.awt.Color;

/**
 * @author Oleg Cherednik
 * @since 26.09.2015
 */
public class GroupColorProvider implements ColorProvider {
    private final Color[] colors;

    public GroupColorProvider(Color... colors) {
        if (ArrayUtils.isEmpty(colors))
            throw new IllegalArgumentException("colors is empty");

        this.colors = colors;
    }

    // ========== BackgroundProvider ==========

    @Override
    public Color getBackground(int pos, int total) {
        return colors[pos % colors.length];
    }
}

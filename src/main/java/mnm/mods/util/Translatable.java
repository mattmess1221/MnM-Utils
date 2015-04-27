package mnm.mods.util;

import net.minecraft.client.resources.I18n;

/**
 * Translatable strings. Translations are done via
 * {@link I18n#format(String, Object...)}.
 *
 * @author Matthew
 */
public interface Translatable {

    /**
     * Gets the unlocalized string for this translation
     *
     * @return The untranslated string
     */
    String getUnlocalized();

    /**
     * Translates this string. Should be implemented by doing this.
     *
     * <pre>
     * &#064;Override
     * public String translate(Object... params) {
     *     return I18n.format(getUnlocalized(), params);
     * }
     *
     * </pre>
     *
     * @param params Translation parameters
     * @return The translated string
     */
    String translate(Object... params); // TODO default method
}

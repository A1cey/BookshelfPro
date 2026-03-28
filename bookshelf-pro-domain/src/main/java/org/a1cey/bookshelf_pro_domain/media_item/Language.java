package org.a1cey.bookshelf_pro_domain.media_item;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Locale;
import java.util.Set;

@ValueObject
public record Language(String isoCode) {

    // ISO 639-1 codes as provided by the JVM
    private static final Set<String> ISO_LANGUAGES = Set.of(Locale.getISOLanguages());

    public Language {
        isoCode = isoCode.toLowerCase();

        if (!ISO_LANGUAGES.contains(isoCode)) {
            throw new IllegalArgumentException("Invalid ISO language code: " + isoCode);
        }
    }

    public static Language of(Locale locale) {
        return new Language(locale.getLanguage());
    }

}
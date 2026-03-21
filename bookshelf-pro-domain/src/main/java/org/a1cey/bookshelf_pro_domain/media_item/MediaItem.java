package org.a1cey.bookshelf_pro_domain.media_item;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.Description;
import org.a1cey.bookshelf_pro_domain.ID;
import org.a1cey.bookshelf_pro_domain.Label;
import org.a1cey.bookshelf_pro_domain.Title;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

public abstract class MediaItem {

    @Identity
    @Valid
    private final ID id;
    private final @Valid Title title;
    private final @Nullable URI coverImageUrl;
    private final Description description;
    private final List<@Valid Label> labels;

    protected MediaItem(
            ID id,
            Title title,
            @Nullable URI coverImageUrl,
            Description description,
            List<Label> labels
    ) {
        this.id = id;
        this.title = title;
        this.labels = List.copyOf(labels); // prevent modification from outside
        this.coverImageUrl = coverImageUrl;
        this.description = description;
    }

    public ID getID() {
        return id;
    }

    public Title getTitle() {
        return title;
    }

    public @Nullable URI getCoverImageUrl() {
        return coverImageUrl;
    }

    public Description getDescription() {
        return description;
    }

    public List<Label> getLabels() {
        return labels;
    }

}

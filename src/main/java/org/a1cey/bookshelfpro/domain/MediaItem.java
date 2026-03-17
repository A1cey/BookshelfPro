package org.a1cey.bookshelfpro.domain;

import jakarta.validation.Valid;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

@AggregateRoot
public abstract sealed class MediaItem permits Book {

    @Identity
    @Valid
    private final ID id;
    private final @Valid Title title;
    private final @Nullable URI coverImageUrl;
    private final String description;
    private final List<@Valid Label> labels;

    protected MediaItem(
            ID id,
            Title title,
            @Nullable URI coverImageUrl,
            String description,
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

    public String getDescription() {
        return description;
    }

    public List<Label> getLabels() {
        return labels;
    }

}

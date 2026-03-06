package org.a1cey.bookshelfpro.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.List;

@Entity
public abstract sealed class MediaItem permits Book {

    @Identity
    @Valid
    private final ID id;
    @NotBlank
    private final String title;
    private final @Nullable URI coverImageUrl;
    private final String description;
    private final List<@Valid Label> labels;

    protected MediaItem(
            ID id,
            @NotBlank String title,
            @Nullable URI coverImageUrl,
            String description,
            List<Label> labels
    ) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        this.id = id;
        this.title = title;
        this.labels = List.copyOf(labels); // prevent modification from outside
        this.coverImageUrl = coverImageUrl;
        this.description = description;
    }

    public ID getID() {
        return id;
    }

    public String getTitle() {
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

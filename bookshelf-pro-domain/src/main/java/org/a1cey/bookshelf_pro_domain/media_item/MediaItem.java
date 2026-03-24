package org.a1cey.bookshelf_pro_domain.media_item;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.review.ReviewID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public abstract class MediaItem {

    @Identity
    @Valid
    private final MediaItemID id;
    private final @Valid Title title;
    private final @Nullable URI coverImageUrl;
    private final Description description;
    // not Review, this would bloat the Aggregate -> changing one review doesn't need to load all
    private final List<@Valid ReviewID> reviews;

    protected MediaItem(
            MediaItemID id,
            Title title,
            @Nullable URI coverImageUrl,
            Description description,
            List<ReviewID> reviews
    ) {
        this.id = id;
        this.title = title;
        this.reviews = new ArrayList<>(reviews); // prevent modification from outside
        this.coverImageUrl = coverImageUrl;
        this.description = description;
    }

    public MediaItemID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public @Nullable URI coverImageUrl() {
        return coverImageUrl;
    }

    public Description description() {
        return description;
    }

    public List<ReviewID> reviews() {
        return reviews;
    }

    public abstract MediaItemType mediaItemType();

}

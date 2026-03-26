package org.a1cey.bookshelf_pro_domain.media_item;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.review.ReviewID;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public abstract class MediaItem {

    @Identity
    private final MediaItemID id;
    private final MediaItemType type;
    @Valid
    private Title title;
    @Nullable
    private URI coverImageUrl;
    private Description description;
    // not Review, this would bloat the Aggregate -> changing one review doesn't need to load all
    private final List<@Valid ReviewID> reviews;
    private final UserID owner;

    protected MediaItem(
            MediaItemID id,
            MediaItemType type,
            Title title,
            @Nullable URI coverImageUrl,
            Description description,
            List<ReviewID> reviews,
            UserID owner
    ) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.reviews = new ArrayList<>(reviews); // prevent modification from outside
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.owner = owner;
    }

    public MediaItemID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle, UserID userRequestingChange) throws IllegalArgumentException {
        if (owner != userRequestingChange) {
            throw new IllegalArgumentException(
                    "User (" + userRequestingChange +
                            ") requesting to change title is not owner of media item" + " (" + id + ")."
            );
        }

        title = newTitle;
    }

    public @Nullable URI coverImageUrl() {
        return coverImageUrl;
    }

    public void changeCoverImageUrl(@Nullable URI newUrl, UserID userRequestingChange) throws IllegalArgumentException {
        if (owner != userRequestingChange) {
            throw new IllegalArgumentException(
                    "User (" + userRequestingChange +
                            ") requesting to change coverImageUrl is not owner of " + "media item" + " (" + id + ")."
            );
        }

        coverImageUrl = newUrl;
    }

    public Description description() {
        return description;
    }

    public void changeDescription(Description newDescription, UserID userRequestingChange) throws IllegalArgumentException {
        if (owner != userRequestingChange) {
            throw new IllegalArgumentException(
                    "User (" + userRequestingChange
                            + ") requesting to change description is not owner of " + "media item" + " (" + id + ")."
            );
        }

        description = newDescription;
    }

    public List<ReviewID> reviews() {
        return reviews;
    }

    public MediaItemType type() {
        return type;
    }

    public UserID owner() {return owner;}


}

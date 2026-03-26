package org.a1cey.bookshelf_pro_domain.media_item;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.user.UserID;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;

@AggregateRoot
public abstract class MediaItem {

    @Identity
    protected final MediaItemID id;
    protected final MediaItemType type;
    @Valid
    protected Title title;
    @Nullable
    protected URI coverImageUrl;
    protected Description description;
    protected final UserID owner;

    protected MediaItem(
            MediaItemID id,
            MediaItemType type,
            Title title,
            @Nullable URI coverImageUrl,
            Description description,
            UserID owner
    ) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.owner = owner;
    }

    protected void validateChangeByOwner(UserID userRequestingChange) throws IllegalStateException {
        if (!userRequestingChange.equals(owner)) {
            throw new IllegalStateException(
                    "Change to media item " + id + " requested by non-owner user " + userRequestingChange
            );
        }
    }

    public MediaItemID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(Title newTitle, UserID userRequestingChange) throws IllegalStateException {
        validateChangeByOwner(userRequestingChange);
        title = newTitle;
    }

    public @Nullable URI coverImageUrl() {
        return coverImageUrl;
    }

    public void changeCoverImageUrl(@Nullable URI newUrl, UserID userRequestingChange) throws IllegalStateException {
        validateChangeByOwner(userRequestingChange);
        coverImageUrl = newUrl;
    }

    public Description description() {
        return description;
    }

    public void changeDescription(Description newDescription, UserID userRequestingChange) throws IllegalArgumentException {
        validateChangeByOwner(userRequestingChange);
        description = newDescription;
    }

    public MediaItemType type() {
        return type;
    }

    public UserID owner() {return owner;}


}

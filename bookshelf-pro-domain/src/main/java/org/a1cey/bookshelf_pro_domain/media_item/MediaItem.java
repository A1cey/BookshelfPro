package org.a1cey.bookshelf_pro_domain.media_item;

import java.net.URI;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import jakarta.validation.Valid;

@AggregateRoot
public abstract class MediaItem {

    @Identity
    protected final MediaItemId id;
    protected final MediaItemType type;
    protected final AccountId owner;
    protected Title title;
    protected Subtitle subtitle;
    @Nullable
    protected URI coverImageUrl;
    protected Description description;
    protected Language language;

    protected MediaItem(
        MediaItemId id,
        MediaItemType type,
        @Valid Title title,
        Subtitle subtitle,
        @Nullable URI coverImageUrl,
        Description description,
        AccountId owner,
        Language language
    ) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
        this.coverImageUrl = coverImageUrl;
        this.description = description;
        this.owner = owner;
        this.language = language;
    }

    public MediaItemId id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(@Valid Title newTitle, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        title = newTitle;
    }

    public Subtitle subtitle() {
        return subtitle;
    }

    public void changeSubtitle(Subtitle newSubtitle, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        subtitle = newSubtitle;
    }
    
    public @Nullable URI coverImageUrl() {
        return coverImageUrl;
    }

    public void changeCoverImageUrl(@Nullable URI newUrl, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        coverImageUrl = newUrl;
    }

    public Description description() {
        return description;
    }

    public void changeDescription(Description newDescription, AccountId userRequestingChange) throws IllegalArgumentException {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        description = newDescription;
    }

    public MediaItemType type() {
        return type;
    }

    public AccountId owner() {
        return owner;
    }

    public Language language() {
        return language;
    }

    public void changeLanguage(@Valid Language newLanguage, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        language = newLanguage;
    }

}

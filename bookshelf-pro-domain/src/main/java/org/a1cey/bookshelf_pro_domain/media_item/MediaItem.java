package org.a1cey.bookshelf_pro_domain.media_item;

import jakarta.validation.Valid;
import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountID;
import org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jspecify.annotations.Nullable;

import java.net.URI;

@AggregateRoot
public abstract class MediaItem {

    @Identity
    protected final MediaItemID id;
    protected final MediaItemType type;
    protected Title title;
    protected Subtitle subtitle;
    @Nullable
    protected URI coverImageUrl;
    protected Description description;
    protected final AccountID owner;
    protected Language language;

    protected MediaItem(
            MediaItemID id,
            MediaItemType type,
            @Valid Title title,
            Subtitle subtitle,
            @Nullable URI coverImageUrl,
            Description description,
            AccountID owner,
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

    public MediaItemID id() {
        return id;
    }

    public Title title() {
        return title;
    }

    public void changeTitle(@Valid Title newTitle, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        title = newTitle;
    }

    public Subtitle subtitle() {
        return subtitle;
    }

    public void changeSubtitle(Subtitle newSubtitle, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        subtitle = newSubtitle;
    }


    public @Nullable URI coverImageUrl() {
        return coverImageUrl;
    }

    public void changeCoverImageUrl(@Nullable URI newUrl, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        coverImageUrl = newUrl;
    }

    public Description description() {
        return description;
    }

    public void changeDescription(Description newDescription, AccountID userRequestingChange) throws IllegalArgumentException {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        description = newDescription;
    }

    public MediaItemType type() {
        return type;
    }

    public AccountID owner() {return owner;}

    public Language language() {
        return language;
    }

    public void changeLanguage(@Valid Language newLanguage, AccountID userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        language = newLanguage;
    }

}

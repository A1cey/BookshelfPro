package org.a1cey.bookshelf_pro_domain.media_item.movie;

import java.net.URI;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.OwnershipPolicy;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItem;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemType;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

import jakarta.validation.Valid;

@AggregateRoot
public final class Movie extends MediaItem {

    private final ImdbTitleId imdbTitleId;
    private final Set<Actor> actors;
    private final Set<Director> directors;
    @Nullable
    private ReleaseDate releaseDate;
    private final Set<Studio> studios;
    private OriginCountry originCountry;
    private Duration duration;

    public ImdbTitleId imdbTitleId() {
        return imdbTitleId;
    }

    public Set<Actor> actors() {
        return actors;
    }

    public void changeActors(Set<@Valid Actor> actors, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        this.actors.clear();
        this.actors.addAll(actors);
    }

    public Set<Director> directors() {
        return directors;
    }

    public void changeDirectors(Set<@Valid Director> directors, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        this.directors.clear();
        this.directors.addAll(directors);
    }

    public @Nullable ReleaseDate releaseDate() {
        return releaseDate;
    }

    public void changeReleaseDate(@Valid @Nullable ReleaseDate newReleaseDate, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        releaseDate = newReleaseDate;
    }

    public Set<Studio> studios() {
        return studios;
    }

    public void changeStudios(Set<@Valid Studio> studios, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        this.studios.clear();
        this.studios.addAll(studios);
    }

    public OriginCountry originCountry() {
        return originCountry;
    }

    public void changeOriginCountry(@Valid OriginCountry newOriginCountry, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        originCountry = newOriginCountry;
    }

    public Duration duration() {
        return duration;
    }

    public void changeDuration(Duration newDuration, AccountId userRequestingChange) {
        OwnershipPolicy.validate(owner, userRequestingChange, id);
        duration = newDuration;
    }

    public MovieConsumptionProgress createProgress(Duration watchedDuration) {
        return new MovieConsumptionProgress(watchedDuration, duration);
    }

    Movie(
        @Valid MediaItemId id,
        @Valid Title title,
        Subtitle subtitle,
        @Nullable URI coverImageUrl,
        Description description,
        @Valid AccountId owner,
        Set<@Valid Language> languages,
        @Valid ImdbTitleId imdbTitleId,
        Set<@Valid Actor> actors,
        Set<@Valid Director> directors,
        @Nullable ReleaseDate releaseDate,
        Set<@Valid Studio> studios,
        OriginCountry originCountry,
        Duration duration
    ) {
        super(id, MediaItemType.MOVIE, title, subtitle, coverImageUrl, description, owner, languages);
        this.imdbTitleId = imdbTitleId;
        this.actors = new HashSet<>(actors);
        this.directors = new HashSet<>(directors);
        this.releaseDate = releaseDate;
        this.studios = new HashSet<>(studios);
        this.originCountry = originCountry;
        this.duration = duration;
    }

    public static MovieBuilder builder(
        @Valid MediaItemId id,
        @Valid AccountId owner,
        @Valid Title title,
        @Valid ImdbTitleId imdbTitleId,
        @Valid Duration duration
    ) {
        return new MovieBuilder(id, owner, title, imdbTitleId, duration);
    }

    public static final class MovieBuilder {
        private final MediaItemId id;
        private final AccountId owner;
        private final Title title;
        private final Set<Language> languages = new HashSet<>();
        private final ImdbTitleId imdbTitleId;
        private final Set<Actor> actors = new HashSet<>();
        private final Set<Director> directors = new HashSet<>();
        @Nullable
        private ReleaseDate releaseDate = null;
        private final Set<Studio> studios = new HashSet<>();
        private OriginCountry originCountry = new OriginCountry("");
        private final Duration duration;
        private @Nullable URI coverImageUrl;
        private Description description = new Description("");
        private Subtitle subtitle = new Subtitle("");

        private MovieBuilder(
            @Valid MediaItemId id,
            @Valid AccountId owner,
            @Valid Title title,
            @Valid ImdbTitleId imdbTitleId,
            Duration duration
        ) {
            this.id = id;
            this.owner = owner;
            this.imdbTitleId = imdbTitleId;
            this.title = title;
            this.duration = duration;
            languages.add(Language.of(Locale.ENGLISH));
        }

        public MovieBuilder coverImageUrl(@Nullable URI coverImageUrl) {
            this.coverImageUrl = coverImageUrl;
            return this;
        }

        public MovieBuilder description(Description description) {
            this.description = description;
            return this;
        }

        public MovieBuilder subtitle(Subtitle subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public MovieBuilder languages(Set<@Valid Language> languages) {
            this.languages.addAll(languages);
            return this;
        }

        public MovieBuilder language(@Valid Language language) {
            this.languages.add(language);
            return this;
        }

        public MovieBuilder actor(@Valid Actor actor) {
            this.actors.add(actor);
            return this;
        }

        public MovieBuilder actors(Set<@Valid Actor> actors) {
            this.actors.addAll(actors);
            return this;
        }

        public MovieBuilder directors(Set<@Valid Director> directors) {
            this.directors.addAll(directors);
            return this;
        }

        public MovieBuilder director(@Valid Director director) {
            this.directors.add(director);
            return this;
        }

        public MovieBuilder studios(Set<@Valid Studio> studios) {
            this.studios.addAll(studios);
            return this;
        }

        public MovieBuilder studio(@Valid Studio studio) {
            this.studios.add(studio);
            return this;
        }

        public MovieBuilder releaseDate(@Valid ReleaseDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder originCountry(@Valid OriginCountry originCountry) {
            this.originCountry = originCountry;
            return this;
        }

        public Movie build() {
            return new Movie(
                id,
                title,
                subtitle,
                coverImageUrl,
                description,
                owner,
                languages,
                imdbTitleId,
                actors,
                directors,
                releaseDate,
                studios,
                originCountry,
                duration
            );
        }

    }
}

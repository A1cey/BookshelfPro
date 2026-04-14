package org.a1cey.bookshelf_pro_domain.media_item.movie;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.MediaItemConsumptionProgress;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public final class MovieConsumptionProgress implements MediaItemConsumptionProgress {
    private final Duration watched;
    private final Duration total;

    MovieConsumptionProgress(Duration watched, Duration total) {
        if (watched.time().compareTo(total.time()) > 0) {
            throw new IllegalArgumentException(
                "Watched duration cannot be greater than total duration:\nwatched:" + watched + ",\ntotal:" + total
            );
        }
        this.total = total;
        this.watched = watched;
    }

    public Duration current() {
        return watched;
    }

    public Duration total() {
        return total;
    }

    @Override
    public double percentage() {
        return (double) watched.time().toMillis() / total.time().toMillis();
    }

    @Override
    public boolean isCompleted() {
        return watched.equals(total);
    }

    @Override
    public boolean isStarted() {
        return watched.time().isPositive() && !isCompleted();
    }

    @Override
    public boolean isNotStarted() {
        return watched.time().isZero() && total.time().isZero();
    }

    @Override
    public MovieConsumptionProgress update(long newWatchedInMillis) {
        return new MovieConsumptionProgress(new Duration(java.time.Duration.ofMillis(newWatchedInMillis)), total);
    }

    /**
     * Only use this method if you know that total is valid for the Movie this is created for.
     * Otherwise, use Movie.createProgress.
     * Example:
     * Reading the progress from the DB must give you a valid total. Therefore, you can use this method.
     */
    public static MovieConsumptionProgress reconstruct(Duration current, Duration total) {
        return new MovieConsumptionProgress(current, total);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MovieConsumptionProgress other)) {
            return false;
        }

        return total.equals(other.total()) && watched.equals(other.watched);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(watched);
        hashCodeBuilder.append(total);
        return hashCodeBuilder.toHashCode();
    }

}

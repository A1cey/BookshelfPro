package org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import jakarta.validation.Valid;

@Entity
public final class ConsumptionProgress {

    @Identity
    private final ConsumptionProgressId id;
    private ConsumptionState state;
    @Valid
    private MediaItemConsumptionProgress progress;

    public ConsumptionProgress(ConsumptionProgressId id, MediaItemConsumptionProgress progress, ConsumptionState initialState) {
        this.id = id;
        this.state = initialState.nextState(progress);
        this.progress = progress;
    }

    public void updateProgress(MediaItemConsumptionProgress newProgress) {
        this.state = state.nextState(newProgress);
        this.progress = newProgress;
    }

    public MediaItemConsumptionProgress progress() {
        return progress;
    }

    public ConsumptionState state() {
        return state;
    }

    public ConsumptionProgressId id() {
        return id;
    }

    public ConsumptionProgressSnapshot snapshot() {
        return ConsumptionProgressSnapshot.of(this);
    }

}

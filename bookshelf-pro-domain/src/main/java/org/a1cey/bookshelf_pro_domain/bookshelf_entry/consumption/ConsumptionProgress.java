package org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption;

import jakarta.validation.Valid;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

@Entity
public final class ConsumptionProgress {

    @Identity
    private final ConsumptionProgressID id;
    private ConsumptionState state;
    @Valid
    private MediaItemConsumptionProgress progress;

    public ConsumptionProgress(ConsumptionProgressID id, MediaItemConsumptionProgress progress) {
        this.id = id;
        this.state = ConsumptionState.NOT_STARTED.nextState(progress);
        this.progress = progress;
    }

    public void updateProgress(MediaItemConsumptionProgress newProgress) {
        this.state = state.nextState(newProgress);
        this.progress = newProgress;
    }

    public MediaItemConsumptionProgress progress() {
        return progress;
    }

    public ConsumptionState state() {return state;}

    public ConsumptionProgressID id() {
        return id;
    }

    public ConsumptionProgressSnapshot snapshot() {
        return ConsumptionProgressSnapshot.of(this);
    }

}

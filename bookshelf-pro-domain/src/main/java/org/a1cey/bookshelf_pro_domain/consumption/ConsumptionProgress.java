package org.a1cey.bookshelf_pro_domain.consumption;

import org.jmolecules.ddd.annotation.Entity;

@Entity
public final class ConsumptionProgress {

    private final ConsumptionProgressID id;
    private ConsumptionState state;
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

    public MediaItemConsumptionProgress getProgress() {
        return progress;
    }

    public ConsumptionProgressID getID() {
        return id;
    }

}

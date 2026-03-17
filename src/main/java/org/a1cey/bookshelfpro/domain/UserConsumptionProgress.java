package org.a1cey.bookshelfpro.domain;

import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public final class UserConsumptionProgress {

    private final ID mediaItemId;
    private ConsumptionState state;
    private ConsumptionProgress progress;

    public UserConsumptionProgress(ID mediaItemId, ConsumptionProgress progress) {
        this.mediaItemId = mediaItemId;
        this.state = ConsumptionState.NOT_STARTED.nextState(progress);
        this.progress = progress;
    }

    public void updateProgress(ConsumptionProgress newProgress) {
        this.state = state.nextState(newProgress);
        this.progress = newProgress;
    }

    public ConsumptionProgress getProgress() {
        return progress;
    }

}

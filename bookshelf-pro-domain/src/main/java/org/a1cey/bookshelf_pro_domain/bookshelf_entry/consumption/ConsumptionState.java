package org.a1cey.bookshelf_pro_domain.bookshelf_entry.consumption;

import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public enum ConsumptionState {
    NOT_STARTED,
    STARTED,
    COMPLETED;

    public ConsumptionState nextState(MediaItemConsumptionProgress progress) {
        ConsumptionState targetState = calculateState(progress);

        // Even if progress is 0, if we were already started/finished,
        // we stay in STARTED (representing a "Reset" or "Re-consumption" start)
        if (this != NOT_STARTED && targetState == NOT_STARTED) {
            return STARTED;
        }

        return targetState;
    }

    private static ConsumptionState calculateState(MediaItemConsumptionProgress progress) {
        if (progress.isCompleted()) {
            return COMPLETED;
        }
        if (progress.isStarted()) {
            return STARTED;
        }
        return NOT_STARTED;
    }
}
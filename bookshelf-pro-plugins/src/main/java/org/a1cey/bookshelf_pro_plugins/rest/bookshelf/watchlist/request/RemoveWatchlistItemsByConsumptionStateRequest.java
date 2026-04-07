package org.a1cey.bookshelf_pro_plugins.rest.bookshelf.watchlist.request;

import java.util.Set;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;

public record RemoveWatchlistItemsByConsumptionStateRequest(
    Set<ConsumptionState> consumptionStates
) {}

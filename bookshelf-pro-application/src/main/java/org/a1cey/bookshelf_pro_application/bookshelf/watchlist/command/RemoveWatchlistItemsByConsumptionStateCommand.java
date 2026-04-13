package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command;

import java.util.Set;

import org.a1cey.bookshelf_pro_domain.bookshelf.bookshelf_entry.consumption.ConsumptionState;
import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;

public record RemoveWatchlistItemsByConsumptionStateCommand(WatchlistId watchlistId, Set<ConsumptionState> consumptionStates) {}

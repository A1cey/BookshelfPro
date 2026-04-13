package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.command;

import org.a1cey.bookshelf_pro_domain.bookshelf.watchlist.WatchlistId;

public record DeleteWatchlistCommand(WatchlistId watchlistId) {}

package org.a1cey.bookshelf_pro_application.bookshelf.watchlist.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.WatchlistDto;

public record GetAllWatchlistsResult(Set<WatchlistDto> watchlists) {}

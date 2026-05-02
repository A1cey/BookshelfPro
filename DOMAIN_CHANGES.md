# Domain changes

- Rating is normalized to [0.0, 10.0] TODO: is this good architecture?
- Labels should be private not global as modifying existing labels should not affect everyone
- MediaItem does no longer hold a list of ReviewIDs, reviews already have a MediaItemID -> relation unidirectional
- Labels are NOT global but user specific. Labels are value objects, Labels can contain lowercase letters, digits and spaces
- Playlists and Watchlists cannot have reviews
- Usernames are unique, between 2 and 30 chars and can only contain numbers, letters and underscores
- Watchlists items are allowed to be COMPLETED as users may wanna mark an already consumed media item for future watching. To still
  provide and easy way of cleaning up Watchlists a use case has been added: RemoveWatchlistItemsByStateUseCase accepting a
  Set<ConsumptionState>
- Book authors are unique (a set)
- Bookshelf entries cannot be deleted (no problems with watchlists and playlists, users can just delete their acoount)
- Deleting account means setting deleted flag in account. This account cannot authenticate themselves anymore.
- Updating playlist and watchlist items first adds the new items from the newItems list, then removes the items from
  the removeItems list and, for playlists, then moves the items according to the moveItems list
- Watchlists cannot be sorted and are internally sorted by insertion order
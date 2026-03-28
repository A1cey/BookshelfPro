- Domain changes:
    - Rating is normalized to [0.0, 10.0] TODO: is this good architecture?
    - Labels should be private not global as modifying existing labels should not affect everyone
    - MediaItem does no longer hold a list of ReviewIDs, reviews already have a MediaItemID -> relation unidirectional
    - Labels are NOT global but user specific. Labels are value objects, Labels can contain lowercase letters, digits and spaces
    - Playlists and Watchlists cannot have reviews
    - Usernames are unique, between 2 and 30 chars and can only contain numbers, letters and underscores
    - Watchlists items are allowed to be COMPLETED as users may wanna mark an already consumed media item for future watching. To still
      provide and easy way of cleaning up Watchlists a use case has been added: RemoveWatchlistItemsByStateUseCase accepting a
      Set<ConsumptionState>
- Should Media Item have the type attribute -> Matching on instanceof(mediaItem) is another possibility
- Deleting bookshelf affects watchlists and playlists as they reference the id
    - Either do not allow deletion of bookshelf entries by user (reasonable as it tracks consumption and labels and the user has so good use
      case to remove this data)
    - Or remove all watchlist/playlist items referencing the entry, when the entry is deleted (this can be quite some work) 
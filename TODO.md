# General

- Is checking owner a domain rule or other layer? -> OwnershipPolicy.validate checks authorization
- Write the documentation and README

# Use Cases

!: authentication needed \
(): values in the request \
-> : return values \
[]: optional value \
Note: Individual change*/add*/remove* use cases are combined into one patch request update use case

- [x] Account
    - [x] Create account (username, password, [email])
    - [x] ! Delete account (accountId)
    - [x] ! Change username (newUsername)
    - [x] ! Change email (newEmail)
    - [x] ! Change password (newPassword)
    - [x] ! Get account details -> id, username, email
    - [x] ! Get media items -> mediaItems
    - [x] ! Get reviews -> reviews
- [x] Bookshelf
    - [x] ! Get bookshelf entries -> bookshelfEntries
    - [x] ! Get playlists -> playlists
    - [x] ! Get watchlists -> watchlists
    - [x] BookshelfEntry
        - [x] ! Create bookshelf entry (mediaItemId, [consumptionProgressSnapshot], [labels]) // TODO: or make this a use
          case on MediaItem -> Book.addToBookshelf
        - [x] ! Update consumption progress (MediaItemConsumptionProgress)
        - [x] ! Add label (label)
        - [x] ! Remove label (label)
        - [x] ! Get bookshelf entry details -> id, mediaItemId, owner, consumptionProgressSnapshot, labels
    - [x] Playlist
        - [x] ! Create playlist (title, [items])
        - [x] ! Delete playlist (playlistId)
        - [x] ! Change title (newTitle)
        - [x] ! Add item (bookshelfEntryId)
        - [x] ! Remove item (playlistItemId)
        - [x] ! Move Item (oldPosition, newPosition)
        - [x] ! Get playlist details -> id, title, items
    - [x] Watchlist
        - [x] ! Create watchlist (title, [items])
        - [x] ! Delete watchlist (watchlistId)
        - [x] ! Change title (newTitle)
        - [x] ! Add item (bookshelfEntryId)
        - [x] ! Remove item (watchlistItemId)
        - [x] ! Remove items by consumption state (consumptionStates)
        - [x] ! Get watchlist details -> id, title, items
- [ ] MediaItem
    - [x] Search ([titleFragment], [subtitleFragment], [languages], [mediaItemType],
      [isbn], [pageCount], [authors], [publishDate], [publisher], [publishPlace], ...) -> media items
    - [x] Get all media items -> media items
    - [x] Book
        - [x] ! Create book (isbn, title, [subtitle], [description], [coverImageUrl], [languages],
          pageCount, [authors], [publishDate], [publisher], [publishPlace])
        - [x] ! Change title (newTitle)
        - [x] ! Change subtitle (newSubtitle)
        - [x] ! Change description (newDescription)
        - [x] ! Change coverImageUrl (newCoverImageUrl)
        - [x] ! Add language (language)
        - [x] ! Remove language (language)
        - [x] ! Change pageCount (newPageCount)
        - [x] ! Add author (author)
        - [x] ! Remove author (author)
        - [x] ! Change publishDate (newPublishDate)
        - [x] ! Change publisher (newPublisher)
        - [x] ! Change publishPlace (publishPlace)
        - [x] Get book details -> id, owner, isbn, title, subtitle, description, coverImageUrl, languages, pageCount, authors,
          publishDate, publisher, publishPlace
        - [x] Get all books -> details of books
    - [x] Movie/Video
        - ...
    - [ ] Series
        - ...
    - [ ] Music
        - ...
    - [x] Review
        - [x] ! Create review (mediaItemId, rating, comment)
        - [x] ! Delete review (reviewId)
        - [x] ! Change review (newRating, newComment)
        - [x] ! Change rating (newRating)
        - [x] ! Change comment (newComment)
        - [x] Get review details -> id, owner, mediaItemId, reviewHistory
        - [x] Get all reviews for media item

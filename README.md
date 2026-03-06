# Bookshelf Pro

## Themenbeschreibung
Die Anwendung ist eine Medienverwaltung, mit der Nutzer ihre Bücher, Filme, Serien und Musik
verwalten können. Sie haben die Möglichkeit Watchlists, Playlists, etc. zu erstellen und die
hinzugefügten Medien zu bewerten und zu kommentieren (eventl. mit einer Historie). Eine
Einbeziehung externer Daten sowie Bewertungen kann eine mögliche Erweiterung sein. Es gibt
verschiedene Konten. Freundeslisten und Teilen der eigenen Bibliothek(en) kann eine weiter mögliche
Erweiterung sein. Die Medien können nach verschiedenen Kategorien sortiert werden (zuletzt
hinzugefügt, zuletzt gesehen, alphanumerisch, Label, ...).

## Use Cases
- Medien verwalten (CRUD)
- Medien bewerten und kommentieren (eventl. mit externen Datensätzen)
- Kategorisieren mit Labels
- Organisieren Playlists/Watchlists

## Technologien
- Java 25
- Spring Boot mit Spring Modulith, jOOQ, Spring Web, SpringDoc-OpenAPI
- Eventl. jMolecules 2 für Dev UX (Not used)
- Maven
- Eventl. Lombok für Dev UX (Not used)
- Postgresql o.ä.
- Flyway
- JUnit 6, Mockito
- Eventl. Mapstruct

## Ubiquitous Language

### Syntax definitions

optional component T := \[T] \
collection of items of type T := (T)

| Term       | Definition                                                                                                                            | Components (WIP)                                                                                            |
|------------|---------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| User       | An account holder who manages their library and interacts with the system.                                                            | username, email, (Role)                                                                                     |
|            |                                                                                                                                       |                                                                                                             |                                                                                                |
| Media Item | The abstract concept of any content managed in the system (Book, Film, Series, Music, Season, Episode).                               | (Label), title, \[description], \[coverImage]                                                               |
| Book       | A written work.                                                                                                                       | \[ISBN], \[subtitle], \[(author)], \[publishDate], \[publisher], \[publishPlace], \[pageCount], \[language] |
| Film       | A standalone motion picture.                                                                                                          | \[IMDb], \[duration]                                                                                        |
| Series     | A motion picture consisting of one or multiple Seasons.                                                                               | \[IMDb], (Season)                                                                                           |
| Season     | A season of a Series consisting of multiple Episodes. Seasons may be ordered by their number.                                         | number, title, Series                                                                                       |
| Episode    | An episode of a Season. Episodes may be ordered by their number.                                                                      | number, title, Season, \[duration]                                                                          |
| Music      | An audio composition or track.                                                                                                        | (artist), \[duration],                                                                                      |
|            |                                                                                                                                       |                                                                                                             |                                                                                                |
| Watchlist  | A collection of Media Items that a User intends to consume in the future. Order is not important or can be reordered.                 | name, \[description], (Media Item)                                                                          |
| Playlist   | An ordered collection of MediaItems, that the User intends to consume in order.                                                       | name, \[description], (Media Item)                                                                          |
|            |                                                                                                                                       |                                                                                                             |                                                                                                |
| Rating     | A quantitative score (0.0-10.0 points) for a Media Item.                                                                              | value                                                                                                       |
| Comment    | A written commentary for a Media Item.                                                                                                | content                                                                                                     |
| Review     | A rating with a comment created by a User for a specific Media Item.                                                                  | User, Rating, Comment, date, Media Item                                                                     |
| Review Log | A historical record of changes to a Review.                                                                                           | date, (Review)                                                                                              |
|            |                                                                                                                                       |                                                                                                             |                                                                                                |
| Label      | A user-defined tag used to categorize Media Items (e.g., "Favorites", "Programming"). Multiple Labels can be applied to a Media Item. | name                                                                                                        |

## Clean Architecture

```
    +---------------------------------------+
    | infrastructure/plugin layer           |
    | db, api, controllers, ...             |
    | adapters (can be own layer) , ...     |
    +---------------------------------------+

                        |
                        V

    +-----------------------------------+
    | application layer                 |
    | use cases, application services   |
    | business logic                    |
    | orchestrate domain code           |
    +-----------------------------------+

                        |
                        V

    +-----------------------------------------------+
    | core/domain layer                             |
    | DDD (domain services, value objects, ...)     |
    | abstractions (if no abstraction layer)        |
    +-----------------------------------------------+

                        |
                        V

    +-----------------------------------+
    | abstraction layer                 |
    | algorithms, datastructures, ...   |
    | *do not use if not important*     |
    +-----------------------------------+
```

## API

The API is designed around REST principles.

### Media Items (General)

- `GET /api/media-items` - List all media items. Supports filtering by label, date, etc.
- `GET /api/media-items/{id}` - Get details of a specific media item.

### Books

- `GET /api/books` - List all books. Supports filtering by label, date, etc.
- `POST /api/books` - Create a new book.
- `GET /api/books/{id}` - Get a specific book.
- `PUT /api/books/{id}` - Update a book.
- `DELETE /api/books/{id}` - Delete a book.

### Films

- `GET /api/films` - List all films. Supports filtering by label, date, etc.
- `POST /api/films` - Create a new film.
- `GET /api/films/{id}` - Get a specific film.
- `PUT /api/films/{id}` - Update a film.
- `DELETE /api/films/{id}` - Delete a film.

### Series, Seasons & Episodes

- `GET /api/series` - List all series. Supports filtering by label, date, etc.
- `POST /api/series` - Create a new series.
- `GET /api/series/{id}` - Get a specific series.
- `PUT /api/series/{id}` - Update a series.
- `DELETE /api/series/{id}` - Delete a series.

- `GET /api/series/{id}/seasons` - List seasons of a series. Supports filtering by label, date, etc.
- `POST /api/series/{id}/seasons` - Add a season to a series.
- `GET /api/seasons/{id}` - Get details of a season.
- `PUT /api/seasons/{id}` - Update a season.

- `GET /api/seasons/{id}/episodes` - List episodes of a season. Supports filtering by label, date, etc.
- `POST /api/seasons/{id}/episodes` - Add an episode to a season.
- `GET /api/episodes/{id}` - Get details of an episode.
- `PUT /api/episodes/{id}` - Update an episode.

### Music

- `GET /api/music` - List all music tracks. Supports filtering by label, date, etc.
- `POST /api/music` - Add a new music track.
- `GET /api/music/{id}` - Get a specific music track.
- `PUT /api/music/{id}` - Update a music track.
- `DELETE /api/music/{id}` - Delete a music track.

### Reviews & Ratings

- `GET /api/media-items/{id}/reviews` - Get all reviews for a media item. Supports filtering by date, etc.
- `POST /api/media-items/{id}/reviews` - Add a review (rating + comment) to a media item.
- `GET /api/media-items/{id}/ratings` - Get all ratings for a media item. Supports filtering by date, etc.
- `GET /api/reviews/{id}` - Get a specific review.
- `PUT /api/reviews/{id}` - Update a review.
- `DELETE /api/reviews/{id}` - Delete a review.

### Collections (Playlists & Watchlists)

- `GET /api/playlists` - List all playlists. Supports filtering by date, etc.
- `POST /api/playlists` - Create a new playlist.
- `GET /api/playlists/{id}` - Get a specific playlist.
- `PUT /api/playlists/{id}` - Update playlist metadata.
- `DELETE /api/playlists/{id}` - Delete a playlist.

- `POST /api/playlists/{id}/items` - Add a media item to a playlist.
- `DELETE /api/playlists/{id}/items/{itemId}` - Remove a media item from a playlist.
- `PUT /api/playlists/{id}/order` - Reorder items in a playlist (accepts list of item IDs in new order).

*(Endpoints for Watchlists follow the same pattern as Playlists, at `/api/watchlists`)*

### Labels

- `GET /api/labels` - List all available labels. Supports filtering by date, etc.
- `POST /api/labels` - Create a new label.
- `DELETE /api/labels/{id}` - Delete a label.
- `POST /api/media-items/{id}/labels/{labelId}` - Assign a label to a media item.
- `DELETE /api/media-items/{id}/labels/{labelId}` - Remove a label from a media item.

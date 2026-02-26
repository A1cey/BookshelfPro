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
- Spring Boot mit Spring Modulith, Spring Data JPA (instead: JDBC), Spring Web, SpringDoc-OpenAPI
- Eventl. jMolecules 2 für Dev UX (Not used)
- Maven
- Eventl. Lombok für Dev UX (Not used)
- Postgresql o.ä.
- Flyway
- JUnit 6, Mockito
- Eventl. Mapstruct

## Ubiquitous Language

### Syntax definitions

optional component T := \[T]
collection of items of type T := (T)

| Term       | Definition                                                                                                                            | Components (WIP)                                                                               |
|------------|---------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| User       | An account holder who manages their library and interacts with the system.                                                            | username, email, (Role)                                                                        |
|            |                                                                                                                                       |                                                                                                |                                                                                                |
| Media Item | The abstract concept of any content managed in the system (Book, Film, Series, Music).                                                | (Label), title, \[description], \[coverImage]                                                  |
| Book       | A written work.                                                                                                                       | \[ISBN], \[subtitle], \[(author)], \[publishDate], \[publisher], \[publishPlace], \[pageCount] |
| Film       | A standalone motion picture.                                                                                                          | \[IMDb], \[duration]                                                                           |
| Series     | A motion picture consisting of one or multiple Seasons.                                                                               | \[IMDb], (Season)                                                                              |
| Season     | A season of a Series consisting of multiple Episodes. Seasons may be ordered by their number.                                         | number, title, Series                                                                          |
| Episode    | An episode of a Season. Episodes may be ordered by their number.                                                                      | number, title, Season, \[duration]                                                             |
| Album      | An ordered collection of Music.                                                                                                       | title                                                                                          | 
| Music      | An audio composition or track.                                                                                                        | (artist), \[Album], \[duration],                                                               |
|            |                                                                                                                                       |                                                                                                |                                                                                                |
| Watchlist  | A collection of Media Items that a User intends to consume in the future.                                                             | name, \[description], (Media Item)                                                             |
| Playlist   | An ordered collection of MediaItems, that the User intends to consume in order.                                                       | name, \[description], (Media Item)                                                             |
|            |                                                                                                                                       |                                                                                                |                                                                                                |
| Rating     | A quantitative score (e.g., 1-5 stars) for a Media Item.                                                                              | value                                                                                          |
| Comment    | A written commentary for a Media Item.                                                                                                | content                                                                                        |
| Review     | A rating with a comment created by a User for a specific Media Item.                                                                  | User, Rating, Comment, date, Media Item                                                        |
| Review Log | A historical record of changes to a Review.                                                                                           | date, (Review)                                                                                 |
|            |                                                                                                                                       |                                                                                                |                                                                                                |
| Label      | A user-defined tag used to categorize Media Items (e.g., "Favorites", "Programming"). Multiple Labels can be applied to a Media Item. | name                                                                                           |

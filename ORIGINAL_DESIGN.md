# Bookshelf Pro

## Themenbeschreibung

Die Anwendung ist eine Medienverwaltungs- und Medienkonsum-Tracking Plattform, mit der Nutzer ihre Bücher, Filme, Serien und Musik verwalten
sowie ihren Konsumfortschritt verfolgen können.
Medien werden global gespeichert. Das heißt Nutzer können Medien zur globalen Bibliothek hinzufügen und eigens hinzugefügte Medien updaten.
Jeder Nutzer hat zudem eine private Bibliothek, welche alle Medien beinhaltet, mit denen der Nutzer interagiert hat, z.B. durch Watchlists,
Playlists, Bewertungen oder Labels.

Im Folgenden werden die Medien im Kontext der privaten Bibliothek besprochen:

Neben der Verwaltung von Medien ermöglicht das System das Tracking des Nutzerkonsums von Medien. Medien durchlaufen dabei verschiedene
Zustände wie NICHT_BEGONNEN, BEGONNEN oder ABGESCHLOSSEN. Abhängig vom Medientyp wird ein unterschiedlicher
Fortschritt erfasst, beispielsweise Seiten bei Büchern, Minuten bei Filmen oder Episoden bei Serien. Dieser Fortschritt wird validiert:
geleseneSeitenzahl <= SeitenzahlDesBuchs, geschauteZeit <= LängeDesFilms, gehörteZeit <= LängeDesMusiktitels, etc. Liegt der Fortschritt bei
0% so hat das Medium den Zustand NICHT_BEGONNEN, bei 100% den Zustand ABGESCHLOSSEN. Der Nutzer kann den Fortschritt jederzeit manuell
anpassen (Rekonsum, Update).
Allerdings kann ein Medium nicht mehr auf den Zustand NICHT_BEGONNEN zurückfallen, wenn es diesen verlassen hat (Erlaubte Zustandsübergänge:
NICHT_BEGONNEN -> BEGONNEN/ABGESCHLOSSEN, ABGESCHLOSSEN -> BEGONNEN, BEGONNEN -> ABGESCHLOSSEN)
Sonderfälle: Der Zustand einer Serie/Season ist definiert durch den Zustand der dazugehörigen Seasons/Episoden: Eine Serie/Season hat den
Zustand BEGONNEN, sobald eine ihrer Seasons/Episoden diesen Zustand hat und hat den Zustand nur ABGESCHLOSSEN, wenn alle ihrer
Seasons/Episoden auch den Zustand ABGESCHLOSSEN haben. Setzt der Nutzer den Zustand einer Serie/Season manuell auf BEGONNEN und keine
Season/Episode hat diesen Zustand wird die Änderung abgelehnt, setzt er den Zustand manuell auf ABGESCHLOSSEN werden alle Seasons/Episoden
auch auf ABGESCHLOSSEN gesetzt.
Serien und Seasons sind im Vergleich zu den anderen Medien zudem besonders, da sie aus weiteren Medien bestehen. Serien haben Seasons und
Season haben Episoden. Dabei gilt: Es gibt keine zwei Seasons bzw. Episoden in einer Serie bzw. Season mit derselben Nummer und eine Season
bzw. Episode gehört zu genau einer Serie bzw. Season.

Nutzer können Medien bewerten. Eine Bewertung (Review) besteht aus einem Rating im Bereich [0.0, 10.0] und einem optionalen Kommentar.
Bewertungen sind dabei an den tatsächlichen Konsum gebunden, d.h. eine Bewertung beinhaltet den bei der Erstellung der Bewertung aktuellen
Fortschritt des Nutzers (z.B. 80% abgeschlossen). Änderungen an Reviews werden historisiert, d.h. es gibt eine aktive (== aktuellste) Review
und eine Historie an Änderungen. So kann ein Nutzer für ein bestimmtes Medium nur eine aktive Review besitzen. Eine Review kann zudem
nur erstellt werden, wenn der Zustand des Mediums BEGONNEN oder ABGESCHLOSSEN ist.
Das Rating einer Playlist, Serie oder Season ergibt sich aus dem durchschnittlichen Rating der darin enthaltenen Media Items + der
direkten Reviews. Z.B.: Das Rating einer Season ergibt sich aus dem Durchschnitt der Episoden-Ratings und den direkten Ratings der
Season ((SummerEpisodenRatings + SummeDirekteRatings) / (AnzahlEpisodenRatings + AnzahlDirekterRatings)).
Bewertungen gelten für die globale Bibliothek nicht nur für die private Bibliothek.

Darüber hinaus können Nutzer Medien innerhalb ihrer privaten Bibliothek mit Labels kategorisieren (z.B. SciFi, Harry Potter, ...). Diese
Labels werden wie Medien global verwaltet, d.h. jeder Nutzer kann Label definieren. Es dürfen allerdings keine duplikate Labels existieren.
Labels dürfen nur aus Kleinbuchstaben und Leerzeichen bestehen.

Nutzer können eigene Sammlungen in Form von Watchlists und Playlists erstellen. Diese können benannt werden.
Watchlists sind unsortierte Sammlungen von Medien, welcher der Nutzer in Zukunft konsumieren
möchte. Eine Watchlist darf kein Medium enthalten, das bereits den Status ABGESCHLOSSEN hat. Das Hinzufügen eines solchen Items wird
abgelehnt. Ein Medium darf in einer Watchlist nur einmal vorkommen.
Eine Playlist ist eine sortierte Sammlung an Medien. Die Reihenfolge ist durch die Einfügeposition bestimmt, kann aber
vom Nutzer geändert werden. Medien dürfen mehrmals in einer Playlist vorkommen.

Die Medien können durchsucht werden nach ihren Eigenschaften (Titel, Autor, Künstler, Direktor, Länge, etc.)/Labels/Bewertung/....

Jeder Nutzer hat einen Account. Über diesen Account kann der Nutzer seine Bibliothek verwalten.

Mögliche Erweiterungen:

- Einbeziehung externer Daten zu den Medien (Metadaten zu Medien, Bewertungen etc.)
- Freundeslisten
- Teilen der privaten Playlists/Watchlists mit anderen Nutzern

## Use Cases

- Media Management
- Consumption Tracking
- Reviews
- Watchlists
- Playlists
- Labels
- User Accounts

### Medienverwaltung

- Medien zur globalen Bibliothek hinzufügen
- Eigene Medien aktualisieren
- Medien durchsuchen (Titel, Autor, Künstler, Regisseur, Länge, Labels, Bewertungen, etc.)
- Medien zur privaten Bibliothek hinzufügen (durch Interaktion)

### Konsumtracking

- Konsum eines Mediums starten
- Fortschritt eines Mediums aktualisieren
- Konsum eines Mediums abschließen
- Fortschritt eines Mediums zurücksetzen bzw. erneut beginnen (Rekonsum)
- Fortschritt eines Mediums validieren (z.B. Seitenzahl, Länge)
- Konsumstatus eines Mediums automatisch aus Fortschritt ableiten
- Konsumstatus einer Serie oder Season aus Episodenstatus ableiten

### Reviews und Bewertungen

- Review für ein Medium erstellen
- Review aktualisieren
- Review löschen
- Historie von Review-Änderungen einsehen
- Durchschnittsbewertung eines Mediums berechnen
- Durchschnittsbewertung von Serien, Seasons und Playlists berechnen

### Labelverwaltung

- Label erstellen
- Label bearbeiten
- Label löschen
- Label zu Medien hinzufügen
- Label von Medien entfernen

### Watchlists

- Watchlist erstellen
- Watchlist umbenennen
- Medium zu Watchlist hinzufügen
- Medium aus Watchlist entfernen
- Watchlist anzeigen

### Playlists

- Playlist erstellen
- Playlist umbenennen
- Medium zu Playlist hinzufügen
- Medium aus Playlist entfernen
- Reihenfolge der Playlist ändern
- Playlist anzeigen

### Benutzerverwaltung

- Benutzerkonto erstellen
- Benutzer anmelden
- Private Bibliothek anzeigen

## Changes

- LibraryEntry Aggregate
    - ID
    - MediaItemID
    - Progress -> Moved from MediaItems
    - Labels -> Moved from MediaItems
- MediaItems Aggregates
    - ID
    - Reviews
    - ...

## Technologien

- Java 25
- Spring Boot mit evetl. Spring Modulith, jOOQ, Spring Web, SpringDoc-OpenAPI
- Maven
- Postgres
- Flyway
- JUnit 6, Mockito

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

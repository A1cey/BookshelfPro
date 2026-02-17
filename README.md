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
- Spring Boot mit Spring Modulith, Spring Data JPA, Spring Web, SpringDoc-OpenAPI
- Eventl. jMolecules 2 für Dev UX (Not used)
- Maven
- Eventl. Lombok für Dev UX (Not used)
- Postgresql o.ä.
- Flyway
- JUnit 6, Mockito
- Eventl. Mapstruct

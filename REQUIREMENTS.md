# Project Requirements: Bookshelf Pro

## 1. Functional Requirements

### Core Features
- **User Management**:
  - Support for different user accounts
- **Media Management (CRUD)**:
  - Create, Read, Update, Delete entries for:
    - Books
    - Movies
    - Series
    - Music
- **Organization**:
  - Categorization using labels
  - Sorting capabilities: Recently added, recently seen, alphanumeric, by label
- **Lists**:
  - Create and manage Watchlists (general list of media planned to consume)
  - Create and manage Playlists (list of media to consume in specific order)
- **Interaction**:
  - Rate media (with history)
  - Comment on media (with history)

### Extensions (Nice-to-Have / Future)
- **External Integration**: Fetch data and ratings from external APIs
- **Social Features**: Friend lists, Sharing libraries

## 2. Technical Requirements & Stack
- **Language**: Java 25
- **Framework**: Spring Boot
  - Spring Modulith (Modular Monolith Architecture)
  - Spring Data JDBC (Persistence)
  - Spring Web (REST API)
  - SpringDoc-OpenAPI (Documentation)
- **Database**: PostgreSQL (managed via Flyway migrations)
- **Build Tool**: Maven
- **Testing**: JUnit 6, Mockito

## 3. Architecture
The project should follow a **Modular Monolith** approach using Spring Modulith

### Proposed Modules (Packages)
- `user`: User account management
- `media`: Core media entities (Book, Movie, Series, Music) and basic CRUD
- `interaction`: Ratings and Comments
- `organization`: Playlists, Watchlists, Labels

## 5. Implementation Status & Missing Parts
**Current Status**: Initial Project Setup only (`BookshelfProApplication.java`).

**Missing Parts (To Be Implemented)**:
1.  **Domain Models**: Entities for User, Book, Movie, Series, Music, Rating, Comment, Playlist, Label.
2.  **Database Setup**:
    - Configuration for PostgreSQL (e.g., in `compose.yaml`).
    - Flyway migration scripts for initial schema.
3.  **Repositories**: Spring Data JDBC repositories for all entities.
4.  **Services**: Business logic for CRUD, sorting, and list management.
5.  **Controllers**: REST endpoints for the frontend/API consumers.
6.  **API Documentation**: OpenAPI configuration.
7.  **Security**: Authentication/Authorization implementation.

## 6. Testing Strategy
- **Unit Tests**: JUnit 6 & Mockito for Service logic.
- **Integration Tests**: `@SpringBootTest` for Controller/Repository interaction.
- **Architecture Tests**: Ensure Modulith boundaries are respected.

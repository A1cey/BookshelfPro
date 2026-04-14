package org.a1cey.bookshelf_pro_plugins.rest.media_item.movie;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.media_item.movie.CreateMovieUseCase;
import org.a1cey.bookshelf_pro_application.media_item.movie.GetAllMoviesUseCase;
import org.a1cey.bookshelf_pro_application.media_item.movie.GetMovieByIdUseCase;
import org.a1cey.bookshelf_pro_application.media_item.movie.UpdateMovieUseCase;
import org.a1cey.bookshelf_pro_application.media_item.movie.command.CreateMovieCommand;
import org.a1cey.bookshelf_pro_application.media_item.movie.command.GetMovieCommand;
import org.a1cey.bookshelf_pro_application.media_item.movie.command.UpdateMovieCommand;
import org.a1cey.bookshelf_pro_application.media_item.movie.result.CreateMovieResult;
import org.a1cey.bookshelf_pro_application.media_item.movie.result.GetAllMoviesResult;
import org.a1cey.bookshelf_pro_application.media_item.movie.result.GetMovieByIdResult;
import org.a1cey.bookshelf_pro_domain.Title;
import org.a1cey.bookshelf_pro_domain.media_item.Description;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemId;
import org.a1cey.bookshelf_pro_domain.media_item.Subtitle;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Actor;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Director;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Duration;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ImdbTitleId;
import org.a1cey.bookshelf_pro_domain.media_item.movie.OriginCountry;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ReleaseDate;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Studio;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.movie.request.CreateMovieRequest;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.movie.request.UpdateMovieRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media-item/movie")
public class MovieController {

    private final CreateMovieUseCase createMovieUseCase;
    private final UpdateMovieUseCase updateMovieUseCase;
    private final GetMovieByIdUseCase getMovieByIdUseCase;
    private final GetAllMoviesUseCase getAllMoviesUseCase;

    public MovieController(
        CreateMovieUseCase createMovieUseCase,
        UpdateMovieUseCase updateMovieUseCase,
        GetMovieByIdUseCase getMovieByIdUseCase,
        GetAllMoviesUseCase getAllMoviesUseCase
    ) {
        this.createMovieUseCase = createMovieUseCase;
        this.updateMovieUseCase = updateMovieUseCase;
        this.getMovieByIdUseCase = getMovieByIdUseCase;
        this.getAllMoviesUseCase = getAllMoviesUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateMovieResult> createMovie(@RequestBody CreateMovieRequest request) {
        var command = new CreateMovieCommand(
            new Title(request.title()),
            Optional.ofNullable(request.subtitle()).map(Subtitle::new),
            Optional.ofNullable(request.description()).map(Description::new),
            Optional.ofNullable(request.coverImageUrl()),
            Optional.ofNullable(request.languages()).map(ls -> ls.stream().map(Language::new).collect(Collectors.toSet())),
            new ImdbTitleId(request.imdbTitleId()),
            Optional.ofNullable(request.actors())
                    .map(actors -> actors.stream().map(actor -> new Actor(actor.name(), actor.role())).collect(Collectors.toSet())),
            Optional.ofNullable(request.directors()).map(directors -> directors.stream().map(Director::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.releaseDate()).map(ReleaseDate::new),
            Optional.ofNullable(request.studios()).map(studios -> studios.stream().map(Studio::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.originCountry()).map(OriginCountry::new),
            Duration.of(request.durationSeconds())
        );

        return ResponseEntity.ok(createMovieUseCase.execute(command));
    }

    @PatchMapping("/{id}")
    public void updateMovie(@PathVariable UUID id, @RequestBody UpdateMovieRequest request) {
        var command = new UpdateMovieCommand(
            new MediaItemId(id),
            Optional.ofNullable(request.title()).map(Title::new),
            Optional.ofNullable(request.subtitle()).map(Subtitle::new),
            Optional.ofNullable(request.description()).map(Description::new),
            Optional.ofNullable(request.coverImageUrl()),
            Optional.ofNullable(request.languages()).map(ls -> ls.stream().map(Language::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.actors())
                    .map(actors -> actors.stream().map(actor -> new Actor(actor.name(), actor.role())).collect(Collectors.toSet())),
            Optional.ofNullable(request.directors()).map(directors -> directors.stream().map(Director::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.releaseDate()).map(ReleaseDate::new),
            Optional.ofNullable(request.studios()).map(studios -> studios.stream().map(Studio::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.originCountry()).map(OriginCountry::new),
            Optional.ofNullable(request.durationSeconds()).map(Duration::of)
        );

        updateMovieUseCase.execute(command);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetMovieByIdResult> getMovieById(@PathVariable UUID id) {
        var command = new GetMovieCommand(new MediaItemId(id));
        return getMovieByIdUseCase
                   .execute(command)
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<GetAllMoviesResult> getAllMovies() {
        return ResponseEntity.ok(getAllMoviesUseCase.execute());
    }
}
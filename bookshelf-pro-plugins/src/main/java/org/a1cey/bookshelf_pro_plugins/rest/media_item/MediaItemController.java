package org.a1cey.bookshelf_pro_plugins.rest.media_item;

import java.util.Optional;
import java.util.stream.Collectors;

import org.a1cey.bookshelf_pro_application.media_item.GetAllMediaItemsUseCase;
import org.a1cey.bookshelf_pro_application.media_item.SearchUseCase;
import org.a1cey.bookshelf_pro_application.media_item.command.SearchCommand;
import org.a1cey.bookshelf_pro_application.media_item.result.GetAllMediaItemsResult;
import org.a1cey.bookshelf_pro_application.media_item.result.SearchResult;
import org.a1cey.bookshelf_pro_domain.media_item.Language;
import org.a1cey.bookshelf_pro_domain.media_item.MediaItemSearchCriteria;
import org.a1cey.bookshelf_pro_domain.media_item.book.Author;
import org.a1cey.bookshelf_pro_domain.media_item.book.BookSearchCriteria;
import org.a1cey.bookshelf_pro_domain.media_item.book.Isbn;
import org.a1cey.bookshelf_pro_domain.media_item.book.PageCount;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishDate;
import org.a1cey.bookshelf_pro_domain.media_item.book.PublishPlace;
import org.a1cey.bookshelf_pro_domain.media_item.book.Publisher;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Actor;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Director;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ImdbTitleId;
import org.a1cey.bookshelf_pro_domain.media_item.movie.MovieSearchCriteria;
import org.a1cey.bookshelf_pro_domain.media_item.movie.OriginCountry;
import org.a1cey.bookshelf_pro_domain.media_item.movie.ReleaseDate;
import org.a1cey.bookshelf_pro_domain.media_item.movie.Studio;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.request.BookSearchRequest;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.request.MovieSearchRequest;
import org.a1cey.bookshelf_pro_plugins.rest.media_item.request.SearchRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media-item")
public class MediaItemController {
    private final GetAllMediaItemsUseCase getAllMediaItemsUseCase;
    private final SearchUseCase searchUseCase;

    public MediaItemController(GetAllMediaItemsUseCase getAllMediaItemsUseCase, SearchUseCase searchUseCase) {
        this.getAllMediaItemsUseCase = getAllMediaItemsUseCase;
        this.searchUseCase = searchUseCase;
    }

    @GetMapping
    public ResponseEntity<GetAllMediaItemsResult> getAllMediaItems() {
        return ResponseEntity.ok(getAllMediaItemsUseCase.execute());
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResult> search(SearchRequest request) {
        var searchCriteria = new MediaItemSearchCriteria(
            Optional.ofNullable(request.titleFragment()),
            Optional.ofNullable(request.subtitleFragment()),
            Optional.ofNullable(request.languages()).map(langs -> langs.stream().map(Language::new).collect(Collectors.toSet())),
            Optional.ofNullable(request.mediaItemType()),
            Optional.ofNullable(request.specificSearchRequest())
                    .map(specificSearchRequest -> switch (specificSearchRequest) {
                             case BookSearchRequest req -> new BookSearchCriteria(
                                 Optional.ofNullable(req.isbn()).map(Isbn::new),
                                 Optional.ofNullable(req.authors()).map(authors -> authors.stream().map(Author::new).toList()),
                                 Optional.ofNullable(req.publishDate()).map(PublishDate::new),
                                 Optional.ofNullable(req.publisher()).map(Publisher::new),
                                 Optional.ofNullable(req.publishPlace()).map(PublishPlace::new),
                                 Optional.ofNullable(req.pageCount()).map(PageCount::new)
                             );
                             case MovieSearchRequest req -> new MovieSearchCriteria(
                                 Optional.ofNullable(req.imdbTitleId()).map(ImdbTitleId::new),
                                 Optional.ofNullable(req.actors())
                                         .map(actors -> actors.stream()
                                                              .map((actor) -> new Actor(actor.name(), actor.role()))
                                                              .collect(Collectors.toSet())),
                                 Optional.ofNullable(req.directors())
                                         .map(directors -> directors.stream().map(Director::new).collect(Collectors.toSet())),
                                 Optional.ofNullable(req.releaseDate()).map(ReleaseDate::new),
                                 Optional.ofNullable(req.studios())
                                         .map(studios -> studios.stream().map(Studio::new).collect(Collectors.toSet())),
                                 Optional.ofNullable(req.originCountry()).map(OriginCountry::new),
                                 Optional.ofNullable(req.duration())
                             );
                         }

                    )
        );

        return ResponseEntity.ok(searchUseCase.execute(new SearchCommand(searchCriteria)));
    }
}
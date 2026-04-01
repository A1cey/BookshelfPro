package org.a1cey.bookshelf_pro_plugins.rest.media_item;

import org.a1cey.bookshelf_pro_application.media_item.GetAllMediaItemsUseCase;
import org.a1cey.bookshelf_pro_application.media_item.result.GetAllMediaItemsResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media-item")
public class MediaItemController {
    private final GetAllMediaItemsUseCase getAllMediaItemsUseCase;

    public MediaItemController(GetAllMediaItemsUseCase getAllMediaItemsUseCase) {
        this.getAllMediaItemsUseCase = getAllMediaItemsUseCase;
    }

    @GetMapping()
    public ResponseEntity<GetAllMediaItemsResult> getAllBooks() {
        return ResponseEntity.ok(getAllMediaItemsUseCase.execute());
    }
}
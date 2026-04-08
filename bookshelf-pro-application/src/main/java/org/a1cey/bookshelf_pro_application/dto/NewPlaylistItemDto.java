package org.a1cey.bookshelf_pro_application.dto;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record NewPlaylistItemDto(UUID item, @Nullable Integer position) {}

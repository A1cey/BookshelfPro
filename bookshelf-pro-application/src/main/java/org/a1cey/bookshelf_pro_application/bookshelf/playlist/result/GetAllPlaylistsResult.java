package org.a1cey.bookshelf_pro_application.bookshelf.playlist.result;

import java.util.Set;

import org.a1cey.bookshelf_pro_application.dto.PlaylistDto;

public record GetAllPlaylistsResult(Set<PlaylistDto> playlists) {}

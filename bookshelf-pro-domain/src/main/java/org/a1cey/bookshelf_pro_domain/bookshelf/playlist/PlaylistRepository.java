package org.a1cey.bookshelf_pro_domain.bookshelf.playlist;

import java.util.Optional;
import java.util.Set;

import org.a1cey.bookshelf_pro_domain.account.AccountId;
import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface PlaylistRepository {
    void save(Playlist playlist);

    void update(Playlist playlist);

    void delete(Playlist playlist);

    Optional<Playlist> findById(PlaylistId playlistId);

    Optional<Playlist> findByIdAndOwner(PlaylistId playlistId, AccountId owner);

    Set<Playlist> findByOwner(AccountId owner);
}

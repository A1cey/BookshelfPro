package org.a1cey.bookshelf_pro_application;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public final class IdService {
    public UUID generateId() {
        // This is enough, generating the same UUID4 twice is extremely unlikely (no problem)
        return UUID.randomUUID();
    }
}

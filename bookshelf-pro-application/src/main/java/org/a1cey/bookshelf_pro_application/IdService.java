package org.a1cey.bookshelf_pro_application;

import java.util.UUID;

public final class IdService {
    public static UUID UUIDv4() {
        // This is enough, generating the same UUID4 twice is extremely unlikely (no problem)
        return UUID.randomUUID();
    }
}

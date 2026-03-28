package org.a1cey.bookshelf_pro_domain.account;

import java.util.UUID;

import org.a1cey.bookshelf_pro_domain.Id;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record AccountId(UUID value) implements Id {}

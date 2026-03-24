package org.a1cey.bookshelf_pro_domain;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.UUID;

@ValueObject
public record ID(UUID id) {}

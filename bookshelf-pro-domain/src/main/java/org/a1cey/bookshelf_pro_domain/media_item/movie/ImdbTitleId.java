package org.a1cey.bookshelf_pro_domain.media_item.movie;

import jakarta.validation.constraints.NotBlank;

public record ImdbTitleId(@NotBlank String value) {

    public ImdbTitleId {
        if (value.isBlank()) {
            throw new IllegalArgumentException("IMBd title Id cannot be blank");
        }
        if (!value.startsWith("tt")) {
            throw new IllegalArgumentException("IMBd title Id must start with tt");
        }
        if (value.length() < 9) {
            throw new IllegalArgumentException("IMBd title Id must be at least 9 characters long ('tt' and 7+ digits)");
        }
        if (!value.substring(2).chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("IMBd title Id must only contain digits after the 'tt' prefix");
        }
    }
}

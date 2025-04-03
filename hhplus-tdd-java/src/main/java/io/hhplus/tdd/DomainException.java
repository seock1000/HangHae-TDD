package io.hhplus.tdd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class DomainException extends RuntimeException {
    private final ErrorResponse errorResponse;
}

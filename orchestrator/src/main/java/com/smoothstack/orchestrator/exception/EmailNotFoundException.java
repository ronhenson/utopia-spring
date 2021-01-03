package com.smoothstack.orchestrator.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    @NonNull
    private String email;
}

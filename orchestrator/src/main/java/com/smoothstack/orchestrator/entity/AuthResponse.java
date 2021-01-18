package com.smoothstack.orchestrator.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponse {
    private String msg;
    private boolean emailIsDuplicate;
    private boolean isDataIntegrityError;
    private boolean success;
}

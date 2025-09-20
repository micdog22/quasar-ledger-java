
package com.example.quasarledger.web.dto;

import com.example.quasarledger.domain.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountDtos {

    public static class CreateAccountRequest {
        @NotBlank public String name;
        @NotNull public AccountType type;
        public Long parentId;
    }

    public static class AccountResponse {
        public Long id;
        public String name;
        public AccountType type;
        public Long parentId;

        public AccountResponse(Long id, String name, AccountType type, Long parentId) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.parentId = parentId;
        }
    }
}

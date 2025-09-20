
package com.example.quasarledger.web.dto;

import com.example.quasarledger.domain.PostingType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class EntryDtos {

    public static class PostingRequest {
        @NotNull public Long accountId;
        @NotNull public PostingType type; // DEBIT or CREDIT
        @NotNull @DecimalMin("0.00") public BigDecimal amount;
    }

    public static class CreateEntryRequest {
        public LocalDateTime timestamp; // optional; defaults to now
        @NotBlank public String description;
        @NotNull @Valid public List<PostingRequest> postings;
    }

    public static class CreateEntryResponse {
        public Long id;
        public LocalDateTime timestamp;
        public String description;
        public int postings;

        public CreateEntryResponse(Long id, LocalDateTime ts, String desc, int pcount) {
            this.id = id; this.timestamp = ts; this.description = desc; this.postings = pcount;
        }
    }
}

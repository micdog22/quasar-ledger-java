
package com.example.quasarledger.repo;

import com.example.quasarledger.domain.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findByAccount_Id(Long accountId);
    List<Posting> findByAccount_IdAndJournalEntry_TimestampBetween(Long accountId, LocalDateTime from, LocalDateTime to);
    List<Posting> findByJournalEntry_TimestampBetween(LocalDateTime from, LocalDateTime to);
}

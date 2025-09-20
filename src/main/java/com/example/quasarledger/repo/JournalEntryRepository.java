
package com.example.quasarledger.repo;

import com.example.quasarledger.domain.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> { }

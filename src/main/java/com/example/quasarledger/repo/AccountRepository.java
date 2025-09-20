
package com.example.quasarledger.repo;

import com.example.quasarledger.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNameIgnoreCase(String name);
}

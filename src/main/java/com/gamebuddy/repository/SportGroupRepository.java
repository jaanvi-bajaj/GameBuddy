package com.gamebuddy.repository;

import com.gamebuddy.model.SportGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SportGroupRepository extends JpaRepository<SportGroup, Long> {
    Optional<SportGroup> findByInviteCode(String inviteCode);

    List<SportGroup> findBySportIgnoreCaseContaining(String sport);

    // Additional simple search methods can be added as needed
}
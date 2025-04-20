package com.datn.repository;

import com.datn.model.Label;
import com.datn.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Optional<Label> findByName(String name);
    List<Label> findByCreator(User creator);
}
package com.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.model.Chat;
import com.datn.model.Project;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    

	Chat findByProject(Project projectById);
	
//	List<Chat> findByProjectNameContainingIgnoreCase(String projectName);
}


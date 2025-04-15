package com.datn.service;

import java.util.List;

import com.datn.exception.ChatException;
import com.datn.exception.ProjectException;
import com.datn.exception.UserException;
import com.datn.model.Message;

public interface MessageService {

    Message sendMessage(Long senderId, Long chatId, String content) throws UserException, ChatException, ProjectException;

    List<Message> getMessagesByProjectId(Long projectId) throws ProjectException, ChatException;
}


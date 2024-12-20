package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountService accountService;

    public MessageService(MessageRepository messageRepository, AccountService accountService) {
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }
    
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isEmpty()
                || message.getMessageText().length() > 255
                || !accountService.isValidUser(message.getPostedBy())) {
            return Optional.empty();
        }
        Message createdMessage = messageRepository.save(message);
        return Optional.of(createdMessage);
    }

    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    public Optional<Message> deleteMessage(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        message.ifPresent(m -> messageRepository.deleteById(messageId));
        return message;
    }

    public Optional<Message> updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return Optional.empty();
        }
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setMessageText(newText);
            Message updated = messageRepository.save(message);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}

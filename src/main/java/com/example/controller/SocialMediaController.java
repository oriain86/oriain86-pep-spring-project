package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



/**
 * This controller maps the same routes that were previously handled by Javalin in the original application.
 *
 * Endpoints:
 * POST /register             -> registerAccount
 * POST /login                -> loginAccount
 * GET /messages              -> getAllMessages
 * POST /messages             -> createMessage
 * GET /messages/{messageId}  -> getMessageById
 * DELETE /messages/{messageId} -> deleteMessageById
 * PATCH /messages/{messageId} -> updateMessageText
 * GET /accounts/{accountId}/messages -> getMessagesByAccountId
 */



@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Delegates requests to the accountService.registerAccount function
    
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account) {
        Optional<Account> registeredAccount = accountService.registerAccount(account);
        if (registeredAccount.isPresent()) {
            return ResponseEntity.ok(registeredAccount.get());
        } else if (account.getUsername() == null || account.getUsername().isEmpty()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Delegates requests to the accountService.loginAccount function

    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody Account account) {
        Optional<Account> loggedInAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
        if (loggedInAccount.isPresent()) {
            return ResponseEntity.ok(loggedInAccount.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Delegates requests to the messageService.getAllMessages function

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // Delegates requests to the messageService.createMessage function

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        Optional<Message> createdMessage = messageService.createMessage(message);
        if (createdMessage.isPresent()) {
            return ResponseEntity.ok(createdMessage.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delegates requests to the messageService.getMessageById function

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable("messageId") int messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent()) {
            return ResponseEntity.ok(message.get());
        } else {
            return ResponseEntity.ok().body("");
        }
    }

    // Delegates requests to the messageService.deleteMessage function

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable("messageId") int messageId) {
        Optional<Message> deletedMessage = messageService.deleteMessage(messageId);
        if (deletedMessage.isPresent()) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().body("");
        }
    }

    // Delegates requests to the messageService.updateMessageText function

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessageText(@PathVariable("messageId") int messageId, @RequestBody Message updatedMsg) {
        String newText = updatedMsg.getMessageText();
        if (newText == null || newText.isEmpty() || newText.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Message> updatedMessage = messageService.updateMessageText(messageId, newText);
        if (updatedMessage.isPresent()) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delegates requests to the messageService.updateMessageText function

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable("accountId") int accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }
}



package com.shefin.messagingApp.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.entity.Message;
import com.shefin.messagingApp.entity.MessageList;
import com.shefin.messagingApp.entity.MessageListKey;
import com.shefin.messagingApp.entity.UnreadMessageStat;
import com.shefin.messagingApp.repo.FolderRepo;
import com.shefin.messagingApp.repo.MessageListRepo;
import com.shefin.messagingApp.repo.MessageRepo;
import com.shefin.messagingApp.repo.UnreadMessageStatsRepo;
import com.shefin.messagingApp.service.FolderService;

@Controller
public class MessageViewController {
	
	@Autowired
	private FolderRepo folderRepo;
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private MessageRepo messageRepo;
	
	@Autowired
	private MessageListRepo messageListRepo;
	
	@Autowired
	private UnreadMessageStatsRepo unreadMessageStatRepo;
	
	
	
	@RequestMapping("/messages/{id}")
	public String messageView(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable UUID id, @RequestParam String folder) {
		if (principal==null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return "index.html";
		}
		
		// here we need to pass all info from DB to the html page
		
		String userId = principal.getAttribute("login");
		
		// fetching folders
		List<Folder> userFolders = folderRepo.findAllById(userId);
		model.addAttribute("userFolders",userFolders);
		
		List<Folder> defaultFolders = folderService.getDefaultFolders(userId);
		model.addAttribute("defaultFolders",defaultFolders);
		
		// getting mes based on id		
		Optional<Message> message = messageRepo.findById(id);
		if (message.isEmpty()) {
			return "inbox-page.html";
		}
		
		Message mes = message.get();
		String toIds = String.join(",", mes.getTo());
		
		
		
		
		
		// for each message we need to change the isread
		// isread is in messagelist table and to get messagelist table you need messagelistKey
		MessageListKey key = new MessageListKey();
		key.setId(userId);
		key.setLabel(folder);
		key.setTimeUUID(mes.getId());
		
		Optional<MessageList> messagelist = messageListRepo.findById(key);
		
		if (messagelist.isPresent()) {
			MessageList messageListItem = messagelist.get();
			if (messageListItem.isUnread()) {
				messageListRepo.setIsUnreadToFalse(messageListItem.getKey().getId(),messageListItem.getKey().getLabel(),messageListItem.getKey().getTimeUUID());
				unreadMessageStatRepo.decrementUnreadCount(userId, folder);
			}
		}
		
		model.addAttribute("message",message.get());
		model.addAttribute("toIds", toIds);
		model.addAttribute("userName", principal.getAttribute("name"));
		model.addAttribute("stats", folderService.mapCountToLabel(userId));
		
		
		return "message-view.html";
		
	}

}

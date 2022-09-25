package com.shefin.messagingApp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.entity.Message;
import com.shefin.messagingApp.entity.MessageList;
import com.shefin.messagingApp.entity.MessageListKey;
import com.shefin.messagingApp.repo.FolderRepo;
import com.shefin.messagingApp.repo.MessageListRepo;
import com.shefin.messagingApp.repo.MessageRepo;
import com.shefin.messagingApp.service.FolderService;



@Controller
public class InboxController {
	@Autowired
	private FolderRepo folderRepo;
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private MessageListRepo messageListRepo;
	@Autowired
	private MessageRepo messageRepo;
	
	@RequestMapping("/inbox")
	public String homePage(
			@AuthenticationPrincipal OAuth2User principal, 
			Model model
			) {
		
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
		
		//fetching message list ; this should be a path variable as we want the page to load when click on it
		
		
		
		
		List<MessageList> inboxList = messageListRepo.findAllByKey_IdAndKey_Label(userId, "Inbox");
		model.addAttribute("inboxList", inboxList);
		model.addAttribute("folderLabel", "inbox");
		
		return "inbox-page.html";
		
	}
	
	@PostConstruct
	public void init() {
		folderRepo.save(new Folder("Shefin-Shareef","Spam","Green"));
		folderRepo.save(new Folder("Shefin-Shareef","Service","Blue"));
		folderRepo.save(new Folder("Shefin-Shareef","Work","Yelloq"));
		
		for(int i = 0; i<10;i++) {
			MessageListKey key = new MessageListKey();
			key.setId("Shefin-Shareef");
			key.setLabel("Inbox");
			key.setTimeUUID(Uuids.timeBased());
			
			MessageList list = new MessageList();
			list.setKey(key);
			list.setSubject("Subject "+ i);
			List<String> to = Arrays.asList("Shefin-Shareef","Ashiya");
			list.setTo(to);
			list.setUnread(true);
						
			messageListRepo.save(list);
			
			
			Message message = new Message();
			message.setId(key.getTimeUUID());
			message.setSubject(list.getSubject());
			message.setFrom("koushik");
			message.setBody("Body "+i);
			message.setTo(to);
			
			messageRepo.save(message);
		}
		
		
	}

}

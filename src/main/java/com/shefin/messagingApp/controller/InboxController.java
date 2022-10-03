package com.shefin.messagingApp.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.entity.MessageList;
import com.shefin.messagingApp.repo.FolderRepo;
import com.shefin.messagingApp.repo.MessageListRepo;
import com.shefin.messagingApp.service.FolderService;
import com.shefin.messagingApp.service.MessageService;



@Controller
public class InboxController {
	@Autowired
	private FolderRepo folderRepo;
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private MessageListRepo messageListRepo;
	
	
	
	@Autowired
	private MessageService messageService;
	
	@RequestMapping("/")
	public String homePage(
			@RequestParam(required = false) String folder,
			@AuthenticationPrincipal OAuth2User principal, 
			Model model
			) {
		
		if (principal==null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return "index.html";
		}
		
		// here, need to pass all info from DB to the html page
		
		String userId = principal.getAttribute("login");
		
		// fetching folders
		List<Folder> userFolders = folderRepo.findAllById(userId);
		model.addAttribute("userFolders",userFolders);
		
		List<Folder> defaultFolders = folderService.getDefaultFolders(userId);
		model.addAttribute("defaultFolders",defaultFolders);
		
		// fetching unread message stats for the user
			
		model.addAttribute("stats", folderService.mapCountToLabel(userId));
		
		//fetching message list ; this should be a path variable as we want the page to load when click on it
		
		if (!StringUtils.hasText(folder)) {
			folder="Inbox";
		}
		List<MessageList> inboxList = messageListRepo.findAllByKey_IdAndKey_Label(userId, folder);
		
		inboxList.stream().forEach(inboxListItem->{
			PrettyTime p = new PrettyTime();
			UUID timeBasedUuid = inboxListItem.getKey().getTimeUUID();
			Date messageDateTime = new Date(Uuids.unixTimestamp(timeBasedUuid));
			inboxListItem.setTimeAgo(p.format(messageDateTime));
			
		});
		
		
//		System.out.println(p.format(org.joda.time.DateTime(time).minusSeconds(1)));
		
		
		model.addAttribute("inboxList", inboxList);
		model.addAttribute("folderName", folder);
		model.addAttribute("userName", principal.getAttribute("name"));
		
		
		return "inbox-page.html";
		
	}
	
	@PostConstruct
	public void init() {
		folderRepo.save(new Folder("Shefin-Shareef","Spam","Green"));
		folderRepo.save(new Folder("Shefin-Shareef","Service","Blue"));
		folderRepo.save(new Folder("Shefin-Shareef","Work","Yellow"));
		
		
		for(int i = 0; i<10;i++) {
			messageService.sendMessage("Shareef", Arrays.asList("Shefin-Shareef","Ashiya","Sharmina"), "subject "+i, "body "+i);
		}
		
		
	}

}

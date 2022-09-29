package com.shefin.messagingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.repo.FolderRepo;
import com.shefin.messagingApp.repo.MessageListRepo;
import com.shefin.messagingApp.repo.MessageRepo;
import com.shefin.messagingApp.service.FolderService;
@Controller
public class ComposeController {
	@Autowired
	private FolderRepo folderRepo;
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private MessageListRepo messageListRepo;
	@Autowired
	private MessageRepo messageRepo;
	
	@RequestMapping("/compose")
	public String getComposePage(
			@AuthenticationPrincipal OAuth2User principal, 
			Model model) {
		
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
				
				return "compose-message.html";
				
		
		
	}

}

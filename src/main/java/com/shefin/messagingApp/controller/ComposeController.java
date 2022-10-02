package com.shefin.messagingApp.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.repo.FolderRepo;
import com.shefin.messagingApp.repo.MessageListRepo;
import com.shefin.messagingApp.repo.MessageRepo;
import com.shefin.messagingApp.service.FolderService;
import com.shefin.messagingApp.service.MessageService;

import jnr.ffi.Struct.pid_t;
@Controller
public class ComposeController {
	@Autowired
	private FolderRepo folderRepo;
	@Autowired
	private FolderService folderService;
	@Autowired
	private MessageService messageService;
	
	
	@RequestMapping("/compose")
	public String getComposePage(
			@AuthenticationPrincipal OAuth2User principal, 
			Model model,@RequestParam(required = false)  String toIds) {
		
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
				model.addAttribute("stats", folderService.mapCountToLabel(userId));
				
				
				if (StringUtils.hasText(toIds)) {
					model.addAttribute("toIds", toIds);					
				}
				
				return "compose-message.html";
				
		
		
	}
	
	// after a post request (here after getting to compose page) it is better to redirect for any clicks instead of another post request therefor imma return ModelView object which has the ability to redirect 
	
	@PostMapping("/sendMessage")
	public ModelAndView sendMessage(
			@RequestBody MultiValueMap<String, String> formData,
			@AuthenticationPrincipal OAuth2User principal
			) {
		if (principal==null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return new ModelAndView("redirect:/");
		}
		
		if (StringUtils.hasText(formData.getFirst("toIds"))) {
			String from = principal.getAttribute("login");
			String[] to = formData.getFirst("toIds").split(",");
			List<String> toIds = Arrays.asList(to);
			String subject =  formData.getFirst("subject");
			String body =  formData.getFirst("body");
			messageService.sendMessage(from, toIds, subject, body);
			return new ModelAndView("redirect:/");
		}
		return new ModelAndView("redirect:/compose"); 
		
		
		
	}
	

}

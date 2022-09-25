package com.shefin.messagingApp.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shefin.messagingApp.entity.Folder;

@Service
public class FolderService {
	
	
	
	public List<Folder> getDefaultFolders(String UserId) {
		return Arrays.asList(
				new Folder(UserId, "Inbox", "white"),
				new Folder(UserId, "Sent", "white"),
				new Folder(UserId, "Important", "white")
				);
	}
}

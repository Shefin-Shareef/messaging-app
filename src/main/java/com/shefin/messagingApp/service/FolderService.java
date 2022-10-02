package com.shefin.messagingApp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shefin.messagingApp.entity.Folder;
import com.shefin.messagingApp.entity.UnreadMessageStat;
import com.shefin.messagingApp.repo.UnreadMessageStatsRepo;

@Service
public class FolderService {
	@Autowired
	private UnreadMessageStatsRepo unreadMessageStatsRepo;
	
	
	public List<Folder> getDefaultFolders(String UserId) {
		return Arrays.asList(
				new Folder(UserId, "Inbox", "white"),
				new Folder(UserId, "Sent", "white"),
				new Folder(UserId, "Important", "white")
				);
	}
	
	public Map<String, Integer> mapCountToLabel(String userId) {

		List<UnreadMessageStat> stats = unreadMessageStatsRepo.findAllById(userId);
		return stats.stream().collect(Collectors.toMap(UnreadMessageStat::getLabel, UnreadMessageStat::getUnreadCount));
		
	}
}

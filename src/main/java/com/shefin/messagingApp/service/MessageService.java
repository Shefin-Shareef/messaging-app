package com.shefin.messagingApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.shefin.messagingApp.entity.Message;
import com.shefin.messagingApp.entity.MessageList;
import com.shefin.messagingApp.entity.MessageListKey;
import com.shefin.messagingApp.repo.MessageListRepo;
import com.shefin.messagingApp.repo.MessageRepo;
import com.shefin.messagingApp.repo.UnreadMessageStatsRepo;


@Service
public class MessageService {
	
	@Autowired 
	private MessageListRepo messageListRepo;
	@Autowired 
	private MessageRepo messageRepo;
	@Autowired
	private UnreadMessageStatsRepo unreadMessageStatsRepo;
	
	
	
	public void sendMessage(String from, List<String> to, String subject, String body) {
		Message message =  new Message();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setBody(body);
		message.setId(Uuids.timeBased());
		messageRepo.save(message);
		
		// saving message to inbox of every recipient
		to.forEach(id -> {
			MessageList item = createMessageListItem(to, id,subject, message, body,"Inbox");
			messageListRepo.save(item);
			unreadMessageStatsRepo.incrementUnreadCount(id, "Inbox");
		});
		
		// saving the entry to Sent box of sender
		MessageList sentItemEntry = createMessageListItem(to, from, subject, message, body, "Sent");
		sentItemEntry.setUnread(false);
		messageListRepo.save(sentItemEntry);
	}


	private MessageList createMessageListItem(List<String> to, String owner,String subject, Message message, String body, String label) {
		MessageListKey key = new MessageListKey();
		key.setId(owner);
		key.setLabel(label);
		key.setTimeUUID(message.getId());
		MessageList item = new MessageList();
		item.setKey(key);
		item.setSubject(subject);
		item.setTo(to);
		item.setUnread(true);
		return item;
	}
}

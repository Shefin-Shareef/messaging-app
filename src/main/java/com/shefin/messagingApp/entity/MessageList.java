package com.shefin.messagingApp.entity;

import java.util.List;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@Table(value = "messages_by_user_folder")
public class MessageList {
	@PrimaryKey
	private MessageListKey key;
	@CassandraType(type = Name.LIST,typeArguments = Name.TEXT)
	private List<String> to;
	@CassandraType(type = Name.TEXT)
	private String subject;
	@CassandraType(type = Name.BOOLEAN)
	private boolean isUnread;
	public MessageListKey getKey() {
		return key;
	}
	public void setKey(MessageListKey key) {
		this.key = key;
	}
	public List<String> getTo() {
		return to;
	}
	public void setTo(List<String> to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public boolean isUnread() {
		return isUnread;
	}
	public void setUnread(boolean isUnread) {
		this.isUnread = isUnread;
	}
	public MessageList(MessageListKey key, List<String> to, String subject, boolean isUnread) {
		super();
		this.key = key;
		this.to = to;
		this.subject = subject;
		this.isUnread = isUnread;
	}
	public MessageList() {
		super();
	}
	
	
	

}

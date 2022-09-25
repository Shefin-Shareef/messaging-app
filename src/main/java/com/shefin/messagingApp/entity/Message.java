package com.shefin.messagingApp.entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;


@Table(value = "messages_by_id")
public class Message {
	
	@PrimaryKey
	private UUID id;	
	@CassandraType(type = Name.TEXT)
	private String from;	
	@CassandraType(type = Name.LIST,typeArguments = Name.TEXT)
	private List<String> to;
	@CassandraType(type = Name.TEXT)
	private String subject;
	@CassandraType(type = Name.TEXT)
	private String body;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID timeUUID) {
		this.id = timeUUID;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Message(UUID timeUUID, String from, List<String> to, String subject, String body) {
		super();
		this.id = timeUUID;
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.body = body;
	}
	public Message() {
		super();
	}
	
	

}

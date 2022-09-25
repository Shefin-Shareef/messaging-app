package com.shefin.messagingApp.repo;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.shefin.messagingApp.entity.MessageList;
import com.shefin.messagingApp.entity.MessageListKey;

public interface MessageListRepo extends CassandraRepository<MessageList, MessageListKey> {

	public List<MessageList> findAllByKey_IdAndKey_Label(String id, String label);

}

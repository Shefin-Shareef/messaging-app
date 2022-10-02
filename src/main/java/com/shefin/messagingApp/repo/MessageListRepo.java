package com.shefin.messagingApp.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.shefin.messagingApp.entity.MessageList;
import com.shefin.messagingApp.entity.MessageListKey;

public interface MessageListRepo extends CassandraRepository<MessageList, MessageListKey> {

	
	public List<MessageList> findAllByKey_IdAndKey_Label(String id, String label);

	public List<MessageList> findByKey(MessageListKey key);
	
	@Query("update messages_by_user_folder set isunread = false where user_id=?0 and label=?1 and created_time_uuid=?2")
	public void setIsUnreadToFalse(String user_id, String label, UUID created_time_uuid);
}

package com.shefin.messagingApp.repo;

import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.shefin.messagingApp.entity.UnreadMessageStat;

@Repository
public interface UnreadMessageStatsRepo extends CassandraRepository<UnreadMessageStat, String>{
	
	public List<UnreadMessageStat> findAllById(String id);
	
	@Query("update unread_message_stats set unreadcount = unreadcount+1 where user_id=?0 and label=?1")
	public void incrementUnreadCount(String userId, String label);
	
	@Query("update unread_message_stats set unreadcount = unreadcount-1 where user_id=?0 and label=?1")
	public void decrementUnreadCount(String userId, String label);

}

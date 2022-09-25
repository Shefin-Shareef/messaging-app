package com.shefin.messagingApp.repo;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.shefin.messagingApp.entity.Message;

public interface MessageRepo extends CassandraRepository<Message, UUID>{

}

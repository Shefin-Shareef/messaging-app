package com.shefin.messagingApp.repo;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.shefin.messagingApp.entity.Folder;

@Repository
public interface FolderRepo extends CassandraRepository<Folder, String>{

}

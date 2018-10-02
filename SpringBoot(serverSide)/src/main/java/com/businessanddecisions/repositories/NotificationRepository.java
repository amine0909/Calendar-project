package com.businessanddecisions.repositories;

import com.businessanddecisions.models.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {

    @Query(value = "SELECT * FROM notifications n JOIN events e ON (e.id=n.event_id)" +
            "JOIN plannings p ON (p.id=e.calendar) "+
            "JOIN users u ON (u.id=p.creator)"+
            " WHERE receiver_id=:id AND seen='false'", nativeQuery = true)
    List<NotificationModel> findByReceiver(@Param("id") Long id);


    @Query(value= "SELECT * FROM notifications n JOIN events e ON (e.id=n.event_id)" +
             "JOIN plannings p ON (p.id=e.calendar)" +
             "JOIN users u ON (u.id=p.creator) WHERE receiver_id=:id", nativeQuery = true)
    List<NotificationModel> findAllByReceiver(@Param("id") Long id);


}

package com.businessanddecisions.repositories;


import com.businessanddecisions.models.EventModel;
import com.businessanddecisions.models.PlanningModel;
import com.businessanddecisions.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventModel,Long> {


    @Query(value="SELECT * FROM events WHERE calendar=:id", nativeQuery = true)
    List<EventModel> findByCalendar(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM events WHERE calendar=:id", nativeQuery = true)
    void deleteByCalendar(@Param("id") Long id);

    @Query(value="SELECT * FROM events e JOIN plannings p ON(p.id=e.calendar) JOIN users u ON (u.id=p.creator) WHERE u.id=:id "
            , nativeQuery = true)
    List<EventModel> findByCreatorForManager(@Param("id") Long creatorId);

    @Query(value = "SELECT u.id FROM events e JOIN plannings p ON(p.id=e.calendar) JOIN users u ON (u.id=p.creator) WHERE e.id=:id", nativeQuery = true)
    Long getEventPropriID(@Param("id") Long eventId);



}

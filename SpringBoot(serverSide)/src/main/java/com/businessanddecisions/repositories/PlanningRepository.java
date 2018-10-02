package com.businessanddecisions.repositories;

import com.businessanddecisions.models.EventModel;
import com.businessanddecisions.models.PlanningModel;
import com.businessanddecisions.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanningRepository extends JpaRepository<PlanningModel,Long> {
	PlanningModel findByCreator(UserModel creator);



	//PlanningModel findByFileUrl(String url);
	//int deleteByFileUrl(String url);
}

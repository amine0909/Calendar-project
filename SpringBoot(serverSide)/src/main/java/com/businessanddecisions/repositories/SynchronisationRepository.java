package com.businessanddecisions.repositories;

import com.businessanddecisions.models.SynchronisationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SynchronisationRepository extends JpaRepository<SynchronisationModel,Long> {
}

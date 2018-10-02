package com.businessanddecisions.repositories;

import com.businessanddecisions.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
     UserModel findByEmail(String email);
     List<UserModel> findByRoleNot(String role);
     //@Query(value = "SELECT new com.businessanddecisions.models.UserModel(u.id,u.firstName,u.lastName,u.email,u.role) FROM UserModel u")
     //List<UserModel> getAllUsersForAdmin();
     @Query(value = "SELECT * FROM users u WHERE u.id > :id LIMIT 10",nativeQuery = true)
     List<UserModel> getNextTenUsers(@Param("id") long id);

     @Query(value = "SELECT * FROM users u WHERE u.his_boss = (SELECT id FROM users uu WHERE uu.email = :email)",nativeQuery = true)
     List<UserModel> getManagerEmployees(@Param("email") String email);

     @Query(value = "SELECT * FROM users u WHERE u.id < :id LIMIT 10",nativeQuery = true)
     List<UserModel> getPreviousTenUsers(@Param("id") long id);


     @Query(value= "SELECT * FROM users WHERE role='employee' AND his_boss=:mId", nativeQuery = true)
     List<UserModel> getManagerEmployee(@Param("mId") Long mId);
}

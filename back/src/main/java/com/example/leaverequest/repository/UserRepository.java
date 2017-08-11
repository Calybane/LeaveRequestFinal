package com.example.leaverequest.repository;

import com.example.leaverequest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface UserRepository extends JpaRepository<User, String>
{
    User findByLogin(String login);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE USER u set u.DAYS_LEFT = u.DAYS_LEFT + 1 " +
                    "WHERE u.LOGIN in (SELECT r.LOGIN FROM LEAVE_REQUEST r " +
                    "WHERE r.LEAVE_FROM <= :holiday and r.LEAVE_TO >= :holiday)",
                    nativeQuery = true)
    void addOneDay(@Param("holiday") Date date);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE USER u set u.DAYS_LEFT = u.DAYS_LEFT - 1 " +
                    "WHERE u.LOGIN in (SELECT r.LOGIN FROM LEAVE_REQUEST r " +
                    "WHERE r.LEAVE_FROM <= :holiday and r.LEAVE_TO >= :holiday)",
                    nativeQuery = true)
    void removeOneDay(@Param("holiday") Date date);
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE USER u set u.DAYS_LEFT = u.DAYS_LEFT + :days WHERE u.LOGIN = :login", nativeQuery = true)
    void addDays(@Param("days") int days, @Param("login") String login);
}

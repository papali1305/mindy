package com.enspd.mindyback.repository;

import com.enspd.mindyback.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    public  User findByMail(@Param("mail") String mail);

    @Query("SELECT u FROM User u WHERE u.mail = :mail")
    public  User findMail(@Param("mail") String mail);

    public User findUtilisateurByMail(String mail);

}

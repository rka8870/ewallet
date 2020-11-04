/**
 * 
 */
package com.novopay.ewallet.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.novopay.ewallet.entity.User;

/**
 * @author User
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUserName (String userName);
}

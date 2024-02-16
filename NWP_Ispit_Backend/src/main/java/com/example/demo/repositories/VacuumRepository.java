package com.example.demo.repositories;

import com.example.demo.models.Vacuum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {

    @Query("SELECT v FROM Vacuum v WHERE " + "v.addedBy.email = :email AND v.isDeleted IS FALSE")
    List<Vacuum> findVacuumsForUser(@Param("email") String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(propagation = Propagation.MANDATORY)
    @Query("SELECT v FROM Vacuum v WHERE " + "v.id = :vacuumId " + "AND v.isDeleted IS FALSE")
    Optional<Vacuum> findVacuumForStart(@Param("vacuumId") Long vacuumId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(propagation = Propagation.MANDATORY)
    @Query("SELECT v FROM Vacuum v WHERE " + "v.id = :vacuumId " + "AND v.isDeleted IS FALSE")
    Optional<Vacuum> findVacuumForStop(@Param("vacuumId") Long vacuumId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(propagation = Propagation.MANDATORY)
    Optional<Vacuum> findVacuumById(@Param("vacuumId") Long vacuumId);
}

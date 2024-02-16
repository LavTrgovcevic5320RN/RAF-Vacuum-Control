package com.example.demo.repositories;

import com.example.demo.models.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    List<ErrorMessage> findAllByUser(@Param("user") String user);
}

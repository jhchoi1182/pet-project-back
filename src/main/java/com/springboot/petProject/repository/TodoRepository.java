package com.springboot.petProject.repository;

import com.springboot.petProject.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

    List<Todo> findAllByUserId(Integer userId);

}

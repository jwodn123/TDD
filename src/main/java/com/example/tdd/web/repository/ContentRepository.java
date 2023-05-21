package com.example.tdd.web.repository;

import com.example.tdd.web.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

}

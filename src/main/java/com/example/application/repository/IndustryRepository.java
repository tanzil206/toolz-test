package com.example.application.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.application.model.Industry;

@Repository
public interface IndustryRepository extends CrudRepository<Industry, Long> {

	public Industry findById(long id);

}

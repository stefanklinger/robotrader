package org.robotrader.core.repository;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository<T, ID extends Serializable> {

	T findById(ID id);

	List<T> findAll();

	T save(T entity);

	T delete(T entity);
}

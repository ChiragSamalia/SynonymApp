package com.Assignment.Synonym.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Assignment.Synonym.Dto.SynonymDTO;

@Repository
public interface SynonymRepo extends JpaRepository<SynonymDTO, String> {
	public SynonymDTO findByWordIgnoreCase(String word);


}

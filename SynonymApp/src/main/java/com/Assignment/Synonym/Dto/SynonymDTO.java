package com.Assignment.Synonym.Dto;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.Assignment.Synonym.Service.StringListConverter;

@Entity(name="Words")
@Table
public class SynonymDTO {

	@Id
	@Column(name = "Word", nullable = false)
	String word;

	@Convert(converter = StringListConverter.class)
	Set<String> synonym;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Set<String> getSynonym() {
		return synonym;
	}

	public void setSynonym(Set<String> synonym) {
		this.synonym = synonym;
	}
}

package com.Assignment.Synonym.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<Set<String>, String> {
	private static final String SPLIT_CHAR = ";";

	@Override
	public String convertToDatabaseColumn(Set<String> stringList) {
		return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
	}

	@Override
	public Set<String> convertToEntityAttribute(String string) {
		return string != null ? new HashSet<>(Arrays.asList(string.split(SPLIT_CHAR))) : new HashSet<>();
	}
}

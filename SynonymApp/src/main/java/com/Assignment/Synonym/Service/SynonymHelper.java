package com.Assignment.Synonym.Service;

import java.util.*;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.Assignment.Synonym.Dto.SynonymDTO;
import com.Assignment.Synonym.Repo.SynonymRepo;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SynonymHelper {

	@Autowired
	SynonymRepo Repository;
	private Map<String, Set<String>> synonymMap = new LinkedHashMap<>();

	@Transactional
	public String addSynoynm(SynonymDTO input) {
		System.out.println(input.getWord() + " " + input.getSynonym());
		List<SynonymDTO> list = new ArrayList<>();
		SynonymDTO WordPresent = Repository.findByWordIgnoreCase(input.getWord());
		SynonymDTO SynonymPresent = Repository.findByWordIgnoreCase((input.getSynonym().stream().findFirst().get()));
		if (WordPresent != null && SynonymPresent != null) {
			return "Word/Synonym both exists";
		}

		synonymMap.computeIfAbsent(input.getWord(), i -> new LinkedHashSet<>()).addAll(input.getSynonym());
		synonymMap.computeIfAbsent(input.getSynonym().stream().reduce("", (i, j) -> j), i -> new LinkedHashSet<>())
				.add(input.getWord());

		if (WordPresent != null || SynonymPresent != null) {
			propogateTransitive(WordPresent, SynonymPresent, input);
		}

		System.out.println(synonymMap);
		for (Map.Entry<String, Set<String>> it : synonymMap.entrySet()) {
			SynonymDTO savedObj = new SynonymDTO();
			savedObj.setWord(it.getKey());
			savedObj.setSynonym(it.getValue());
			list.add(savedObj);
		}

		List<SynonymDTO> result=Repository.saveAllAndFlush(list);
		if(result.equals(Collections.EMPTY_LIST) || result.isEmpty() || result==null)
		{
			return "Save Failed";
		}
		return "Save Successful";

	}

	@Transactional
	private void propogateTransitive(SynonymDTO WordDB, SynonymDTO SynonymDB, SynonymDTO input) {
		if (WordDB!= null) {
			Set<String> wordSynonymSet = WordDB.getSynonym();
			if (!wordSynonymSet.equals(Collections.EMPTY_SET) || !wordSynonymSet.isEmpty()) {
				for (String synm : wordSynonymSet) {
					if (!input.getSynonym().stream().reduce("", (i, j) -> j).equals(synm)) {
						synonymMap.computeIfAbsent(synm, i -> new LinkedHashSet<>()).addAll(input.getSynonym());

						System.out.println(input.getSynonym().stream().reduce("", (i, j) -> j));
						synonymMap.computeIfAbsent(input.getSynonym().stream().reduce("", (i, j) -> j),
								i -> new LinkedHashSet<>()).add(synm);
					}
				}
			}
		}
		if (SynonymDB!= null) {
			Set<String> wordSynonymSet = SynonymDB.getSynonym();
			if (!wordSynonymSet.equals(Collections.EMPTY_SET) || !wordSynonymSet.isEmpty()) {
				for (String synm : wordSynonymSet) {
					System.out.println(synm);
					if (!input.getSynonym().stream().reduce("", (i, j) -> j).equals(synm)) {
						System.out.println(input.getSynonym().stream().reduce("", (i, j) -> j));
						synonymMap.computeIfAbsent(synm, i -> new LinkedHashSet<>()).add(input.getWord());
						synonymMap.computeIfAbsent(input.getWord(), i -> new LinkedHashSet<>()).add(synm);
					}
				}
			}
		}
	}

	public SynonymDTO findByWord(String word) {
		return Repository.findByWordIgnoreCase(word);
	}

	@SuppressWarnings("null")
	@Transactional
	@Modifying
	public String deleteByWord(String word) {
		SynonymDTO SynonymDB = findByWord(word);
		if (SynonymDB == null)
			return null;
		Set<String> wordSynonymSet = SynonymDB.getSynonym();
		if (wordSynonymSet != null || !wordSynonymSet.isEmpty() || wordSynonymSet.equals(Collections.EMPTY_SET)
				|| wordSynonymSet.equals(Collections.emptySet())) {
			System.out.println("hi");
			for (String synm : wordSynonymSet) {
				SynonymDTO S = findByWord(synm);
				S.getSynonym().remove(word);
				Repository.saveAndFlush(S);
			}
		}
		Repository.deleteById(word);
		return "SUCCESS";
	}

	@Modifying
	@Transactional
	public String updateByWord(String oldWord, String newWord) {
		Set<String> wordSynonymSet = new HashSet<>();
		SynonymDTO Synonymnew = Repository.findByWordIgnoreCase(newWord);
		if (Synonymnew != null) {
			return "New Word already exists";
		}
		SynonymDTO SynonymDB = Repository.findByWordIgnoreCase(oldWord);
		SynonymDTO SynonymDBCopy = new SynonymDTO();
		if (SynonymDB == null)
			return null;
		wordSynonymSet = SynonymDB.getSynonym();
		if (wordSynonymSet != null) {
			for (String synm : wordSynonymSet) {
				SynonymDTO sam = Repository.findByWordIgnoreCase(synm);
				if (sam.getSynonym().contains(newWord)) {
					return "UPDATE_FAILED";
				}
				sam.getSynonym().remove(oldWord);
				sam.getSynonym().add(newWord);
				Repository.save(sam);
			}
		}
		BeanUtils.copyProperties(SynonymDB, SynonymDBCopy);
		SynonymDBCopy.setWord(newWord);
		Repository.delete(SynonymDB);
		Repository.save(SynonymDBCopy);
		return "SUCCESS";
	}
}

package com.Assignment.Synonym.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.Assignment.Synonym.Dto.SynonymDTO;
import com.Assignment.Synonym.Service.SynonymHelper;

@RestController
public class MainController {
	@Autowired
	SynonymHelper synonymHelper;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<?> Home() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/addSynonym", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> addNewSynonym(@RequestBody SynonymDTO synonymDTO) {
		long Result = synonymDTO.getSynonym().stream().filter(i -> i.equalsIgnoreCase(synonymDTO.getWord())).count();
		if (Result > 0) {
			return new ResponseEntity<String>("Bad Request Both Word and Synonym cannot be same",
					HttpStatus.BAD_REQUEST);
		}
		String result = synonymHelper.addSynoynm(synonymDTO);
		if (result == "Save Failed") {
			return new ResponseEntity<>("Timeout", HttpStatus.GATEWAY_TIMEOUT);
		} else if (result == "Word/Synonym both exists") {
			return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>("Created!", HttpStatus.CREATED);
		}
	}

	@RequestMapping(value = "/getSynonym", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody SynonymDTO getByWord(@RequestParam("word") String word) {
		SynonymDTO result = synonymHelper.findByWord(word);
		if (result == null) {
			return new SynonymDTO();
		} else {
			return result;
		}
	}

	@RequestMapping(value = "/deleteSynonym", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> deleteByWord(@RequestParam("word") String word) {
		String Result = synonymHelper.deleteByWord(word);
		if (Result == null)
			return new ResponseEntity<>("Given Word/Synonym " + word + " doesn't exist", HttpStatus.NOT_FOUND);
		return ResponseEntity.ok(Result);
	}

	@RequestMapping(value = "/updateSynonym", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> updateSynonym(@RequestParam("oldWord") String oldWord,
			@RequestParam("newWord") String newWord) {
		if (oldWord == null || newWord == null || oldWord == "" || newWord == "")
			return new ResponseEntity<String>("Bad Request New and Old Word/Synonym cannot be null or blank",
					HttpStatus.BAD_REQUEST);
		if (oldWord == newWord)
			return new ResponseEntity<String>("Bad Request New and Old Word/Synonym cannot be same",
					HttpStatus.BAD_REQUEST);

		String result = synonymHelper.updateByWord(oldWord, newWord);
		if (result == "New Word already exists") {
			return new ResponseEntity<>("New Word already exists", HttpStatus.BAD_REQUEST);
		}
		if (result == "SUCCESS") {
			return ResponseEntity.ok(result);

		} else if (result == null) {
			return new ResponseEntity<>("Given word/synonym " + "'" + oldWord + "'" + " is not available for Updation",
					HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("Given word/synonym " + "'" + oldWord + "'" + " is not available for Updation",
					HttpStatus.NOT_FOUND);
		}
	}

}

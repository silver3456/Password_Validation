package core;

import java.io.*;
import java.util.*;
import org.passay.*;
import org.passay.dictionary.*;
import org.passay.dictionary.sort.ArraysSort;

public class PasswordValidation {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		String password = "password";
		Properties p = new Properties();
		p.load(new FileInputStream("C:/workspace_11/PV/src/main/resources/messages.properties"));
		MessageResolver msg = new PropertiesMessageResolver(p);

		ArrayWordList awl = WordLists.createFromReader(
				new FileReader[] { new FileReader("C:/workspace_11/PV/src/main/resources/dictionary.txt") }, false,
				new ArraysSort());
		WordListDictionary dict = new WordListDictionary(awl);
		// DictionaryRule dictRule = new DictionaryRule(dict);
		DictionarySubstringRule dicSubRule = new DictionarySubstringRule(dict);
		dicSubRule.setMatchBackwards(true);

		CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule();
		rule.setNumberOfCharacteristics(3);

		rule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
		rule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
		rule.getRules().add(new CharacterRule(EnglishCharacterData.Alphabetical, 1));
		rule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
		rule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

		PasswordValidator validator = new PasswordValidator(msg,
				Arrays.asList(
						// Length between 8 and 16 characters
						new LengthRule(8, 16),
						// Define elements of N (Alphabetical, Digit, Upper,
						// Lower, Symbol)
						rule,
						// No whitespace
						new WhitespaceRule(),
						// Dictionary Rule
						dicSubRule));
		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			System.out.println("Valid password");
		}
		for (String m : validator.getMessages(result)) {
			System.err.println(m);
		}
	}
}

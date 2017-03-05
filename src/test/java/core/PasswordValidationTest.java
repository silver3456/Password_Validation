package core;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import java.io.*;
import java.util.*;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.DictionarySubstringRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.testng.*;
import org.testng.annotations.*;
import java.lang.reflect.Method;


public class PasswordValidationTest {
    String csvFile = "C:/workspace_11/PV/src/main/resources/passwords_validation.csv";
    private String test_name = "";
    public String getTestName() {return test_name;}
    private void setTestName(String a) {test_name = a;}

    @BeforeMethod(alwaysRun = true)
    public void bm(Method method, Object[] parameters) {
           setTestName(method.getName());
           Override a = method.getAnnotation(Override.class);
           String testCaseId = (String) parameters[a.id()];
           setTestName(testCaseId);}
    
    @DataProvider(name = "dp")
    public Iterator<String[]> a2d() throws InterruptedException, IOException {
           String cvsLine = "";
           String[] a = null;
           ArrayList<String[]> al = new ArrayList<>();
           BufferedReader br = new BufferedReader(new FileReader(csvFile));
           while ((cvsLine = br.readLine()) != null) {
                  a = cvsLine.split(";");al.add(a);}br.close();return al.iterator();}
    @Override
    @Test(dataProvider = "dp")
    public void test(String password) throws FileNotFoundException, IOException {
           assertThat(passwordValidate(password), equalTo(true));}
public static boolean passwordValidate(String password) throws FileNotFoundException, IOException {
           Properties p = new Properties();
           p.load(new FileInputStream("C:/workspace_11/PV/src/main/resources/messages.properties"));
          MessageResolver msg = new PropertiesMessageResolver(p);
           ArrayWordList awl = WordLists.createFromReader(
new FileReader[] {
new FileReader("C:/workspace_11/PV/src/main/resources/dictionary.txt") }, false, 
new ArraysSort());
           DictionarySubstringRule dicSubRule = 
new DictionarySubstringRule(
                           new WordListDictionary(awl));dicSubRule.setMatchBackwards(true);
           CharacterCharacteristicsRule rule = new CharacterCharacteristicsRule();
          rule.setNumberOfCharacteristics(5);
           rule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
           rule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
           rule.getRules().add(new CharacterRule(EnglishCharacterData.Alphabetical, 1));
           rule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
           rule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

          PasswordValidator validator = 
new PasswordValidator(msg,Arrays.asList(
new LengthRule(8, 16),rule,
new WhitespaceRule(),dicSubRule));
           RuleResult result = validator.validate(new PasswordData(password));
           if (result.isValid()) {System.out.println("Valid password");return true;}
           for (String m : validator.getMessages(result)) {System.err.println(m);}
           return false;
    }
}

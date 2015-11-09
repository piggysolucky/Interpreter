package classProject.interpreterModules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This Class defines a different set of tokens
 * */
public class Token {
	public static final String OpenParenthesis = "(";
	public static final String ClosingParenthesis = ")";
	public static final String Dot = ".";
	public static final String ERROR = "Not Valid Input!";
	public static final String End = "End of file !";
	public static final String NIL = "NIL";
	public static Set<String> arith = new HashSet<>(Arrays.asList("PLUS", "MINUS", "TIMES", "QUOTIENT", "REMAINDER", "LESS", "GREATER"));
	public static Set<String> buildIn = new HashSet<>(Arrays.asList("CAR", "CDR", "CONS", "ATOM", "EQ", "NULL", "INT", "PLUS", "MINUS",
			"TIMES", "QUOTIENT", "REMAINDER", "LESS", "GREATER", "COND", "QUOTE", "DEFUN"));
}

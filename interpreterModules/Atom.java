package classProject.interpreterModules;


public class Atom {
	//check if the input is an atom
	public static boolean isAtom (String s){
		return s.matches("[+-]?[1-9][0-9]*") ||s.matches("[A-Z][0-9A-Z]*") || s.matches("0");
	}
}

package logika.theoremprover;

import logika.model.Language;
import logika.model.Sequent;
import logika.model.ast.FormulaNode;
public class SequentCalculus {
	
	public static void forFormula(FormulaNode formula) {
	    Sequent seq = Sequent.forFormula(formula);
	}
	
	public static void forSequent(Sequent seq) {
	    
	}
	
	public static void forString(String sequentString, Language lang) {
	    
	}

}

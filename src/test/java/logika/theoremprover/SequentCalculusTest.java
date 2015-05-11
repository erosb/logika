package logika.theoremprover;

import logika.model.TestSupport;
import logika.model.ast.FormulaNode;

import org.junit.Test;

public class SequentCalculusTest {
	
	private TestSupport ts = TestSupport.forLang1();
	
	@Test(expected=IllegalArgumentException.class)
	public void forFormulaShouldOnlyAcceptImplications() {
		FormulaNode input = ts.parseFormula("and(false, true)");
		SequentCalculus.forFormula(input);
	}

}

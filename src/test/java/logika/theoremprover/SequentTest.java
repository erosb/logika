package logika.theoremprover;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import logika.model.Predicate;
import logika.model.TestSupport;
import logika.model.ast.FormulaNode;
import logika.model.ast.PredicateNode;

import org.junit.Test;

import static java.lang.String.format;
public class SequentTest {
	
	private TestSupport ts = TestSupport.forLang1();
	
	@Test(expected=IllegalArgumentException.class)
	public void forFormulaShouldOnlyAcceptImplication() {
		ts.parseFormula("and(true, false)");
		Sequent.forFormula(ts.parseFormula("and(true, false)"));
	}
	
	private static void assertCollectionEquals(Collection<FormulaNode> expected, Collection<FormulaNode> actual) {
		if (expected.size() != actual.size()) {
			throw new AssertionError(format("expected size: %d, actual size: %d", expected.size(), actual.size()));
		}
		for (FormulaNode a: actual) {
			if (!expected.contains(a)) {
				throw new AssertionError(format("unexpected actual formula %s", a.toString()));
			}
		}
		for (FormulaNode e: expected) {
			if (!actual.contains(e)) {
				throw new AssertionError(format("expected formula %s is not contained by actual collection", e.toString()));
			}
		}
	}
	
	private Collection<FormulaNode> parsePredicateList(String str) {
		String[] segments = str.split(" ");
		Collection<FormulaNode> rval = new ArrayList<>(segments.length);
		for (String pred: segments) {
			Predicate p = ts.lang().predicateByName(pred);
			rval.add(new PredicateNode(p, Collections.emptyList()));
		}
		return rval;
	}
	
	@Test
	public void premisesShouldBeListedForConjunctions() {
		Sequent seq = Sequent.forFormula(ts.parseFormula("impl(and(P, Q), false)"));
		assertCollectionEquals(parsePredicateList("P Q"), seq.premises());
	}
	
	@Test
	public void conclusionsShouldBeListedForDisjunctions() {
	    Sequent seq = Sequent.forFormula(ts.parseFormula("impl(true, or(P, Q))"));
	    assertCollectionEquals(parsePredicateList("P Q"), seq.conclusions());
	}

}

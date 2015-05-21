package logika.model;

import static java.lang.String.format;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import logika.model.ast.FormulaNode;
import logika.model.ast.PredicateNode;

import org.junit.Test;
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
	
	@Test
	public void testIsTerminal() {
	    Sequent seq = ts.parseSequent("P -> P, Q");
	    assertTrue(seq.isTerminal());
	    seq = ts.parseSequent("P -> Q, R");
	    assertFalse(seq.isTerminal());
	}
	
	
	@Test
	public void isTerminalShouldBeTrueForImplication() {
	    Sequent seq = ts.parseSequent("impl(P, Q) -> impl(P, Q), and(P, Q)");
	    assertTrue(seq.isTerminal());
	}
	
	@Test
	public void implWithDifferentArgsIsNotTerminal() {
	    Sequent seq = ts.parseSequent("impl(P, Q) -> impl(P, R), and(P, Q)");
        assertFalse(seq.isTerminal());
	}

}

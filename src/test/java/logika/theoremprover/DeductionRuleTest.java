package logika.theoremprover;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.Optional;

import logika.model.Sequent;
import logika.model.TestSupport;

import org.junit.Test;
public class DeductionRuleTest {
    
    private TestSupport ts = TestSupport.forLang1();
    
    private void assertMatcherResult(DeductionRule subject, String inputSeq, String... expectedSequents) {
        Sequent in = ts.parseSequent(inputSeq);
        Optional<List<Sequent>> actual = subject.apply(in);
        if (expectedSequents.length == 0) {
            assertFalse(actual.isPresent());
        } else {
            List<Sequent> actList = actual.get();
            assertEquals(expectedSequents.length, actList.size());
            for (int i = 0; i < expectedSequents.length; ++i) {
                Sequent expSeq = ts.parseSequent(expectedSequents[i]);
                Sequent actSeq = actList.get(i);
                assertEquals(expSeq, actSeq);
            }
        }
    }
    
    
    @Test
    public void implicationIn() {
        assertMatcherResult(DeductionRule.IMPLICATION_IN, "true -> impl(P, impl(Q, P))", "true, P -> impl(Q, P)");
    }
    
    @Test
    public void conjunctionOut() {
        assertMatcherResult(DeductionRule.CONJUNCTION_OUT, "and(P, Q), R -> S, false", "P, Q, R -> S, false");
    }
    
    @Test
    public void conjunctionIn() {
        assertMatcherResult(DeductionRule.CONJUNCTION_IN, "P -> Q, and(R, S)", "P -> Q, R", "P -> Q, S");
    }
    
    @Test
    public void disjunctionOut() {
        assertMatcherResult(DeductionRule.DISJUNCTION_OUT, "or(P, Q), true -> R", "P, true -> R", "Q, true -> R");
    }
    
    @Test
    public void disjunctionIn() {
        assertMatcherResult(DeductionRule.DISJUNCTION_IN, "P, true -> P, or(R, S)", "P, true -> P, R, S");
    }
    
    @Test    
    public void implicationOut() {
        assertMatcherResult(DeductionRule.IMPLICATION_OUT, "impl(P, Q), R -> false", "R -> false, P", "Q, R -> false");
    }
    
    @Test
    public void negationOut() {
        assertMatcherResult(DeductionRule.NEGATION_OUT, "not(P), Q -> R", "Q -> R, P");
    }
    
    @Test
    public void negationIn() {
        assertMatcherResult(DeductionRule.NEGATION_IN, "P -> Q, not(R)", "R, P -> Q");
    }

}

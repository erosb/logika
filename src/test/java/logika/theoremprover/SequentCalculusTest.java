package logika.theoremprover;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import logika.model.TestSupport;

import org.junit.Test;

public class SequentCalculusTest {
	
	private TestSupport ts = TestSupport.forLang1();
	
	private SequentCalculus subject(String sequent) {
	    return SequentCalculus.forString(new PrintingDeductionListener(), sequent, ts.lang());
	}
	
	@Test
	public void implicationRemoval() {
	    assertTrue(subject("true -> impl(P, impl(Q, P))").deduce());
	}
	

	@Test
	public void nonTheoremsShouldFailProperly() {
	    assertFalse(subject("P -> Q").deduce());
	}

}

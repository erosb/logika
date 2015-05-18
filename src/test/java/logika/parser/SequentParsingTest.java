package logika.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import logika.model.ast.FormulaNode;
import logika.theoremprover.Sequent;

import org.junit.Test;
public class SequentParsingTest {
    
    @Test
    public void test() {
        Sequent actual = subject("P -> Q").recognizeSequent();
        assertNotNull(actual);
        List<FormulaNode> premises = new ArrayList<>(actual.premises());
        List<FormulaNode> conclusions = new ArrayList<>(actual.conclusions());
        assertEquals("P", premises.get(0).toString());
        assertEquals("Q", conclusions.get(0).toString());
    }
    
    @Test
    public void multiplePremisesShouldBeRecognized() {
        Sequent actual = subject("P1(x, y), P -> false").recognizeSequent();
        assertEquals(2, actual.premises().size());
    }
    
    private Parser subject(final String input) {
        return Parser.forString(input, getClass().getResourceAsStream("/lang1.xml"));
    }

}

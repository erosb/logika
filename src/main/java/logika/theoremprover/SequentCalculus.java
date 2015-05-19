package logika.theoremprover;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import logika.model.Language;
import logika.model.Sequent;
import logika.model.ast.FormulaNode;
import logika.parser.Parser;

public class SequentCalculus {
    
    private static final List<DeductionRule> rules = Arrays.asList(
            DeductionRule.IMPLICATION_IN,
            DeductionRule.CONJUNCTION_OUT,
            DeductionRule.DISJUNCTION_IN,
            DeductionRule.NEGATION_IN,
            DeductionRule.NEGATION_OUT,
            DeductionRule.CONJUNCTION_IN,
            DeductionRule.DISJUNCTION_OUT,
            DeductionRule.IMPLICATION_OUT
    );
	
	public static SequentCalculus forFormula(DeductionListener listener, FormulaNode formula) {
	    return forSequent(listener, Sequent.forFormula(formula));
	}
	
	public static SequentCalculus forSequent(DeductionListener listener, Sequent seq) {
	    return new SequentCalculus(listener, null, seq);
	}
	
	public static SequentCalculus forString(DeductionListener listener, String sequentString, Language lang) {
	    Sequent seq = Parser.forString(sequentString, lang).recognizeSequent();
	    return forSequent(listener, seq);
	}
	
	private DeductionStep prevStep;
	
	private Sequent sequent;
	
	private DeductionListener listener;

    public SequentCalculus(DeductionListener listener, DeductionStep prevStep, Sequent sequent) {
        this.listener = listener;
        this.sequent = sequent;
    }
    
    public boolean deduce() {
        if (sequent.isTerminal()) {
            return true;
        }
        Optional<List<Sequent>> nextSequents = rules.stream()
        .map(matcher -> matcher.apply(sequent))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
        if (!nextSequents.isPresent()) {
            return false;
        }
        boolean rval = true;
        for (Sequent next: nextSequents.get()) {
            DeductionStep nextStep = new DeductionStep(prevStep, next);
            listener.accept(nextStep);
            rval &= new SequentCalculus(listener, nextStep, next).deduce();
        }
        return rval;
    }

}

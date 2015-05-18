package logika.theoremprover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import logika.model.Language;
import logika.model.Sequent;
import logika.model.ast.FormulaNode;
import logika.parser.Parser;
import logika.parser.TokenType;

public class SequentCalculus {
    
    private static interface Matcher extends Function<Sequent, Optional<Sequent>>{};
    
    private static final Matcher implicationIn = sequent -> {
        Optional<FormulaNode> implication = sequent.conclusions().stream()
            .filter(form -> form.is(TokenType.IMPL))
            .findFirst();
        if (implication.isPresent()) {
            List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size() + 1);
            newPremises.addAll(sequent.premises());
            newPremises.add(implication.get().getSubformula(0));
            List<FormulaNode> newConclusions = sequent.conclusions().stream()
                .map(form -> form == implication.get() ? implication.get().getSubformula(1) : form)
                .collect(Collectors.toList());
            return Optional.of(new Sequent(newPremises, newConclusions));
        } else {
            return Optional.empty();
        }
    };
    
    private static final List<Matcher> matchers = Arrays.asList(
    implicationIn  
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
        Optional<Sequent> next = matchers.stream()
        .map(matcher -> matcher.apply(sequent))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
        if (!next.isPresent()) {
            return false;
        }
        DeductionStep nextStep = new DeductionStep(prevStep, next.get());
        listener.accept(nextStep);
        return new SequentCalculus(listener, nextStep, next.get()).deduce();
    }

}

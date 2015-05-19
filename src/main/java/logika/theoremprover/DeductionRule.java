package logika.theoremprover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import logika.model.Sequent;
import logika.model.ast.FormulaNode;
import logika.parser.TokenType;

public interface DeductionRule extends Function<Sequent, Optional<List<Sequent>>> {

    public static final DeductionRule IMPLICATION_IN = sequent -> {
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
            return Optional.of(Arrays.asList(new Sequent(newPremises, newConclusions)));
        } else {
            return Optional.empty();
        }
    };

    public static final DeductionRule CONJUNCTION_OUT = sequent -> {
        Optional<FormulaNode> conjunction = sequent.premises().stream()
                .filter(form -> form.is(TokenType.AND))
                .findFirst();
        if (conjunction.isPresent()) {
            FormulaNode conjFormula = conjunction.get();
            List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size() + 2);
            newPremises.addAll(conjFormula.getSubformulas());
            newPremises.addAll(sequent.premises()
                    .stream()
                    .filter(form -> form != conjFormula)
                    .collect(Collectors.toList()));
            return Optional.of(Arrays.asList(new Sequent(newPremises, sequent.conclusions())));
        } else {
            return Optional.empty();
        }
    };
    
    public static final DeductionRule CONJUNCTION_IN = sequent -> {
        Optional<FormulaNode> result = sequent.conclusions().stream()
                .filter(form -> form.is(TokenType.AND))
                .findFirst();
        if (result.isPresent()) {
            FormulaNode conjunction = result.get();
            List<FormulaNode> remaining = sequent.conclusions().stream()
                    .filter(form -> form != conjunction)
                    .collect(Collectors.toList());
            List<FormulaNode> newConclusions = new ArrayList<>(remaining.size() + 1);
            newConclusions.addAll(remaining);
            newConclusions.add(conjunction.getSubformula(0));
            Sequent result1 = new Sequent(sequent.premises(), newConclusions);
            newConclusions = new ArrayList<>(remaining.size() + 1);
            newConclusions.addAll(remaining);
            newConclusions.add(conjunction.getSubformula(1));
            Sequent result2 = new Sequent(sequent.premises(), newConclusions);
            return Optional.of(Arrays.asList(result1, result2));
        } else {
            return Optional.empty();
        }
    };

    public static final DeductionRule DISJUNCTION_OUT = sequent -> {
        Optional<FormulaNode> result = sequent.premises().stream()
                .filter(form -> form.is(TokenType.OR))
                .findFirst();
        if (result.isPresent()) {
            FormulaNode disjunction = result.get();
            List<FormulaNode> remaining = sequent.premises().stream()
                    .filter(form -> form != disjunction)
                    .collect(Collectors.toList());
            List<FormulaNode> newPremises = new ArrayList<>(remaining.size() + 1);
            newPremises.add(disjunction.getSubformula(0));
            newPremises.addAll(remaining);
            Sequent result1 = new Sequent(newPremises, sequent.conclusions());
            newPremises = new ArrayList<>(remaining.size() + 1);
            newPremises.add(disjunction.getSubformula(1));
            newPremises.addAll(remaining);
            Sequent result2 = new Sequent(newPremises, sequent.conclusions());
            return Optional.of(Arrays.asList(result1, result2));
        } else {
            return Optional.empty();
        }
    };
    
    public static final DeductionRule DISJUNCTION_IN = sequent -> {
        Optional<FormulaNode> result = sequent.conclusions().stream()
                .filter(form -> form.is(TokenType.OR))
                .findFirst();
        if (result.isPresent()) {
            FormulaNode disjunction = result.get();
            List<FormulaNode> remaining = sequent.conclusions()
                    .stream().filter(form -> form != disjunction)
                    .collect(Collectors.toList());
            List<FormulaNode> newConclusions = new ArrayList<>(remaining.size() + 2);
            newConclusions.addAll(remaining);
            newConclusions.addAll(disjunction.getSubformulas());
            return Optional.of(Arrays.asList(new Sequent(sequent.premises(), newConclusions)));
        } else {
            return Optional.empty();
        }
    };
    
    public static final DeductionRule IMPLICATION_OUT = sequent -> {
        Optional<FormulaNode> result = sequent.premises().stream()
                .filter(form -> form.is(TokenType.IMPL))
                .findFirst();
        if (result.isPresent()) {
            FormulaNode impl = result.get();
            List<FormulaNode> remainingPremises = sequent.premises().stream()
                    .filter(form -> form != impl)
                    .collect(Collectors.toList());
            List<FormulaNode> newConclusions = new ArrayList<>(sequent.conclusions().size() + 1);
            newConclusions.addAll(sequent.conclusions());
            newConclusions.add(impl.getSubformula(0));
            Sequent result1 = new Sequent(remainingPremises, newConclusions);
            List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size());
            newPremises.add(impl.getSubformula(1));
            newPremises.addAll(remainingPremises);
            Sequent result2 = new Sequent(newPremises, sequent.conclusions());
            return Optional.of(Arrays.asList(result1, result2));
        } else {
            return Optional.empty();
        }
    };
    
    public static final DeductionRule NEGATION_OUT = sequent -> {
        Optional<FormulaNode> result = sequent.premises().stream()
                .filter(form -> form.is(TokenType.NOT))
                .findFirst();
        if (result.isPresent()) {
            FormulaNode negation = result.get();
            List<FormulaNode> remaining = sequent.premises().stream()
                    .filter(form -> form != negation)
                    .collect(Collectors.toList());
            List<FormulaNode> newConclusions = new ArrayList<>(sequent.conclusions().size() + 1);
            newConclusions.addAll(sequent.conclusions());
            newConclusions.add(negation.getSubformula(0));
            return Optional.of(Arrays.asList(new Sequent(remaining, newConclusions)));
        } else {
            return Optional.empty();
        }
    }; 
    
    public static final DeductionRule NEGATION_IN = sequent -> {
        Optional<FormulaNode> result = sequent.conclusions().stream()
                .filter(form -> form.is(TokenType.NOT))
                .findFirst();
        if (result.isPresent()) {
            FormulaNode negation = result.get();
            List<FormulaNode> remainingConclusions = sequent.conclusions().stream()
                    .filter(form -> form != negation)
                    .collect(Collectors.toList());
            List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size() + 1);
            newPremises.add(negation.getSubformula(0));
            newPremises.addAll(sequent.premises());
            return Optional.of(Arrays.asList(new Sequent(newPremises, remainingConclusions)));
        } else {
            return Optional.empty();
        }
    };

}

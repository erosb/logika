package logika.theoremprover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logika.model.Sequent;
import logika.model.ast.FormulaNode;
import logika.parser.TokenType;

public interface DeductionRule extends Function<Sequent, Optional<List<Sequent>>> {
    
    public static Predicate<FormulaNode> filterFor(TokenType tokenType) {
        return form -> form.is(tokenType);
    }
    
    public static DeductionRule onMatchIn(Function<Sequent, Collection<FormulaNode>> seqToList,
            Predicate<? super FormulaNode> filter,
            BiFunction<Sequent, FormulaNode, List<Sequent>> rewriter) {
        return sequent -> {
            Optional<FormulaNode> match = seqToList.apply(sequent)
                    .stream().filter(filter)
                    .findFirst();
            return match.map(m -> rewriter.apply(sequent, m));
        };
    }
    
    public static DeductionRule onMatchInPremises(Predicate<? super FormulaNode> filter,
            BiFunction<Sequent, FormulaNode, List<Sequent>> rewriter) {
        return onMatchIn(Sequent::premises, filter, rewriter);
    }
    
    public static DeductionRule onMatchInConclusions(Predicate<? super FormulaNode> filter,
            BiFunction<Sequent, FormulaNode, List<Sequent>> rewriter) {
        return onMatchIn(Sequent::conclusions, filter, rewriter);
    }

    public static final DeductionRule IMPLICATION_IN = onMatchInConclusions(
            filterFor(TokenType.IMPL),
            (sequent, impl) -> {
        List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size() + 1);
        newPremises.addAll(sequent.premises());
        newPremises.add(impl.getSubformula(0));
        List<FormulaNode> newConclusions = sequent.conclusions().stream()
                .map(form -> form == impl ? impl.getSubformula(1) : form)
                .collect(Collectors.toList());
        return Arrays.asList(new Sequent(newPremises, newConclusions));
    }) ;
            
    public static final DeductionRule CONJUNCTION_OUT = onMatchInPremises(
            filterFor(TokenType.AND),
            (sequent, conj) -> {
        List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size() + 2);
        newPremises.addAll(conj.getSubformulas());
        newPremises.addAll(sequent.premises()
                .stream()
                .filter(form -> form != conj)
                .collect(Collectors.toList()));
        return Arrays.asList(new Sequent(newPremises, sequent.conclusions()));
    }); 
            
    public static final DeductionRule CONJUNCTION_IN = onMatchInConclusions(
            filterFor(TokenType.AND),
            (sequent, conj) -> {
        List<FormulaNode> remaining = sequent.conclusions().stream()
                .filter(form -> form != conj)
                .collect(Collectors.toList());
        List<FormulaNode> newConclusions = new ArrayList<>(remaining.size() + 1);
        newConclusions.addAll(remaining);
        newConclusions.add(conj.getSubformula(0));
        Sequent result1 = new Sequent(sequent.premises(), newConclusions);
        newConclusions = new ArrayList<>(remaining.size() + 1);
        newConclusions.addAll(remaining);
        newConclusions.add(conj.getSubformula(1));
        Sequent result2 = new Sequent(sequent.premises(), newConclusions);
        return Arrays.asList(result1, result2);
    }); 
            
    public static final DeductionRule DISJUNCTION_OUT = onMatchInPremises(
            filterFor(TokenType.OR),
            (sequent, disj) -> {
                List<FormulaNode> remaining = sequent.premises().stream()
                        .filter(form -> form != disj)
                        .collect(Collectors.toList());
                List<FormulaNode> newPremises = new ArrayList<>(remaining.size() + 1);
                newPremises.add(disj.getSubformula(0));
                newPremises.addAll(remaining);
                Sequent result1 = new Sequent(newPremises, sequent.conclusions());
                newPremises = new ArrayList<>(remaining.size() + 1);
                newPremises.add(disj.getSubformula(1));
                newPremises.addAll(remaining);
                Sequent result2 = new Sequent(newPremises, sequent.conclusions());
                return Arrays.asList(result1, result2);
            }); 
            
    public static final DeductionRule DISJUNCTION_IN = onMatchInConclusions(
            filterFor(TokenType.OR),
            (sequent, disj) -> {
                List<FormulaNode> remaining = sequent.conclusions()
                        .stream().filter(form -> form != disj)
                        .collect(Collectors.toList());
                List<FormulaNode> newConclusions = new ArrayList<>(remaining.size() + 2);
                newConclusions.addAll(remaining);
                newConclusions.addAll(disj.getSubformulas());
                return Arrays.asList(new Sequent(sequent.premises(), newConclusions));
            });
            
    
    public static final DeductionRule IMPLICATION_OUT = onMatchInPremises(
            filterFor(TokenType.IMPL), 
            (sequent, impl) -> {
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
                return Arrays.asList(result1, result2);
            }); 
            
    public static final DeductionRule NEGATION_OUT = onMatchInPremises(
            filterFor(TokenType.NOT), 
            (sequent, negation) -> {
                List<FormulaNode> remaining = sequent.premises().stream()
                        .filter(form -> form != negation)
                        .collect(Collectors.toList());
                List<FormulaNode> newConclusions = new ArrayList<>(sequent.conclusions().size() + 1);
                newConclusions.addAll(sequent.conclusions());
                newConclusions.add(negation.getSubformula(0));
                return Arrays.asList(new Sequent(remaining, newConclusions));
            }); 
            
    public static final DeductionRule NEGATION_IN = onMatchInConclusions(
            filterFor(TokenType.NOT), 
            (sequent, negation) -> {
                List<FormulaNode> remainingConclusions = sequent.conclusions().stream()
                        .filter(form -> form != negation)
                        .collect(Collectors.toList());
                List<FormulaNode> newPremises = new ArrayList<>(sequent.premises().size() + 1);
                newPremises.add(negation.getSubformula(0));
                newPremises.addAll(sequent.premises());
                return Arrays.asList(new Sequent(newPremises, remainingConclusions));
            }); 
            
}

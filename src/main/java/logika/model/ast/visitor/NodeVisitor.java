package logika.model.ast.visitor;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.ConstantNode;
import logika.model.ast.FunctionNode;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

public interface NodeVisitor<R> {

    public R accumulate(R previous, R current);

    public R identity();

    public R visitBinaryOperator(BinaryOpNode node);

    public R visitConstant(ConstantNode node);

    public R visitFunction(FunctionNode node);

    public R visitPredicate(PredicateNode node);

    public R visitQuantifier(QuantifierNode node);

    public R visitUnaryOperator(UnaryOpNode node);

    public R visitVar(VarNode node);

}

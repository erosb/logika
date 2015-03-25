package logika.model.ast.visitor;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.ConstantNode;
import logika.model.ast.FunctionNode;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

public interface NodeVisitor<R> {

    public <E extends R> E visitBinaryOperator(BinaryOpNode node);

    public <E extends R> E visitConstant(ConstantNode node);

    public <E extends R> E visitFunction(FunctionNode node);

    public <E extends R> E visitPredicate(PredicateNode node);

    public <E extends R> E visitQuantifier(QuantifierNode node);

    public <E extends R> E visitUnaryOperator(UnaryOpNode node);

    public <E extends R> E visitVar(VarNode node);

}

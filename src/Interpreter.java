import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

class Interpreter extends AbstractParseTreeVisitor<Double> implements simpleCalcVisitor<Double> {

	private Namespace namespace;

	public Interpreter() {
		this.namespace	 = new Namespace();
	}

	private static Double boolToDouble(boolean value) {
		return value ? 1.0 : 0.0;
	}
	
	private static boolean doubleToBool(Double value) {
		return !value.equals(0.0);
	}

	@Override
	public Double visitStart_(simpleCalcParser.Start_Context ctx) {
		this.visit(ctx.sequence_());
		return null;
	}

	@Override
	public Double visitDeclaration(simpleCalcParser.DeclarationContext ctx) {
		this.namespace.setName(
			ctx.ID().getText(),
			this.visit(ctx.operation_())
		);
		return null;
	}

	@Override
	public Double visitSummator_(simpleCalcParser.Summator_Context ctx) {
		return null;
	}

	@Override
	public Double visitPartitioner_(simpleCalcParser.Partitioner_Context ctx) {
		return null;
	}

	@Override
	public Double visitRelation_comparison_(simpleCalcParser.Relation_comparison_Context ctx) {
		return null;
	}

	@Override
	public Double visitEquality_comparison_(simpleCalcParser.Equality_comparison_Context ctx) {
		return null;
	}

	@Override
	public Double visitPartition(simpleCalcParser.PartitionContext ctx) {
		switch (ctx.partitioner_().getText()) {
			case "*": return this.visit(ctx.operation_(0)) * this.visit(ctx.operation_(1));
			case "/": return this.visit(ctx.operation_(0)) / this.visit(ctx.operation_(1));
			default: throw new RuntimeException();
		}
	}

	@Override
	public Double visitOr(simpleCalcParser.OrContext ctx) {
		return Interpreter.boolToDouble(
			Interpreter.doubleToBool(this.visit(ctx.operation_(0)))
			|| Interpreter.doubleToBool(this.visit(ctx.operation_(1)))
		);
	}

	@Override
	public Double visitTernary(simpleCalcParser.TernaryContext ctx) {
		return
			Interpreter.doubleToBool(this.visit(ctx.operation_(1)))
			? this.visit(ctx.operation_(0))
			: this.visit(ctx.operation_(2));
	}

	@Override
	public Double visitParenthesis(simpleCalcParser.ParenthesisContext ctx) {
		return this.visit(ctx.operation_());
	}

	@Override
	public Double visitNot(simpleCalcParser.NotContext ctx) {
		return Interpreter.boolToDouble(
			!Interpreter.doubleToBool(
				this.visit(ctx.operation_())
			)
		);
	}

	@Override
	public Double visitIdentifier(simpleCalcParser.IdentifierContext ctx) {
		return this.namespace.getValueFromName(ctx.getText());
	}

	@Override
	public Double visitNumber(simpleCalcParser.NumberContext ctx) {
		return Double.parseDouble(ctx.getText());
	}

	@Override
	public Double visitRelationComparison(simpleCalcParser.RelationComparisonContext ctx) {
		switch (ctx.relation_comparison_().getText()) {
			case "<": return Interpreter.boolToDouble(
				this.visit(ctx.operation_(0))
				< this.visit(ctx.operation_(1))
			);
			case "<=": return Interpreter.boolToDouble(
				this.visit(ctx.operation_(0))
				<= this.visit(ctx.operation_(1))
			);
			case ">": return Interpreter.boolToDouble(
				this.visit(ctx.operation_(0))
				> this.visit(ctx.operation_(1))
			);
			case ">=": return Interpreter.boolToDouble(
				this.visit(ctx.operation_(0))
				>= this.visit(ctx.operation_(1))
			);
			default: throw new RuntimeException();
		}
	}

	@Override
	public Double visitAnd(simpleCalcParser.AndContext ctx) {
		return Interpreter.boolToDouble(
			Interpreter.doubleToBool(this.visit(ctx.operation_(0)))
			&& Interpreter.doubleToBool(this.visit(ctx.operation_(1)))
		);
	}

	@Override
	public Double visitNegativ(simpleCalcParser.NegativContext ctx) {
		return -this.visit(ctx.operation_());
	}

	@Override
	public Double visitSummation(simpleCalcParser.SummationContext ctx) {
		switch (ctx.summator_().getText()) {
			case "+": return this.visit(ctx.operation_(0)) + this.visit(ctx.operation_(1));
			case "-": return this.visit(ctx.operation_(0)) - this.visit(ctx.operation_(1));
			default: throw new RuntimeException();
		}
	}

	@Override
	public Double visitEqualityComparison(simpleCalcParser.EqualityComparisonContext ctx) {
		switch (ctx.equality_comparison_().getText()) {
			case "==": return Interpreter.boolToDouble(
				this.visit(ctx.operation_(0)).equals(
					this.visit(ctx.operation_(1))
				)
			);
			case "!=": return Interpreter.boolToDouble(
				!this.visit(ctx.operation_(0)).equals(
					this.visit(ctx.operation_(1))
				)
			);
			default: throw new RuntimeException();
		}
	}

	@Override
	public Double visitConditionIf(simpleCalcParser.ConditionIfContext ctx) {
		if (
			Interpreter.doubleToBool(
				this.visit(ctx.operation_())
			)
		) {
			this.visit(ctx.sequence_());
		}
		return null;
	}

	@Override
	public Double visitConditionIfElse(simpleCalcParser.ConditionIfElseContext ctx) {
		if (
			Interpreter.doubleToBool(
				this.visit(ctx.operation_())
			)
		) {
			this.visit(ctx.sequence_(0));
		} else {
			this.visit(ctx.sequence_(1));
		}
		return null;
	}

	@Override
	public Double visitWhile_(simpleCalcParser.While_Context ctx) {
		while (
			Interpreter.doubleToBool(
				this.visit(ctx.operation_())
			)
		){
			this.visit(ctx.sequence_());
		}
		return null;
	}

	@Override
	public Double visitPrint_(simpleCalcParser.Print_Context ctx) {
		System.out.println(this.visit(ctx.expression_()));
		return null;
	}

	@Override
	public Double visitExpression_(simpleCalcParser.Expression_Context ctx) {
		for (ParseTree child : ctx.children) {
			if (child != null) {
				return this.visit(child);
			}
		}
		return null;
	}

	@Override
	public Double visitEmptySequence(simpleCalcParser.EmptySequenceContext ctx) {
		return null;
	}

	@Override
	public Double visitSingleSequence(simpleCalcParser.SingleSequenceContext ctx) {
		this.visit(ctx.expression_());
		return null;
	}

	@Override
	public Double visitChainedSequence(simpleCalcParser.ChainedSequenceContext ctx) {
		this.visit(ctx.sequence_(0));
		this.visit(ctx.sequence_(1));
		return null;
	}

}

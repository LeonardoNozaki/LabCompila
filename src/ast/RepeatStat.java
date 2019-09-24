package ast;

import java.util.ArrayList;

public class RepeatStat extends Statement{
	public RepeatStat( Expr expr, ArrayList<Statement> stat ) {
		this.expr = expr;
		this.stat = stat;
	}

	public void genC( PW pw ) {
		
	}

	private Expr expr;
	private ArrayList<Statement> stat = new ArrayList<Statement>();

}

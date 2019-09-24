package ast;

import java.util.ArrayList;

public class WhileStat extends Statement{
	public WhileStat( Expr expr, ArrayList<Statement> stat ) {
		this.expr = expr;
		this.stat = stat;
	}

	public void genC( PW pw ) {
		
	}

	private Expr expr;
	private ArrayList<Statement> stat = new ArrayList<Statement>();
}

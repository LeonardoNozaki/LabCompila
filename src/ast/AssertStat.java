package ast;

import java.util.ArrayList;

public class AssertStat extends Statement{
	public AssertStat( Expr expr, String message ) {
		this.expr = expr;
		this.message = message;
	}

	public void genC( PW pw ) {
		
	}

	private Expr expr;
	private String message;
}

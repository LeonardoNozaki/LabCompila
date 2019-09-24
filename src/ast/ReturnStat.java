package ast;

import java.util.ArrayList;

public class ReturnStat extends Statement{
	public ReturnStat( Expr expr ) {
		this.expr = expr;
	}

	public void genC( PW pw ) {
		
	}

	private Expr expr;
}

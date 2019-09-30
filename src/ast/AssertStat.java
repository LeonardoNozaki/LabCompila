package ast;

import java.util.ArrayList;

public class AssertStat extends Statement{
	public AssertStat( Expr expr, String message ) {
		this.expr = expr;
		this.message = message;
	}
	
	public void genJava(PW pw) {
		pw.printIdent("if(!( " );
		this.expr.genJava(pw);
		pw.println(" )){");
		pw.add();
		pw.printlnIdent("System.out.print(" + message + ");");
		pw.sub();
		pw.printlnIdent("}");
	}

	public void genC( PW pw ) {
		
	}

	private Expr expr;
	private String message;
}

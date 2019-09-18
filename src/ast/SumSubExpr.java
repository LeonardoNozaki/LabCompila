package ast;
import lexer.*;
import java.util.*;

public class SumSubExpr {
	public SumSubExpr(ArrayList<Term> expr, ArrayList<Token> op) {
		this.expr = expr;
		this.op = op;
	}
	
	private ArrayList<Term> expr;
	private ArrayList<Token> op;
	
}

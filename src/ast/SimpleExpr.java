package ast;
import lexer.*;
import java.util.*;

public class SimpleExpr {
	public SimpleExpr(ArrayList<SumSubExpr> expr) {
		this.expr = expr;
	}
	
	private ArrayList<SumSubExpr> expr;
}

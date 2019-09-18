package ast;
import lexer.*;
import java.util.*;

public class Term {
	public Term(ArrayList<SignalFactor> expr, ArrayList<Token> op) {
		this.expr = expr;
		this.op = op;
	}
	
	private ArrayList<SignalFactor> expr;
	private ArrayList<Token> op;
	
}

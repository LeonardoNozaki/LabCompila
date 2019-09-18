package ast;
import lexer.*;

public class SignalFactor {
	public SignalFactor(Token op, Factor factor) {
		this.op = op;
		this.factor = factor;
	}
	
	private Token op;
	private Factor factor;
}

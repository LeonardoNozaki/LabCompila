package ast;

public class LocalDec extends Statement{
	public LocalDec(Variable variable) {
		this.variable = variable;
	}
	
	@Override
	public void genC(PW pw) {
		
	}
	
	private Variable variable;
}

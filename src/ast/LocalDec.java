package ast;

public class LocalDec extends Statement{
	public LocalDec(Variable variable) {
		this.variable = variable;
	}
	
	@Override
	public void genC(PW pw) {
		
	}
	
	public void genJava(PW pw) {
		this.variable.genJava(pw);
	}
	
	private Variable variable;
}

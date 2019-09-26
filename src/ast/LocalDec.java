package ast;

abstract public class LocalDec extends Statement{
	abstract public void genC(PW pw);
	abstract public void genJava(PW pw);
	abstract public Type getType();
}

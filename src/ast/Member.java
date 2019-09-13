package ast;

abstract public class Member {
	abstract public void genC( PW pw );
	abstract public void genJava(PW pw);
}

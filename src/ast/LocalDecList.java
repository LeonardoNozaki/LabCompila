package ast;
import java.util.*;

public class LocalDecList extends Variable{
	public LocalDecList(Type type, ArrayList<String> id) {
		this.id = id;
		this.type = type;
	}
	
	public void genC(PW pw) {

	}
	public void genJava(PW pw) {
		
	}
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	private Type type;
	private ArrayList<String> id;
}

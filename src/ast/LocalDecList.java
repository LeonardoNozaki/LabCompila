package ast;
import java.util.*;

public class LocalDecList extends Variable{
	public LocalDecList(Type type, ArrayList<Variable> id) {
		this.id = id;
		this.type = type;
		this.init = false;
	}
	
	public boolean getInit() {
		return this.init;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	public void genC(PW pw) {

	}
	public void genJava(PW pw) {
		pw.printIdent(this.type.getJavaname() + " ");
		this.id.get(0).genJava(pw);
		for(int i = 1; i < this.id.size(); i++) {
			pw.print(", ");
			this.id.get(i).genJava(pw);
		}
		pw.println(";");
	}
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	private Type type;
	private ArrayList<Variable> id;
	private boolean init;
}

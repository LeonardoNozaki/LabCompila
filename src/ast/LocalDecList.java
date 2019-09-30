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
		pw.printIdent(this.type.getJavaname() + " " + this.id.get(0));
		for(int i = 1; i < this.id.size(); i++) {
			pw.print(", " + this.id.get(i));
		}
		pw.println(";");
	}
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	private Type type;
	private ArrayList<String> id;
}

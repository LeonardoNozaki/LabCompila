/* ==========================================================================
 * Universidade Federal de Sao Carlos - Campus Sorocaba
 * Disciplina: Laboratorio de Compiladores
 * Prof. Jose Guimaraes
 *
 * Trabalho de Laboratorio de Compiladores - The Cianeto Language
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluno: Leonardo Nozaki   RA: 743561
 * ========================================================================== */

package ast;
import java.util.*;
/*
 * Krakatoa Class
 */
public class TypeCianetoClass extends Type{

   public TypeCianetoClass( String name) {
      super(name);
   }

   public void setSuperClass(TypeCianetoClass superclass) {
	   this.superclass = superclass;
   }
   
   public void setOpen(boolean open) {
	   this.open = open;
   }
   
   public boolean getOpen() {
	   return open;
   }
   
   public String getCname() {
      return getName();
   }
   
   public String getJavaname() {
      return getName();
   }

   public void addField(FieldDec f) {
	   fieldList.add(f);
   }
   
   public void addMethodPublic(MethodDec method) {
	   publicMethodList.add(method);
   }
   
   public void addMethodPrivate(MethodDec method) {
	   privateMethodList.add(method);
   }
   
   public void genJava(PW pw) {
	   pw.println("");
		pw.printlnIdent("class " + this.getJavaname() + " ");
		if(superclass != null) {
			pw.println("extends" + this.superclass.getJavaname() + "{");
		}
		else {
			pw.println("{");
		}
		pw.add();
		for(int i = 0; i < this.fieldList.size(); i++) {
			this.fieldList.get(i).genJava(pw);
		}
		for(int i = 0; i < this.publicMethodList.size(); i++) {
			this.publicMethodList.get(i).genJava(pw);
		}
		for(int i = 0; i < this.privateMethodList.size(); i++) {
			this.privateMethodList.get(i).genJava(pw);
		}
		pw.sub();
		pw.printlnIdent("}");
   }
   
   private boolean open;
   private TypeCianetoClass superclass = null;
   private ArrayList<FieldDec> fieldList = new ArrayList<FieldDec>();
   private ArrayList<MethodDec> publicMethodList = new ArrayList<MethodDec>();
   private ArrayList<MethodDec> privateMethodList = new ArrayList<MethodDec>();
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}

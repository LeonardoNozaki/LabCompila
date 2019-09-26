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
public class TypeCianetoClass {

   public TypeCianetoClass( String name ) {
      this.name = name;
   }
   
   public TypeCianetoClass( String name, String superclass ) {
	      this.name = name;
	      this.superclass = new TypeCianetoClass(superclass);
   }

   public String getCname() {
      return name;
   }
   
   public String getJavaname() {
      return name;
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
   
   private String name;
   private TypeCianetoClass superclass = null;
   private ArrayList<FieldDec> fieldList = new ArrayList<FieldDec>();
   private ArrayList<MethodDec> publicMethodList = new ArrayList<MethodDec>();
   private ArrayList<MethodDec> privateMethodList = new ArrayList<MethodDec>();
   // métodos públicos get e set para obter e iniciar as variáveis acima,
   // entre outros métodos
}

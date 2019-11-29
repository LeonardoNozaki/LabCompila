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
import comp.CompilationError;

public class Program {

	public Program(ArrayList<TypeCianetoClass> classList, ArrayList<MetaobjectAnnotation> metaobjectCallList, 
			       ArrayList<CompilationError> compilationErrorList) {
		this.classList = classList;
		this.metaobjectCallList = metaobjectCallList;
		this.compilationErrorList = compilationErrorList;
	}

	public void setMainJavaClassName(String mainJavaClassName) {
		this.mainJavaClassName = mainJavaClassName;
	}

	/**
	the name of the main Java class when the
	code is generated to Java. This name is equal
	to the file name (without extension)
	*/
		
	public void genJava(PW pw) {
		pw.printlnIdent("import java.util.Scanner;");
		pw.println("");
		pw.printlnIdent("public class " + this.mainJavaClassName + "{");
		pw.add();
		pw.printlnIdent("public static void main(String []args) {");
		pw.add();
		pw.printlnIdent("new Program().run();");
		pw.sub();
		pw.printlnIdent("}");
		pw.sub();
		pw.printlnIdent("}");
		for(int i = 0; i < this.classList.size(); i++) {
			this.classList.get(i).genJava(pw);
		}
	}

	public void genC(PW pw) {
		pw.printlnIdent("#include <malloc.h>");
		pw.printlnIdent("#include <stdlib.h>");
		pw.printlnIdent("#include <stdio.h>");
		pw.printlnIdent("#include <string.h>");
		pw.println("");
		
		pw.printlnIdent("typedef int boolean;");
		pw.printlnIdent("#define true 1");
		pw.printlnIdent("#define false 0");
		pw.println("");
		
		pw.printlnIdent("int readInt() {");
		pw.add();
		pw.printlnIdent("int n;");
		pw.printlnIdent("char __s[512];");
		pw.printlnIdent("gets(__s);");
		pw.printlnIdent("sscanf(__s, \"%d\", &_n);");
		pw.printlnIdent("return n;");
		pw.sub();
		pw.printlnIdent("}");
		pw.println("");
		
		pw.printlnIdent("char *readString() {");
		pw.add();
		pw.printlnIdent("char s[512];");
		pw.printlnIdent("gets(s);");
		pw.printlnIdent("char *ret = malloc(strlen(s) + 1);");
		pw.printlnIdent("strcpy(ret, s);");
		pw.printlnIdent("return ret;");
		pw.sub();
		pw.printlnIdent("}");
		pw.println("");
		
		pw.printlnIdent("char *strInitCat() {");
		pw.add();
		pw.printlnIdent("char *s = malloc(2000);");
		pw.printlnIdent("s[0] = '\0';");
		pw.printlnIdent("return s;");
		pw.sub();
		pw.printlnIdent("}");
		pw.println("");
		
		pw.printlnIdent("char *toStr(int a) {");
		pw.add();
		pw.printlnIdent("char *s = malloc(11);");
		pw.printlnIdent("sprintf(s, \"%d\", a);");
		pw.printlnIdent("return s;");
		pw.sub();
		pw.printlnIdent("}");
		pw.println("");
		
		pw.printlnIdent("typedef void (*Func)();");
		pw.println("");
		
		for(int i = 0; i < this.classList.size(); i++) {
			this.classList.get(i).genC(pw);
			pw.println("");
		}
		
		pw.printlnIdent("int main() {");
		pw.add();
		pw.printlnIdent("_class_Program *program;");
		pw.printlnIdent("program = new_Program();");
		pw.printlnIdent("_Program_run(program);");
		pw.printlnIdent("return 0;");
		pw.sub();
		pw.printlnIdent("}");
	}
	
	public ArrayList<TypeCianetoClass> getClassList() {
		return classList;
	}


	public ArrayList<MetaobjectAnnotation> getMetaobjectCallList() {
		return metaobjectCallList;
	}
	

	public boolean hasCompilationErrors() {
		return compilationErrorList != null && compilationErrorList.size() > 0 ;
	}

	public ArrayList<CompilationError> getCompilationErrorList() {
		return compilationErrorList;
	}

	private String mainJavaClassName;
	private ArrayList<TypeCianetoClass> classList;
	private ArrayList<MetaobjectAnnotation> metaobjectCallList;
	
	ArrayList<CompilationError> compilationErrorList;

	
}
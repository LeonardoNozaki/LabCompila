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

package comp;

import java.util.*;

public class SymbolTable {
  public SymbolTable() {
    classTable = new Hashtable<String, Object>();
    localTable = new ArrayList< Hashtable<String, Object> >();
  }

  public void putInClass( String key, Object value ) {
    classTable.put(key, value);
  }

  public void putInLocal( String key, Object value ) {
    int size = localTable.size();
    if(size > 0){
      aux = localTable.get(size-1);
      aux.put(key, value);
    }
  }

  public void add(){
    localTable.add(new Hashtable<String, Object>());
  }

  public void sub(){
    int size = localTable.size();
    if(size > 0){
      localTable.remove(size-1);
    }
  }

  public Object getInLocal( Object key ) {
    int size = localTable.size();
    for(int i = size-1; i >= 0; i--){
      aux = localTable.get(i);
      Object o = aux.get(key);
      if(o != null){
        return o;
      }
    }
    return null;
  }

  public Object getInClass( Object key ) {
    return classTable.get(key);

  }

  public Object get( String key ) {
    // returns the object corresponding to the key.
    Object result;
    if ( (result = getInLocal(key)) != null ) {
      // found local identifier
      return result;
    }
    else {
    // global identifier, if it is in globalTable
      return classTable.get(key);
    }
  }

  public void removeLocalIdent() {
    // remove all local identifiers from the table
    localTable.clear();
  }

  private Hashtable<String, Object> classTable;
  private Hashtable<String, Object> aux;
  private ArrayList<Hashtable<String, Object> > localTable;
}

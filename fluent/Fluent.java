/*
 * The {@code Fluent} class implements an Event Calculus fluent. A fluent is
 * a condition that varies over time.
 */

package fluent;

/**
 *
 * @author jmb
 */
public class Fluent<E> {
    public Fluent(E anObj){
        myObj = anObj;
    }
    E myObj;
    public E getObj(){
        return myObj;
    }
    public String type(){
        return this.myObj.getClass().getName();
    }
}

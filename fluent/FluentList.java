/*
 * A lsit of fluents
 */
package fluent;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author jmb
 */
public class FluentList implements FluentContainer {

    Vector<Fluent> myFluents;

    public FluentList() {
        myFluents = new Vector<Fluent>();
    }

    /**
     * Adds a fluent
     * @param fIn is the fluent to add
     */
    public void addFluent(Fluent fIn) {
        myFluents.add(fIn);
    }



    /**
     * Returns the first fluent of the given type
     * @param fIn is the fluent to add
     * @return the fluent or null
     */
    public Fluent getFirstFluent(String type){
        Iterator<Fluent> it = myFluents.iterator();
        int i = -1;
        boolean cont = true;
        Fluent f = null;
        synchronized (myFluents) {
            while (cont && it.hasNext()) {
                f = it.next();
                if (f.type().equals(type)) {
                    cont = false;
                } else {
                    i++;
                }
            }
        }
        return f;
    }

    /**
     * Removes a fluent
     * @param fIn is the fluent to remove
     */
    public void removeFluent(Fluent fIn) {
        myFluents.remove(fIn);
    }

    /**
     * Removes the first fluent of the given type
     * @param type is the type of fluent to remove
     * @return the fluent if found, or else null
     */
    public Fluent removeFirstFluent(String type) {
        Iterator<Fluent> it = myFluents.iterator();
        int i = 0;
        boolean cont = true;
        Fluent f = null;
        synchronized (myFluents) {
            while (cont && it.hasNext()) {
                f = it.next();
                if (f.type().equals(type)) {
                    cont = false;
                    myFluents.remove(i);
                } else {
                    i++;
                }
            }
        }
        return f;
    }

    /**
     * Removes all the fluents of the given type
     * @param type is the type of fluent to remove
     */
    public void removeAllFluentsOfType(String type) {
        Iterator<Fluent> it = myFluents.iterator();
        int i = 0;
        Vector<Integer> indices = new Vector<Integer>();
        synchronized (myFluents) {
            while (it.hasNext()) {
                Fluent f = it.next();
                if (f.type().equals(type)) {
                    indices.add(i);
                }
                i++;
            }
            Iterator<Integer> it2 = indices.iterator();
            int remNum = 0;
            while(it2.hasNext()){
                myFluents.remove(it2.next() - remNum++);
            }
        }
    }

    /**
     * Removes all the fluents
     * @param type is the type of fluent to remove
     */
    public void removeAllFluents(){
        myFluents.removeAllElements();
    }

    /**
     * Returns the number of elements in the containrt
     * @return the number of elements in the containrt
     */
    public int size(){
        return myFluents.size();
    }

    public static void main(String args[]){
        FluentList fl = new FluentList();
        String f1 = "hello";
        int f2 = 1234;

        fl.addFluent(new Fluent(f1));
        fl.addFluent(new Fluent(f2));

        System.out.println(fl.size());
        fl.removeFirstFluent(f1.getClass().getName());
        System.out.println(fl.size());

    }
}

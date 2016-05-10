package org.eltech.associationrules;

import java.util.HashMap;
import java.util.List;

import org.eltech.ddm.associationrules.ItemSet;


public class ItemSetsHashMap extends HashMap<List<String>, Integer>{

	private static final long serialVersionUID = 1L;

	public ItemSetsHashMap() {
        super();
    }

    public ItemSetsHashMap(ItemSet itemset) {
        super();
//        put(itemset.getItemIDList(), itemset);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (List<String> s : this.keySet()) {
        	b.append(s);
        }
        return b.toString();
    }

//    public boolean contains(Object o) {
//    	boolean contain = false;
//    	for (List<String> key : this.keySet()) {
//    		if (this.get(key).equals(o)) { 
//    			contain = true;
//    		} else {
//    			contain = false;
//    		}
//    	}
//    	return contain;
//    }
}
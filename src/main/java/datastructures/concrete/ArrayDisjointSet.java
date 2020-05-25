package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import misc.exceptions.NotYetImplementedException;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<T, Integer> nodeInventory;
    private final int default_size = 10;
    private int size = 0;
    
    public ArrayDisjointSet() {
        this.pointers = new int[default_size];
        this.nodeInventory = new ChainedHashDictionary<T, Integer>();
    }

    @Override
    public void makeSet(T item) {
        if (nodeInventory.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        
        if (size + 1 >= this.pointers.length) {
            resize();
        }
        int index = this.size;
        int rank = 0;
        this.nodeInventory.put(item, index);
        this.pointers[index] = -1 * rank -1;
        this.size += 1;
    }
    
    private void resize() {
        int temp[] = new int[pointers.length * 2];
        for (int i = 0; i < pointers.length; i++) {
            temp[i] = pointers[i];
        }
        this.pointers = temp;
    }
    
    @Override
    public int findSet(T item) {
        if (!this.nodeInventory.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = this.nodeInventory.get(item);
        while (this.pointers[index] >= 0) {
            index = this.pointers[index];
        }
        return index;
    }

    @Override
    public void union(T item1, T item2) {
        if (!this.nodeInventory.containsKey(item1) || !this.nodeInventory.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        
        int set1 = findSet(item1);
        int set2 = findSet(item2);
        if (set1 == set2) {
            throw new IllegalArgumentException();
        }
        
        int rank1 = pointers[set1];
        int rank2 = pointers[set2];
        
        if (rank1 > rank2) {
            this.pointers[set1] = set2;
        } else if (rank1 < rank2) {
            this.pointers[set2] = set1;
        } else {
            this.pointers[set2] = set1;
            this.pointers[set1] -= 1;
        }
        
        
    }
}

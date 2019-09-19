
import java.util.*;

// ============  THE MAIN METHOD WITH TWO TESTS.  ==============
// ============  DON'T MODIFY THE TEST METHODS   ===============
// ============  COMPLETE ONLY THE containedIn METHOD ==========

public class GenericIterators {

public static void main(String[] args) {
	
	test1();
	System.out.println();
	test2();
}

static void test1() {
	
	AbsTree<Integer> set1 = new Tree<Integer>(100);
	set1.insert(50);
	set1.insert(50); 
	set1.insert(25);
	set1.insert(75);
	set1.insert(75);
	set1.insert(150);
	set1.insert(125);
	set1.insert(200);
	set1.insert(100);
	set1.insert(201);
	
	
	AbsTree<Integer> set2 = new Tree<Integer>(100);
	set2.insert(150);
	set2.insert(125);
	set2.insert(50);
	set2.insert(50);
	set2.insert(26);
	set2.insert(25);
	set2.insert(27);
	set2.insert(75);
	set2.insert(75);
	set2.insert(76);
	set2.insert(150);
	set2.insert(125);
	set2.insert(200);
	
	
	
	System.out.print("set1 = "); print(set1);
	System.out.print("set2 = "); print(set2);
	
	if (containedIn(set1, set2))
		System.out.println("set1 is contained in set2.");
	else
		System.out.println("set1 is not contained in set2.");
}


static void test2() {
	
	AbsTree<Integer> bag1 = new DupTree<Integer>(100);
	bag1.insert(50);
	bag1.insert(50);
	bag1.insert(25);
	bag1.insert(75);
	bag1.insert(75);
	bag1.insert(150);
	bag1.insert(125);
	bag1.insert(200);
	bag1.insert(100);
	
	AbsTree<Integer> bag2 = new DupTree<Integer>(100);
	bag2.insert(150);
	bag2.insert(125);
	bag2.insert(50);
	bag2.insert(50);
	bag2.insert(26);
	bag2.insert(25);
	bag2.insert(27);
	bag2.insert(75);
	bag2.insert(75);
	bag2.insert(76);
	bag2.insert(150);
	bag2.insert(125);
	bag2.insert(200);
	
	System.out.print("bag1 = "); print(bag1);
	System.out.print("bag2 = "); print(bag2);
	
	if (containedIn(bag1, bag2))
		System.out.println("bag1 is contained in bag2.");
	else
		System.out.println("bag1 is not contained in bag2.");
}


static void print(AbsTree<Integer> bs) {
	System.out.print("{ ");
	for (int x : bs) 
		System.out.print(x + " ");
	System.out.println("}");
}


static <T extends Comparable<T>> boolean containedIn(AbsTree<T> tr1, AbsTree<T> tr2) {
	Iterator<T> iter1 = tr1.iterator();
	Iterator<T> iter2 = tr2.iterator();
	
	int end=0;
	while (iter1.hasNext()) {
		T a=iter1.next();
		while(iter2.hasNext())
		{
			T b=iter2.next();
			if(a.compareTo(b)< 0){
				System.out.println(a+"< "+b); 
				return false;
			}
			else if(a.compareTo(b)> 0){
				System.out.println(a+" > "+b);
				if(end !=1)
					return false;
					
			}
			else if(a.compareTo(b)== 0){
				System.out.println(a+" = "+b);
				end=1;
				break;
			}
			
		}
	  }
	return !iter1.hasNext() && !iter2.hasNext();
	
}

}


//========= GENERIC ABSTREE, TREE, AND DUPTREE (DON'T MODIFY THESE CLASSES)


abstract class AbsTree<T extends Comparable<T>> implements Iterable<T> {

	public AbsTree(T v) {
		value = v;
		left = null;
		right = null;
	}
	public boolean done() {
		return (value == null);
	}
	
	public void insert(T v) {
		if (value.compareTo(v) == 0)
			count_duplicates();
		if (value.compareTo(v) > 0)
			if (left == null)
				left = add_node(v);
			else
				left.insert(v);
		else if (value.compareTo(v) < 0)
			if (right == null)
				right = add_node(v);
			else
				right.insert(v);
	}

	public Iterator<T> iterator() {
		return create_iterator();
	}

	protected abstract AbsTree<T> add_node(T n);
	protected abstract void count_duplicates();
	protected abstract int get_count();
	protected abstract Iterator<T> create_iterator();
	
	protected T value;
	protected AbsTree<T> left;
	protected AbsTree<T> right;
}


class Tree<T extends Comparable<T>> extends AbsTree<T> {
	public Tree(T n) {
		super(n);
	}
	
	public Iterator<T> create_iterator() {
		return new AbsTreeIterator<T>(this);
	}

	protected AbsTree<T> add_node(T n) {
		return new Tree<T>(n);
	}

	protected void count_duplicates() {
		;
	}
	
	protected int get_count() {
		return 1;
	}
}


class DupTree<T extends Comparable<T>> extends AbsTree<T> {
	public DupTree(T n) {
		super(n);
		count = 1;
	};

	public Iterator<T> create_iterator() {
		return new AbsTreeIterator<T>(this);   
	}
	
	protected AbsTree<T> add_node(T n) {
		return new DupTree<T>(n);
	}

	protected void count_duplicates() {
		count++;
	}
	
	protected int get_count() {
		return count;
	}

	protected int count;
}




// ========  GENERIC TREE ITERATORS (COMPLETE THE OUTLINES) =========

 

class AbsTreeIterator<T extends Comparable<T>> implements Iterator<T> {

public AbsTreeIterator(AbsTree<T> root) {

	stack_left_spine(root);
}

public boolean hasNext() {
	if(stack.isEmpty())
		return false;
	else
		return true;
}

public T next() {
	Pair<T> pop= stack.pop();
	AbsTree<T> val=pop.node;
	int count=pop.count;
	
	if(count>1)
	{
		pop.count--;
		stack.push(pop);
		return val.value;
	}
	else
	{
		if(pop.node.right!=null)
		{
			stack_left_spine(pop.node.right);
		}
		return val.value;
	}
}

private void stack_left_spine(AbsTree<T> node) {
	AbsTree<T> treeroot= node;
	Pair<T> pair= new Pair(treeroot);
	stack.push(pair);
	
	while(pair.node.left!=null)
	{
		pair=new Pair(pair.node.left);
		stack.push(pair);
	}
}

private Stack<Pair<T>> stack = new Stack<Pair<T>>();

}

class TreeIterator<T extends Comparable<T>> extends AbsTreeIterator<T> {

	public TreeIterator(AbsTree<T> root) {
		super(root);
	}
}

class DupTreeIterator<T extends Comparable<T>> extends AbsTreeIterator<T> {

	public DupTreeIterator(AbsTree<T> root) {
		super(root);
	}
}

class Pair<T extends Comparable<T>> {
	AbsTree<T> node;
	int count;
	
	public Pair(AbsTree<T> node1)
	{
		count=node1.get_count();
		node= node1;
	}
}
import java.util.concurrent.locks.*;


/** 
    A first-in, first-out bounded collection of objects. 
*/ 

public class BoundedQueue<E>
{ 
   /** 
       Constructs an empty queue. 
       @param capacity the maximum capacity of the queue 
   */ 
   public BoundedQueue(int capacity) 
   { 
      elements = new Object[capacity]; 
      head = 0; 
      tail = 0; 
      size = 0;
      this.capacity = capacity;
   } 
   /** 
       Removes the object at the head. 
       @return the object that has been removed from the queue
       @precondition !isEmpty()
   */ 
   public E remove() throws InterruptedException
   { 
	  
	  lock.lock();
	  try {
		  while(size==0) {
			  notEmpty.await();
		  }
	      if (debug) System.out.print("removeFirst");
	      @SuppressWarnings("unchecked")
		E r = (E) elements[head]; 
	      if (debug) System.out.print(".");
	      head++;
	      if (debug) System.out.print(".");
	      size--;
	      if (head == elements.length) 
	      {
	         if (debug) System.out.print(".");
	         head = 0; 
	      }
	      if (debug) 
	         System.out.println("head=" + head + ",tail=" + tail 
	            + ",size=" + size);
	      notFull.signal();
	      return r; 
	  }
	  finally {
		  lock.unlock();
	  }
   } 

   /** 
       Appends an object at the tail. 
       @param newValue the object to be appended 
 * @throws InterruptedException 
       @precondition !isFull();
   */ 
   public void add(E newValue) throws InterruptedException 
   { 
	  boolean flag=false;
	  lock.lock();
	  try {
		  while(size==capacity) {
			  notFull.await();
		  }
		  for( Object item : elements) {
			  if(item!=null && item.equals(newValue)) {
				  flag=true;
				  break;
			 }
		  }
		  if(flag!=true) {
			  if (debug) System.out.print("add");
		      elements[tail] = newValue; 
		      if (debug) System.out.print(".");
		      tail++;
		      if (debug) System.out.print(".");
		      size++;
		      if (tail == elements.length) 
		      {
		         if (debug) System.out.print(".");
		         tail = 0; 
		      }
		      if (debug) 
		         System.out.println("head=" + head + ",tail=" + tail 
		            + ",size=" + size);
		      notEmpty.signal();
		      
		  }
	  } finally {
		  lock.unlock();
	  }
	  
   } 
   
   
   public void Forceadd(E newValue) throws InterruptedException 
   { 
	  lock.lock();
	  try {
		  while(size==capacity) {
			  notFull.await();
		  }
			 if (debug) System.out.print("add");
		      elements[tail] = newValue; 
		      if (debug) System.out.print(".");
		      tail++;
		      if (debug) System.out.print(".");
		      size++;
		      if (tail == elements.length) 
		      {
		         if (debug) System.out.print(".");
		         tail = 0; 
		      }
		      if (debug) 
		         System.out.println("head=" + head + ",tail=" + tail 
		            + ",size=" + size);
		      notEmpty.signal();
		  
	  } finally {
		  lock.unlock();
	  }
	  
   } 


   public boolean isFull() 
   { 
      return size == elements.length;
   } 

   public boolean isEmpty() 
   { 
      return size == 0; 
   } 

   public void setDebug(boolean newValue)
   {
      debug = newValue;
   }

   private Object[] elements; 
   private int capacity;
   private int head; 
   private int tail; 
   private int size;
   private boolean debug;
   private final Lock lock = new ReentrantLock();
   private final Condition notFull = lock.newCondition();
   private final Condition notEmpty = lock.newCondition();

}

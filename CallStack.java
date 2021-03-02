package interpretor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CallStack {
	Stack<ActivationRecord> records;
	public CallStack() {
		records=new Stack<>();
	}
   

 public void push(ActivationRecord a) {
	 records.push(a);
 }

public ActivationRecord pop() {
	return records.pop();
}
   public ActivationRecord peek() {
	   return records.peek();
   }
      public String toString() {
    	  Stack<ActivationRecord> holder= new Stack<>();
    	  StringBuilder s = new StringBuilder();
    	  
    	  while(!records.isEmpty()) {
    		  ActivationRecord a = records.pop();
    		  holder.push(a);
    		  s.insert(0,  a.toString()+" , ");
    	  }
    	  while(!holder.isEmpty()) {
    		  records.push(holder.pop());
    	  }
    	  s.insert(0,  "Call stack contents: ");
    	  return s.toString();
      }
      

 
}

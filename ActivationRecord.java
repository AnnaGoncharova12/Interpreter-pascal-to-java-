package interpretor;

import java.util.HashMap;

public class ActivationRecord {
	String name;
	ARType type;
	int nesting_level;
	HashMap<String, Object> members ;	
	public ActivationRecord(String name, ARType type, int nesting_level) {
       this.name = name;
        this.type = type;
        this.nesting_level = nesting_level;
        members = new HashMap<>();
	}

   public void  __setitem__(String key, Object value) {
        members.put(key,  value);
   }


   public Object get(String key) {
	   if(members.containsKey(key)) {
		   return members.get(key);
	   }
	   return null;
     
   }

    public String toString() {
    	String s=nesting_level+" "+ type+" "+ name+"    "+ "values :  ";
    	for(String key: members.keySet()) {
    		s+=key+" "+ members.get(key)+" ";
    	}
     
  
        return s;
    }

}

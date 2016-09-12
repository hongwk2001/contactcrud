package com.tkprof.servicefusion.contactcrud;
 
public class DataRecord {
  private int key = -1;
  private int int1;
  private int int2;
  private int int3; 
  private String str1;

  public int getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public int getInt1() {
    return int1;
  }

  public void setInt1(int val) {
    this.int1 = val;
  }

	  public int getInt2() {
		    return int2;
		  }
	  
	  public void setInt2(int val) {
		    this.int2 = val;
	 }

     public int getInt3() {
	    return int3;
	  }

	  public void setInt3(int val) {
	    this.int3 = val;
	  }
 
	  public String getStr1() {
		    return str1;
		  }

	  public void setStr1(String val) {
	    this.str1 = val;
	  }
	  
  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    return  key 
    		+" "+ int1
    		+" "+ int2
    		+" "+ int3
    		+" "+ str1; 
  }
} 

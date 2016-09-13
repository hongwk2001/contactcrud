package com.tkprof.servicefusion.contactcrud;
  
public class ContactRecord {
	private int key = -1;

  private String first_name;
  private String last_name;
  private String date_of_birth;
  private String zip_code;

  public ContactRecord(){
  }
  
  public ContactRecord( String first_name, String last_name,
						String date_of_birth, String zip_code){
	  this.first_name = first_name ;
	  this.last_name =  last_name;
	  this.date_of_birth= date_of_birth  ;
	  this.zip_code =  zip_code;
	 }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

	public String getFirstName() {
	  return first_name;
	}

	public void setFirstName(String fn) {
	  this.first_name = fn;
	}

	public String getLastName() {
	  return last_name;
	}

	public void setLastName(String ln) {
	  this.last_name = ln;
	}

	public String getDateOfBirth() {
		return date_of_birth;
	}

	public void setDateOfBirth(String dt) {
		this.date_of_birth = dt;
	}

	public String getZipCode() {
		return zip_code;
	}

	public void setZipCode(String zip) {
		this.zip_code = zip;
	}

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    return   first_name
			+" "+ last_name
			+" "+ zip_code
			+" "+ zip_code
    		 ;
  }
} 
public class Driver { 

    public String license; 
    public String name; 
    public String street; 
    public String city;
    public String province;  

    public Driver(String license, String name, String street, String city, String province){ 
        this.license = license; 
        this.name = name; 
        this.street = street; 
        this.city = city; 
        this.province = province; 
    } 

    public Driver(){ 
        this("", "", "", "", "");
    }

    public String toString(){
        return "#" + this.license + " " + this.name + " lives at" + " " + this.street + "," + this.city + ", " + this.province;
    }
    
}

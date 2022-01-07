public class Vehicle { 

    public String make; 
    public String model; 
    public int year; 
    public String color; 
    public String plate; 
    public Driver owner; 
    public boolean reportedStolen;
    
    public Vehicle(String make, String model, int year ,String color, String plate){
        this.make = make; 
        this.model = model; 
        this.year = year; 
        this.color = color; 
        this.plate = plate; 
    }

    public Vehicle(){
        this("", "", 0, "", "");
    }

    public String toString(){
        return "A " + this.color + " " + this.year + " " + this.make + " " + this.model + " with plate " + this.plate;
    }

    
}

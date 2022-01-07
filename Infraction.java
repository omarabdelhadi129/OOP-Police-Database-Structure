import java.util.Date;

public class Infraction { 

    public float amount; 
    public String description; 
    public Date dateIssued; 
    public boolean outstanding; 
    public Driver driver; 
    public String paid = "[OUTSTANDING]";


    public Infraction(float amount, String description, Date dateIssued){
        this.amount = amount; 
        this.description = description;
        this.dateIssued = dateIssued;
    } 

    public Infraction(){
        this(0.0f, "", new Date());
    } 

    public void pay(){
        outstanding = false;
        this.paid = "[PAID IN FULL]";
    }

    public String toString(){
        return "$" + String.format("%.2f", this.amount) + " " + this.description + " " + String.format("%tc", this.dateIssued) + " " + paid;
    }
    
}

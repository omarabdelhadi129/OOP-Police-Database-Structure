import java.util.Date;

public class PoliceDatabase { 

    public Vehicle[] vehicles; 
    public int numVehicles;
    public Driver[] drivers; 
    public int numDrivers; 
    public Infraction[] infractions; 
    public int numInfractions;

    // Static constants 
    public static final int max_drivers = 2000; 
    public static final int max_vehicles = 1000; 
    public static final int max_infractions = 800; 

    // Counters for array 
    public int counter_drivers = 0; 
    public int counter_vehicles = 0; 
    public int counter_infraction = 0;
    
    // To intiate arrays
    public PoliceDatabase(){
        this.drivers = new Driver[max_drivers];
        this.vehicles = new Vehicle[max_vehicles]; 
        this.infractions = new Infraction[max_infractions];
    }   

    public void registerDriver(Driver aDriver){
        if (numDrivers < max_drivers){
            drivers[counter_drivers] = aDriver; 
            counter_drivers +=1; 
            numDrivers += 1; 
        }
    }
    
    public void registerVehicle(Vehicle aVehicle, String license){ 
        if (numVehicles < max_vehicles){
            for(int i = 0; i < numDrivers; i++){
                if (drivers[i].license == license){ 
                    aVehicle.owner = drivers[i];
                    vehicles[counter_vehicles] = aVehicle; 
                    counter_vehicles += 1; 
                    numVehicles += 1;
                }
            }
        }
   }

   public void unregisterVehicle(String plate){   
        Vehicle[] temp_vehicles = new Vehicle[max_vehicles];
        int index_tracker = 0;
        
        for (int i = 0; i < numVehicles; i++){
            if(vehicles[i].plate != plate){
                temp_vehicles[index_tracker] = vehicles[i]; 
                index_tracker += 1;
            }
        } 

        vehicles = temp_vehicles; 
        numVehicles -= 1;
    } 

    public void reportStolen(String plate){ 
        for (int i = 0; i < numVehicles; i++){
            if (vehicles[i].plate == plate){
                vehicles[i].reportedStolen = true;
            }
        }
    }

    
    public void changeOwner(String plate, String license){
        Driver[] temp_driver = new Driver[1]; 
        for (int i = 0; i < numDrivers; i++ ){
            if (drivers[i].license == license){
                temp_driver[0] = drivers[i];
            }
        } 
        for (int j = 0; j < numVehicles; j++){
            if (vehicles[j].plate == plate){
                vehicles[j].owner = temp_driver[0];
            }
        }
    }


    public Infraction issueInfraction(String license, float amount, String description, Date date){ 
        for (int i = 0; i < numDrivers; i++){
             if (drivers[i].license == license && numInfractions < max_infractions){
                Infraction ticket = new Infraction(amount, description, date);
                infractions[counter_infraction] = ticket;
                ticket.driver = drivers[i];
                ticket.outstanding = true;
                numInfractions += 1; 
                counter_infraction += 1;
                return ticket;
             }
        }
        return null;
    } 


    public boolean hasOutstandingInfractions(Driver d){
        for (int i = 0; i < numInfractions; i++){
             if (infractions[i].outstanding == true && d == infractions[i].driver){
                  return true;
             }
        } 
        return false;
    } 


    public boolean shouldStopVehicle(String plate){ 
        for (int i = 0; i < numVehicles; i++){
             if (vehicles[i].plate == plate){
                if (vehicles[i].reportedStolen == true){ 
                    return true;
                } else { 
                    for (int j = 0; j < numInfractions; j++){ 
                        if (infractions[j].driver == vehicles[i].owner){ 
                            if (hasOutstandingInfractions(infractions[j].driver)){
                                return true;
                            }
                        }
                    }
                }
             }
        }
        
        return false;
    } 


    public void showInfractionsFor(String license){ 
        int outstanding_counter = 0; 
        for (int i = 0; i < numInfractions; i++){ 
             if (license == infractions[i].driver.license){ 
                System.out.println("$" + infractions[i].amount + " " + "Infraction on " + infractions[i].dateIssued + " " + infractions[i].paid); 
                if (infractions[i].outstanding == true){
                    outstanding_counter += 1;
                }
             }
        }
        System.out.println("Total outstanding infractions = " + outstanding_counter);
    }


    public Driver[] cleanDrivers(){ 
        int counter_cleandrivers = 0;
        Driver[] clean_drivers_temp = new Driver[numDrivers];
        Driver[] dirty_drivers = new Driver[1]; 


        for (int i = 0; i < numDrivers; i++){
             for (int j = 0; j < numInfractions; j++){
                if (drivers[i].license == infractions[j].driver.license){
                    dirty_drivers[0] = drivers[i]; 
                    break;
                }
             }
        
             if (dirty_drivers[0] == null){
                clean_drivers_temp[counter_cleandrivers] = drivers[i]; 
                counter_cleandrivers += 1;
             } else {
                dirty_drivers[0] = null;
             }
        
        }

        Driver[] cleanDrivers = new Driver[counter_cleandrivers]; 

        for (int k = 0; k < counter_cleandrivers; k++){
            cleanDrivers[k] = clean_drivers_temp[k];

        }

        return cleanDrivers;
    }

    public void showInfractionReport(){ 
        String driverName = ""; 
        int infractionsNumber = 0; 
        float amounts_paid = 0.0f;
        boolean reported = false; 

        for (int i = 0; i < numDrivers; i++){
            for (int j = 0; j < numInfractions; j++){
                if (drivers[i] == infractions[j].driver){
                    driverName = drivers[i].name; 
                    infractionsNumber += 1; 
                    reported = true; 
                    
                    if (infractions[j].outstanding == false){
                        amounts_paid += infractions[j].amount;
                    }
                } 
            }
            
            if (reported){
                System.out.println(String.format("%20s", driverName) + " : " + String.format("%d", infractionsNumber) + " infractions, total paid = " + String.format("$%.2f", amounts_paid)); 
            }
            reported = false; 
            infractionsNumber = 0; 
            amounts_paid = 0.0f;
        }
    }



    public static  PoliceDatabase example() { // Register all drivers and their vehicles
        PoliceDatabase pdb = new PoliceDatabase();
    
        pdb.registerDriver(new Driver("L1567-34323-84980", "Matt Adore",
                "1323 Kenaston St.", "Gloucester", "ON"));
        pdb.registerDriver(new Driver("L0453-65433-87655", "Bob B. Pins",
                "32 Rideau Rd.", "Greely", "ON"));
        pdb.registerDriver(new Driver("L2333-45645-54354", "Stan Dupp",
                "1355 Louis Lane", "Gloucester", "ON"));
        pdb.registerDriver(new Driver("L1234-35489-99837", "Ben Dover",
                "2348 Walkley Rd.", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L8192-87498-27387", "Patty O'Lantern",
                "2338 Carling Ave.", "Nepean", "ON"));
        pdb.registerDriver(new Driver("L2325-45789-35647", "Ilene Dover",
                "287 Bank St.", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L1213-92475-03984", "Patty O'Furniture",
                "200 St. Laurant Blvd.", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L1948-87265-34782", "Jen Tull",
                "1654 Stonehenge Cres.", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L0678-67825-83940", "Jim Class",
                "98 Oak Blvd.", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L0122-43643-73286", "Mark Mywords",
                "3 Third St.", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L6987-34532-43334", "Bob Upandown",
                "434 Gatineau Way", "Hull", "QC"));
        pdb.registerDriver(new Driver("L3345-32390-23789", "Carrie Meehome",
                "123 Thurston Drive", "Kanata", "ON"));
        pdb.registerDriver(new Driver("L3545-45396-88983", "Sam Pull",
                "22 Colonel By Drive", "Ottawa", "ON"));
        pdb.registerDriver(new Driver("L1144-26783-58390", "Neil Down",
                "17 Murray St.", "Nepean", "ON"));
        pdb.registerDriver(new Driver("L5487-16576-38426", "Pete Reedish",
                "3445 Bronson Ave.", "Ottawa", "ON"));
        pdb.registerVehicle(new Vehicle("Honda", "Civic", 2015, "yellow", "W3EW4T"),
                "L0453-65433-87655");
        pdb.registerVehicle(new Vehicle("Pontiac","Grand Prix",2007,"dark green","GO SENS"),
                "L0453-65433-87655");
        pdb.registerVehicle(new Vehicle("Mazda", "RX-8", 2004, "white", "OH YEAH"),
                "L2333-45645-54354");
        pdb.registerVehicle(new Vehicle("Nissan","Altima",2017,"bergundy", "Y6P9O7"),
                "L1234-35489-99837");
        pdb.registerVehicle(new Vehicle("Saturn", "Vue", 2002, "white", "9R6P2P"),
                "L2325-45789-35647");
        pdb.registerVehicle(new Vehicle("Honda", "Accord", 2018, "gray", "7U3H5E"),
                "L2325-45789-35647");
        pdb.registerVehicle(new Vehicle("Chrysler", "PT-Cruiser", 2006, "gold", "OLDIE"),
                "L2325-45789-35647");
        pdb.registerVehicle(new Vehicle("Nissan", "Cube", 2010, "white", "5Y6K8V"),
                "L1948-87265-34782");
        pdb.registerVehicle(new Vehicle("Porsche", "959", 1989, "silver", "CATCHME"),
                "L0678-67825-83940");
        pdb.registerVehicle(new Vehicle("Kia", "Soul", 2018, "red", "J8JG2Z"),
                "L0122-43643-73286");
        pdb.registerVehicle(new Vehicle("Porsche", "Cayenne", 2014, "black", "EXPNSV"),
                "L6987-34532-43334");
        pdb.registerVehicle(new Vehicle("Nissan", "Murano", 2010, "silver", "Q2WF6H"),
                "L3345-32390-23789");
        pdb.registerVehicle(new Vehicle("Honda", "Element", 2008, "black", "N7MB5C"),
                "L3545-45396-88983");
        pdb.registerVehicle(new Vehicle("Toyota", "RAV-4", 2010, "green", "R3W5Y7"),
                "L3545-45396-88983");
        pdb.registerVehicle(new Vehicle("Toyota", "Celica", 2006, "red", "FUNFUN"),
                "L5487-16576-38426");
    
        return pdb;
    }
}
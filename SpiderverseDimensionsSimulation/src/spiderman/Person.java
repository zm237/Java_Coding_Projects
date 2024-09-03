package spiderman;

public class Person {
    private int location;
    private String name;
    private int signature;

    public Person(int location, String name, int signature){
        this.location = location;
        this.name = name;
        this.signature = signature;
    }

    public int getLocation(){return location;}

    public String getName(){return name;}

    public int getSignature(){return signature;}

    public void setLocation(int location){this.location = location;}

    public Person copy(){
        int copyLocation = location;
        String copyName = name;
        int copySig = signature;

        Person copyPerson = new Person(copyLocation, copyName, copySig);
        
        return copyPerson;
    }
}

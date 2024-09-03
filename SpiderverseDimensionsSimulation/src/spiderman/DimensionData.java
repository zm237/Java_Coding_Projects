package spiderman;

import java.util.ArrayList;

public class DimensionData{

    private int numOfCanonEvents;
    private int dimWeight;
    private ArrayList<Person> peopleList;

    public DimensionData(int numOfEvent, int weight){
        numOfCanonEvents = numOfEvent;
        dimWeight = weight;
        peopleList = new ArrayList<>();
    }

    public void setNumOfCanonEvents(int number){
        numOfCanonEvents = number;
    } 

    public void setDimWeight(int number){
        dimWeight = number;
    }

    public int getDimWeight(){
        return dimWeight;
    }

    public int getNumOfEvents(){
        return numOfCanonEvents;
    }

    public void addPerson(Person person){
        peopleList.add(person);
    }

    public void removePerson(Person person){
        peopleList.remove(person);
    }

    public ArrayList<Person> getPeopleList(){return peopleList;}
}

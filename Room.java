import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version October 21, 2012
 * 
 * @author Leonardo Paz
 * @version October 20, 2017
 */

public class Room 
{
    /** A description of the room */
    private String description;
    
    /** Stores exits of this room */
    private HashMap<String, Room> exits;
    
    /** An ArrayList of items in a room */
    private ArrayList<Item> items;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        items = new ArrayList<Item>();
        this.description = description;
        exits = new HashMap<String, Room>();
    }

    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit
     * @param neighbour The room to which the exit leads
     */
    public void setExit(String direction, Room neighbour) 
    {
        exits.put(direction, neighbour);
    }

    /**
     * Returns a short description of the room, i.e. the one that
     * was defined in the constructor
     * 
     * @return The short description of the room
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items: A Toothbrush
     *     Weight in lbs: 0.5,
     *     Painting
     *     Weight in lbs: 5
     *     
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String longDesc = "Items: ";
        for(Item item: items) {
            longDesc += item.toString() + ",\n";
        }
        return "You are " + description + ".\n" + getExitString() + "\n" + longDesc;
    }
    
    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction
     * @return The room in the given direction
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Adds an item to a room.
     * 
     * @param item The item that will be added to the room
     */
    public void addItem(Item item) 
    {
        items.add(item);
    }
    
    /**
     * Return the list of items in a room
     * 
     * @return The arraylist of items in the room
     */
    public ArrayList<Item> getItems() 
    {
        return items;
    }
    
}


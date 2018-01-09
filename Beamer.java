
/**
 * A beamer can be charged and fired. When you charge the beamer it remembers
 * what room it was charged in and when fired, transports the player to
 * that room. 
 *
 * @author Leonardo Paz
 * @version 2017.07.11
 */
public class Beamer extends Item
{
    
    private Room chargedRoom;

    /**
     * Constructor for objects of class Beamer
     * 
     * @param desc A short description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Beamer(String name, String desc, double weight)
    {
        super(name, desc, weight);
        chargedRoom = null;
    }

    /**
     * This method charges the beamer and stores the room for when fire
     * is called.
     *
     * @param room The room that the beamer is charged in
     */
    public void charge(Room room)
    {
        chargedRoom = room;
    }
    
    /**
     * This method returns the charged room.
     * 
     * @return The room where the beamer was charged
     */
    public Room getChargedRoom()
    {
        return chargedRoom;
    }
    
    /**
     * This method returns whether or not the beamer is charged.
     * 
     * @return True if the beamer is charged, false otherwise
     */
    public boolean isCharged()
    {
        if (!(chargedRoom == null)) {
            return true;
        }
        else {
            return false;
        }
    }
}

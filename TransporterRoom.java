import java.util.Random;
import java.util.ArrayList;
/**
 * Whenever the player leaves the transporter room with the "go" command, 
 * they are randomly transported into another room.
 *
 * @author Leonardo
 * @version 2017.07.11
 */
public class TransporterRoom extends Room
{
    /** Random index for list of rooms */
    private int randIndex;
    
    /** ArrayList of Rooms */
    private ArrayList<Room> rooms;
    
    /**
     * Constructer for the Transporter Room class.
     * 
     * @param description The room's description.
     */
    public TransporterRoom(String description, ArrayList<Room> rooms)
    {
        super(description);
        this.rooms = rooms;
    }
    
    /**
     * Returns a random room, independent of the direction parameter.
     *
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    public Room getExit(String direction)
    {
        Random rand = new Random();
        randIndex = rand.nextInt(rooms.size());
        return rooms.get(randIndex);
    }
}
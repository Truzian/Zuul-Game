import java.util.*;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
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

public class Game 
{
    /** Parser that intereprets commands */
    private Parser parser;
    
    /** The current room the player is in */
    private Room currentRoom;
    
    /** The previous room */
    private Room prevRoom;
    
    /** Stack of rooms visited */
    private Stack<Room> visits;
    
    /** Item that is being held */
    private Item heldItem;
    
    /** How many times the player has picked up an item */
    private int itemCount;
    
    /** Beamer item that can charge and fire */
    private Beamer beamer;
    
    /** Transporter Room that takes the player somewhere random */
    private TransporterRoom transporterRoom;
    
    /** ArrayList of Rooms */
    private ArrayList<Room> rooms;
       
    /**
     * Create the game and initialise its internal map and held item.
     */
    public Game() 
    {
        visits = new Stack<Room>();
        beamer = new Beamer("beamer", "beamer transporter gun", 5);
        rooms = new ArrayList<Room>();
        createRooms();
        parser = new Parser();
        heldItem = null;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporterRoom = new TransporterRoom("in a transporter Room", rooms);
        
        // add rooms to the ArrayList
        rooms.add(outside);
        rooms.add(theatre);
        rooms.add(pub);
        rooms.add(lab);
        rooms.add(office);
        
        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", transporterRoom);
        
        // create items
        Item key, tv, remote, couch, cookie;
        key = new Item("keys", "set of keys", 0.5);
        tv = new Item("tv", "flatscreen tv", 5);
        remote = new Item("remote", "remote control", 1);
        couch = new Item("couch", "leather couch", 50);
        cookie = new Item("cookie", "cookie", 0.1);
        
        
        // add items to rooms
        outside.addItem(key);
        outside.addItem(remote);
        outside.addItem(cookie);
        outside.addItem(beamer);
        
        pub.addItem(key);
        pub.addItem(couch);
        pub.addItem(cookie);
        
        office.addItem(tv);
        office.addItem(couch);
        office.addItem(key);
        
        office.addItem(cookie);
        
        theatre.addItem(cookie);
        
        lab.addItem(cookie);
        
        transporterRoom.addItem(beamer);
        
        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        
        transporterRoom.setExit("south", outside);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
        
        visits.push(currentRoom); // add starting room to visits
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        if (!(heldItem == null)) {
            System.out.println("You are holding a " + heldItem.getName());
        }
        else {
            System.out.println("You aren't holding anything");
        }
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackback")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        if (currentRoom == transporterRoom) {
            nextRoom = transporterRoom.getExit(direction);
        }
        

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }   
        else {
            prevRoom = currentRoom;
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            visits.push(currentRoom);
            if (!(heldItem == null)) {
                System.out.println("You are holding a " + heldItem.getName());
            }   
            else {
                System.out.println("You aren't holding anything");
            }
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "look" was entered. Output the detailed description of the 
     * current room.
     * 
     * @param command The command to be processed
     */
    private void look(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            System.out.println(currentRoom.getLongDescription());
            if (!(heldItem == null)) {
                System.out.println("You are holding a " + heldItem.getName());
            }
            else {
                System.out.println("You aren't holding anything");
            }
        }
    }
    
    /** 
     * "eat" was entered. Output the detailed description of the 
     * current room.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
            return;
        }
        
        if (heldItem == null) {
            System.out.println("You have no food.");
        }
        else if (heldItem.getName().equals("cookie")) {
            System.out.println("You have eaten a cookie.");
            heldItem = null;
            itemCount = 1; // starts counting at 1 and checks if reaches 6 in take method
        }   
    }
    
    /** 
     * "back" was entered. Changes the current room to the room you were previously in
     * 
     * @param command The command to be processed
     */
    private void back(Command command)
    {
        if(command.hasSecondWord())
        {
            System.out.println("Back what?");
            return;
        }
        
        if(prevRoom == null) {
            System.out.println("You are at the beginning, you can't go back!");
        }
        
        else {
            Room temp = prevRoom;
            prevRoom = currentRoom;
            currentRoom = temp;
            visits.push(currentRoom);
            System.out.println(currentRoom.getLongDescription());
            if (!(heldItem == null)) {
                System.out.println("You are holding a " + heldItem.getName());
            }
            else {
                System.out.println("You aren't holding anything");
            }
        }
    }
    
    /** 
     * "stackback" was entered. Sets the current room to the room visited before that room through a stack. Can stackback all the way to the beginning. Stackback works in the way you have visited
     * rooms. Therefore, if you go forward then go back then go forward again, stackback will take you back in that order and remove the rooms as you go back.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        
        if (visits.size() > 1) {
            prevRoom = visits.pop();
            currentRoom = visits.peek();
            System.out.println(currentRoom.getLongDescription());
            if (!(heldItem == null)) {
                System.out.println("You are holding a " + heldItem.getName());
            }
            else {
                System.out.println("You aren't holding anything");
            }
        }
        else {
            System.out.println("You are at the beginning, you can't go back!");
        }
    }
    
    /**
     * Picks up a specified item. This is a two word command where the second word specifies what is to be taken.
     * If there is nothing to take that will be outputted. Also, if you are holding something already then you
     * can't pick something else up and that will be outputted to the system.
     * 
     * @param command The command to be processed
     */
    private void take(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take
            System.out.println("take what?");
            return;
        }
        
        String itemToTake = command.getSecondWord();
        
        if (!(heldItem == null)) {
            System.out.println("You are already holding something.");
        } 
        
        else {
            boolean added = false; // boolean to see if item was added or not
            if (!(currentRoom.getItems() == null)) {
                
                if ((itemCount % 6 == 0) & (!itemToTake.equals("cookie"))) { // item won't be picked up unless cookie
                    System.out.println("You must eat a cookie before picking up more items");
                    return;
                }
                
                for (Item it: currentRoom.getItems()) {
                    if (it.getName().equals(itemToTake)) {
                        heldItem = it;
                        System.out.println("You picked up " + heldItem.getName());
                        added = true;
                        itemCount++;
                    }
                }
            }
            else {
                System.out.println("There are no items in the room to take.");
            }
            
            if (!added) {
                System.out.println("That item is not in the room.");
            }
        }
    }
    
    /**
     * Drops a specified item. This is a one word command. If there is nothing to drop that will be outputted.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("drop what?");
            return;
        }
        
        if (!(heldItem == null)) {
            System.out.println("You have dropped " + heldItem.getName());
            heldItem = null;
        }
        
        else {
            System.out.println("You are not holding anything");
        }
    }
    
    /**
     * This method charges the beamer and stores the room for when fire
     * is called.
     * 
     * @param command The command to be processed
     */
    private void charge(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("drop what?");
            return;
        }
        
        if ((heldItem == null) || (!(heldItem.getName().equals("beamer")))) {
            System.out.println("You are not holding a beamer");
        }
        else if (!(heldItem == null) & (heldItem.getName().equals("beamer"))) {
            System.out.println("The beamer has been charged");
            beamer.charge(currentRoom);
        }
    }
    
    /**
     * This method fires the beamer and takes the player back
     * to the room where it was charged
     * 
     * @param command The command to be processed
     */
    private void fire(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("drop what?");
            return;
        }
        
        if ((heldItem == null) || (!beamer.isCharged())) { // check to see if beamer is charged and holding a beamer
            System.out.println("You are not holding a beamer or it is not charged");
            return;
        }
        else if ((beamer.isCharged()) & (heldItem.getName().equals("beamer"))) {
            prevRoom = currentRoom;
            currentRoom = beamer.getChargedRoom();
            System.out.println(currentRoom.getLongDescription());
            visits.push(currentRoom);
            beamer.charge(null); // reset the charged room to null
        }
    }
}

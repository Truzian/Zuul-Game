
/**
 * Write a description of class Item here.
 *
 * @author Leonardo Paz
 * @version October 20, 2017
 */
public class Item
{
    /** description of the item */
    private String description;
    
    /** weight of the item */
    private double weight;
    
    /** name of the item */
    private String name;
    
    /**
     * Constructs an item with a description of the item and weight of the item
     * 
     * @param desc A short description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Item(String name, String desc, double weight)
    {
        description = desc;
        this.weight = weight;
        this.name = name;
    }

    /**
     * This method returns a string representation of the item containing its description
     * and weight in the form:
     *     A Toothbrush
     *     Weight in lbs: 0.5
     * 
     * @return A string respresentation of the item
     */
    public String toString()
    {
        return description + "\nWeight in lbs: " + Double.toString(weight);
    }
    
    /** 
     * This method returns the name of the item in string representation.
     * 
     * @return The name of the item
     */
    
    public String getName()
    {
        return name;
    }
}

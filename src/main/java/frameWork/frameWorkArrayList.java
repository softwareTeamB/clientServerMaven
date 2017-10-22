package frameWork;

import java.util.ArrayList;

/**
 * De in deze klassen zijn de belangrijkste methoden voor het aanstruren van een data structure
 *
 * @author michel
 */
public interface frameWorkArrayList {

    /**
     * Kijk of de farme work datastructure leeg is of niet
     *
     * @param strucutre de grote van de datastructure
     * @return of het empty is of niet
     */
    public boolean isEmpty(ArrayList strucutre);

    /**
     * Kijk de grote van de datastructure
     *
     * @param strucutre de grote van de datastructure
     * @return bereken de grote van het object
     */
    public int size(ArrayList strucutre);

    /**
     * Methoden die het toevoegd
     *
     * @param strucutre de grote van de datastructure
     * @param item wat toegevoegd moet worden in een array
     * @return return een objct
     */
    public ArrayList add(ArrayList strucutre, Object[] item);
    
    /**
     * Methoden die het toevoegd
     *
     * @param strucutre de grote van de datastructure
     * @param item wat toegevoegd moet worden
     * @return return een objct
     */
    public ArrayList add(ArrayList strucutre, Object item);

    /**
     * Methoden een item remove
     *
     * @param strucutre het item wat verwijderd moet worden
     * @param item remove item uit de stucture
     * @return return een object
     */
    public ArrayList remove(ArrayList strucutre, Object item);

    /**
     * Methoden die het sort
     *
     * @param strucutre het item wat verwijderd moet worden
     * @return return een object
     */
    public ArrayList sort(ArrayList strucutre);

    /**
     * Methoden die van een arrayList een arrayMaakt
     * @param structure ArrayList
     * @return een object ArrayList
     */
    public Object[] makeArray(ArrayList structure);
}

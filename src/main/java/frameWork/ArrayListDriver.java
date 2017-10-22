package frameWork;

import global.ConsoleColor;
import java.util.ArrayList;
import java.util.Collections;

/**
 * ArrayList drivers
 *
 * @author michel
 */
public class ArrayListDriver implements frameWorkArrayList {

    /**
     * Kijk of de farme work datastructure leeg is of niet
     *
     * @param strucutre de grote van de datastructure
     * @return of het empty is of niet
     */
    @Override
    public boolean isEmpty(ArrayList strucutre) {

        //roep de methoden op die de grote van de ArrayList opvraagds
        int groteArray = size(strucutre);

        //geef terug of de arrayList leeg is of niet
        if (groteArray == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Kijk de grote van de datastructure
     *
     * @param strucutre de grote van de datastructure
     * @return bereken de grote van het object
     */
    @Override
    public int size(ArrayList strucutre) {

        //return terug hoe groot de structure is
        return strucutre.size();
    }

    /**
     * Methoden die het toevoegd
     *
     * @param strucutre de grote van de datastructure
     * @param item wat toegevoegd moet worden in een array
     * @return return een objct
     */
    @Override
    public ArrayList add(ArrayList strucutre, Object[] item) {

        //loop door de object array heen
        for (int i = 0; i < item.length; i++) {

            //object item
            Object object = item[i];

            //roep de add methoden op met een item
            strucutre = add(strucutre, object);
        }

        //return de nieuwe arrayList
        return strucutre;
    }

    /**
     * Methoden die het toevoegd
     *
     * @param strucutre de grote van de datastructure
     * @param item wat toegevoegd moet worden
     * @return return een objct
     */
    @Override
    public ArrayList add(ArrayList strucutre, Object item) {

        //voeg het item toe in de arrayLIst structure
        strucutre.add(item);

        //return de arrayList structure
        return strucutre;
    }

    /**
     * Methoden een item remove
     *
     * @param strucutre het item wat verwijderd moet worden
     * @param item remove item uit de stucture
     * @return return een object
     */
    @Override
    public ArrayList remove(ArrayList strucutre, Object item) {

        //boolean standaart of default
        boolean itemVerwijderd = false;

        //loop door de hele arrayList heen
        for (int i = 0; i < strucutre.size(); i++) {

            //get object
            Object get = strucutre.get(i);

            //als het get item gelijk is aan het item
            if (get.toString().equals(item.toString())) {

                //remove het item
                strucutre.remove(i);

                //item verwijderd
                itemVerwijderd = true;
            }
        }

        //als het itemVerwijderd nog steeds false is geef dan een melding
        if (!itemVerwijderd) {
            ConsoleColor.warn("Het item " + item + " is niet gevonden in de arrayList.");
        }
        //return arrayList
        return strucutre;
    }

    /**
     * Methoden die het sort
     *
     * @param strucutre het item wat verwijderd moet worden
     * @return return een object
     */
    @Override
    public ArrayList sort(ArrayList strucutre) {
        
        //collections arrayList
        Collections.sort(strucutre);
        
        //return de gesorterde arrayTerug
        return strucutre;
    }
}

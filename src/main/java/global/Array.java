package global;

/**
 * Deze methoden is om in de array methoden toe te voegen of de verwijderen
 *
 * @author michel
 */
public class Array {

    /**
     * Deze methoden vraagt de oude array grote op en maak een nieuwe array die
     * 1 grote is en zet de oude data over en voeg de nieuwe String toe
     *
     * @param oldArray de oude array
     * @param addArrayData wat toegevoegd moet worden aan de array
     * @return de nieuwe array
     */
    public String[] addArray(String[] oldArray, String addArrayData) {

        //nummer welk positie gevuld is
        int positieArray = 0;

        //kijk hoe groot de oude array is
        int oldArrayGrote = oldArray.length;

        //de nieuwe array grote is oldArrayGrote ++ 
        int newArrayGrote = oldArrayGrote++;

        //de nieuwe array
        String[] newArray = new String[newArrayGrote];

        //loop door de oude data heen en vroeg het toe in de nieuwe array
        for (int i = 0; i < oldArray.length; i++) {

            newArray[positieArray] = newArray[i];

            //voeg + 1
            positieArray++;
        }

        //voeg de string to aan de nieuwe array
        newArray[positieArray] = addArrayData;
        
        //return de nieuwe array
        return newArray;
    }

}

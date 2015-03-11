package logika.model;

public interface SymbolTable {

    public Constant constantByName(String name);

    public default boolean constantExists(final String name) {
        try {
            constantByName(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isReservedName(String name);

    public Predicate predicateByName(String name);

    public default boolean predicateExists(final String name) {
        try {
            predicateByName(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Term termByName(String termName);

    public default boolean termExists(final String term) {
        try {
            termByName(term);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Variable varByName(String varName);

    public default boolean varExists(final String varName) {
        try {
            varByName(varName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}

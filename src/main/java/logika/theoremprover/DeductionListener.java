package logika.theoremprover;

import java.util.function.Consumer;

public interface DeductionListener extends Consumer<DeductionStep>{
    
    public static final DeductionListener NONE = step -> {};
    
}

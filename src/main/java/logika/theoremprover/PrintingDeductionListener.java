package logika.theoremprover;

import java.util.HashMap;
import java.util.Map;

public class PrintingDeductionListener implements DeductionListener {
    
    private Map<Integer, Integer> depthCounter = new HashMap<>();
    
    private Map<DeductionStep, String> prefixes = new HashMap<>();
    
    private String nextPrefixInDepth(int depth){
        Integer counter = depthCounter.get(depth);
        if (counter == null) {
            counter = 1;
            depthCounter.put(depth, counter);
        }
        return counter.toString();
    }

    @Override
    public void accept(DeductionStep step) {
//        DeductionStep prevStep = step.getPrevStep();
//        if (prevStep == null) {
//            prefix = nextPrefixInDepth(step.getDepth());
//        }
//        String prefix = prefixes.get(step);
//        if (prefix == null) {
//            prefix = nextPrefixInDepth(step.getDepth());
//        }
        System.out.println("\t" + step.getStepSequent());
    }
     
}

package logika.theoremprover;

import java.util.HashMap;
import java.util.Map;

import logika.model.Sequent;

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
        System.out.println(prefixFor(step) + step.getStepSequent());
    }

	private String prefixFor(DeductionStep step) {
		StringBuilder rval = new StringBuilder();
		int depth = step.getDepth();
		for (int i = 0; i < depth; ++i) {
			rval.append("  ");
		}
		return rval.toString();
	}
     
}

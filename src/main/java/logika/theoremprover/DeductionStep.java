package logika.theoremprover;

import logika.model.Sequent;

public class DeductionStep {
    
    private DeductionStep prevStep;
    
    private Sequent stepSequent;

    public DeductionStep(DeductionStep prevStep, Sequent stepSequent) {
        this.prevStep = prevStep;
        this.stepSequent = stepSequent;
    }
    
    @Override
    public int hashCode() {
        return System.identityHashCode(stepSequent);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof DeductionStep)) {
            return false;
        }
        return hashCode() == other.hashCode();
    }

    public DeductionStep getPrevStep() {
        return prevStep;
    }

    public Sequent getStepSequent() {
        return stepSequent;
    }
    
    public int getDepth() {
        if (prevStep == null) {
            return 1;
        }
        return prevStep.getDepth() + 1;
    }

}

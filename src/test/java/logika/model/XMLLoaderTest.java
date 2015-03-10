package logika.model;

import java.util.Set;

import logika.model.Constant;
import logika.model.Language;
import logika.model.Predicate;
import logika.model.Term;
import logika.model.Type;
import logika.model.XMLLoader;

import org.junit.Assert;
import org.junit.Test;

public class XMLLoaderTest {
    
    private Language load(String xmlFile) {
        return new XMLLoader(getClass().getResourceAsStream(xmlFile)).load();
    }
    
    @Test
    public void typesLoaded() {
        Language actual = load("/lang1.xml");
        Set<Type> loadedTypes = actual.getTypes();
        Assert.assertEquals(3, loadedTypes.size());
        Assert.assertTrue(loadedTypes.contains(new Type("Type1")));
        Assert.assertTrue(loadedTypes.contains(new Type("Type2")));
        Assert.assertTrue(loadedTypes.contains(new Type("Type3")));
    }
    
    @Test
    public void constantsLoaded() {
        Language actual = load("/lang1.xml");
        Set<Constant> loadedConstants = actual.getConstants();
        Assert.assertEquals(3, loadedConstants.size());
    }
    
    @Test
    public void predicatesLoaded() {
        Language actual = load("/lang1.xml");
        Set<Predicate> loadedPredicates = actual.getPredicates();
        Assert.assertEquals(2, loadedPredicates.size());
        Assert.assertEquals(2, actual.predicateByName("P1").getArgTypes().size());
    }
    
    @Test
    public void termsLoaded() {
        Language lang = load("/lang1.xml");
        Set<Term> terms = lang.getTerms();
        Assert.assertEquals(3, terms.size());
    }

}

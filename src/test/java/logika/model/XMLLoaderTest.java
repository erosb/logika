package logika.model;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class XMLLoaderTest {

    @Test
    public void constantsLoaded() {
        Language actual = load("/lang1.xml");
        Set<Constant> loadedConstants = actual.getConstants();
        Assert.assertEquals(3, loadedConstants.size());
    }

    @Test
    public void functionsLoaded() {
        Language lang = load("/lang1.xml");
        Set<Function> functions = lang.getFunctions();
        Assert.assertEquals(3, functions.size());
    }

    private Language load(final String xmlFile) {
        return new XMLLoader(getClass().getResourceAsStream(xmlFile)).load();
    }

    @Test
    public void predicatesLoaded() {
        Language actual = load("/lang1.xml");
        Set<Predicate> loadedPredicates = actual.getPredicates();
        Assert.assertEquals(6, loadedPredicates.size());
        Assert.assertEquals(2, actual.predicateByName("P1").getArgTypes().size());
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

}

package logika.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLLoader {
    
    private final Document document;
    
    private final XPathFactory xpathFactory = XPathFactory.newInstance();
    
    private Set<Type> types = new HashSet<>();
    
    private Set<Constant> constants = new HashSet<>();
    
    private Set<Predicate> predicates = new HashSet<>();
    
    private Set<Term>  terms = new HashSet<>();
    
    public XMLLoader(InputStream in) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            document = dbf.newDocumentBuilder().parse(in);
            XPath xpath = xpathFactory.newXPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void loadTypes() {
        NodeList typeNodes = query("/language/types/type");
        for (int i = 0; i < typeNodes.getLength(); ++i) {
            String typeName = typeNodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
            types.add(new Type(typeName));
        }
    }
    
    private NodeList query(String string) {
        try {
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expr = xpath.compile(string);
            return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public Language load() {
        loadTypes();
        loadConstants();
        loadPredicates();
        loadTerms();
        return new Language(types, constants, predicates, terms);
    }
    
    private void loadTerms() {
        NodeList termNodes = query("/language/terms/term");
        for (int i = 0; i < termNodes.getLength(); ++i) {
            Node termNode = termNodes.item(i);
            String termName = termNode.getAttributes().getNamedItem("name").getNodeValue();
            List<Type> argTypes = loadArgumentTypes(termNode);
            String termTypeName = termNode.getAttributes().getNamedItem("type").getNodeValue();
            Type termType = typeByName(termTypeName);
            terms.add(new Term(termName, argTypes, termType));
        }
    }

    private void loadPredicates() {
        NodeList predicateNodes = query("/language/predicates/predicate");
        for (int i = 0; i < predicateNodes.getLength(); ++i) {
            Node predNode = predicateNodes.item(i);
            String predName = predNode.getAttributes().getNamedItem("name").getNodeValue();
            List<Type> argTypes = loadArgumentTypes(predNode);
            predicates.add(new Predicate(predName, argTypes));
        }
    }

    private List<Type> loadArgumentTypes(Node parentNode) {
        List<Type> argTypes = new ArrayList<Type>();
        for (int j = 0; j < parentNode.getChildNodes().getLength(); ++j) {
            Node childNode = parentNode.getChildNodes().item(j);
            if (!childNode.getNodeName().equals("arg")) {
                continue;
            }
            String argTypeName = childNode.getAttributes().getNamedItem("type").getNodeValue();
            Type argType = typeByName(argTypeName);
            argTypes.add(argType);
        }
        return argTypes;
    }

    private Type typeByName(String typeName) {
        return types.stream().filter((t) -> t.getName().equals(typeName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("type [" + typeName + "] not found"));
    }

    private void loadConstants() {
        NodeList constantNodes = query("/language/constants/constant");
        for (int i = 0; i < constantNodes.getLength(); ++i) {
            NamedNodeMap attrs = constantNodes.item(i).getAttributes();
            String constantName = attrs.getNamedItem("name").getNodeValue();
            String typeName = attrs.getNamedItem("type").getNodeValue();
            constants.add(new Constant(constantName, typeByName(typeName)));
        }
    }

}

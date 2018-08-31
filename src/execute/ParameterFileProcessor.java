package execute;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import java.io.File;
import java.io.IOException;

import static execute.ExecutorConstants.EMPTY_STRING;
import static execute.ExecutorConstants.NAME;
import static execute.ExecutorConstants.PARAMETER_FILE_ROOT;
import static execute.ExecutorConstants.PROPERTY_OPERATOR;
import static execute.ExecutorConstants.SCOPE;

public class ParameterFileProcessor {
    private final String filePath;
    private Document document;


    public ParameterFileProcessor(String filePath) throws SAXException, IOException, ParserConfigurationException{
        this.filePath = filePath;
        readyFile();
    }

    private void readyFile() throws SAXException, IOException, ParserConfigurationException {
        File file = new File(this.filePath);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        this.document = documentBuilder.parse(file);
    }

    public void processOperators(Transformer transformer){
        Node rootNode = document.getElementsByTagName(PARAMETER_FILE_ROOT).item(0);
        for (int i = 0; i < rootNode.getChildNodes().getLength(); i++) {
            Node operatorNode = rootNode.getChildNodes().item(i);
            if (operatorNode.getNodeName().equals(PROPERTY_OPERATOR)) {
                String name = EMPTY_STRING;
                String scope = EMPTY_STRING;
                for(int j=0;j<operatorNode.getAttributes().getLength();j++){
                    Node propertyNode = operatorNode.getAttributes().item(j);
                    if(propertyNode.getNodeName().equals(NAME)){
                        name = propertyNode.getNodeValue();
                    }else if(propertyNode.getNodeName().equals(SCOPE)){
                        scope = propertyNode.getNodeValue();
                    }
                }
                if(!(name.equals(EMPTY_STRING) || scope.equals(EMPTY_STRING))){
                    transformer.setParameter(name,scope);
                }
            }
        }
    }

    public Document getDocument(){
        return this.document;
    }
}

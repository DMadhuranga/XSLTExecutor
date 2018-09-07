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

import static execute.ExecutorConstants.COMMA;
import static execute.ExecutorConstants.EMPTY_STRING;
import static execute.ExecutorConstants.NAME;
import static execute.ExecutorConstants.PARAMETER_FILE_ROOT;
import static execute.ExecutorConstants.PROPERTY_OPERATOR;
import static execute.ExecutorConstants.RUN_TIME_PROPERTIES;
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
        String runTimeProperties = EMPTY_STRING;
        Node rootNode = document.getElementsByTagName(PARAMETER_FILE_ROOT).item(0);
        for (int i = 0; i < rootNode.getChildNodes().getLength(); i++) {
            for(int j=0;j<rootNode.getAttributes().getLength();j++){
                Node propertyNode = rootNode.getAttributes().item(j);
                if(propertyNode.getNodeName().equals(RUN_TIME_PROPERTIES)){
                    runTimeProperties = propertyNode.getNodeValue();
                }
            }
        }
        String[] properties = runTimeProperties.split(COMMA);
        int currentIndex = 0;
        while (currentIndex<properties.length){
            transformer.setParameter(properties[currentIndex],properties[currentIndex+1]);
            currentIndex+=2;
        }
    }
}

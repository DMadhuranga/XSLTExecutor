package execute;

import javax.xml.transform.*;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import net.sf.saxon.TransformerFactoryImpl;

public class Executor {
    public static void main(String args[]) throws Exception {
        File xsltFile = new File("/home/danushka/Downloads/output.xml");
        File inputXml = new File("/home/danushka/Downloads/input1.xml");

        Source xmlSource = new javax.xml.transform.stream.StreamSource(inputXml);
        Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);
        StringWriter sw = new StringWriter();

        Result result = new javax.xml.transform.stream.StreamResult(sw);

        TransformerFactory transFact = new TransformerFactoryImpl();
        Transformer trans = transFact.newTransformer(xsltSource);

        for (int i = 3; i < args.length; i++) {
            String[] parts = args[i].split("=");
            trans.setParameter(parts[0], parts[1]);
        }
        ParameterFileProcessor parameterFileProcessor = new ParameterFileProcessor("/home/danushka/Downloads/format.xml");
        parameterFileProcessor.processOperators(trans);

        trans.transform(xmlSource, result);
        File outputFile = new File("/home/danushka/Downloads/output1.xml");
        FileWriter output = new FileWriter(outputFile);
        output.write(sw.toString());
        output.close();
    }
}

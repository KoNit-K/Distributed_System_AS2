import java.io.*;
import java.io.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class FormatXML{

    //set the auto-indent to yes. and reformat xml input.
    // the result is formatted xml
    public static String prettyFormat(String input, int indent) {
        try
        {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            // This statement works with JDK 6
            transformerFactory.setAttribute("indent-number", indent);
             
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        }catch (Throwable e){
            return "";
        }
    }
 
    //get input and return formatted xml result
    public static String prettyFormat(String input) {
        //separe <?xml>  and <feed>
        String tmp = prettyFormat(input, 2);
        String result ="";
        String[] splited = tmp.split(">",2);
        result= splited[0]+">"+"\n"+splited[1];
        return result;
    }
}
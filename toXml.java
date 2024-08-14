import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.*;

public class toXml{
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private int length;

	//construct
	public toXml(){
		try{
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//get information from file and remove entry tag.
	//and call start() function if failed return "";
	public String readFile (String filename){
		String[] result =null;
		try{
			File file = new File(filename);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String input = ""; 
			result = new String[100];
			int counter =0;
			while ((input = br.readLine()) != null){
				String[] splited = input.split(":",2); 
				if(splited[0].equals("entry")){
					//do nothing
				}else if(splited[0].equals("")) {
					//do nothing
				}else{
					result[counter]= splited[1];					
					counter++;
					length++;
				}
			}  
			return start(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; 
	}
	//tranfer string array to a xml String. and return this result.
	//if failed return "";
	public String start(String[] input){
		try{
			// root element
			Element rootElement = doc.createElement("feed");
			doc.appendChild(rootElement);
			Attr attr = doc.createAttribute("xml:lang");
			attr.setValue("en-US");
			rootElement.setAttributeNode(attr);
			Attr attr1 = doc.createAttribute("xmlns");
			attr.setValue("http://www.w3.org/2005/Atom");
			rootElement.setAttributeNode(attr1);
			// create sub-element
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(input[0]));
			rootElement.appendChild(title);
			Element subtitle = doc.createElement("subtitle");
			subtitle.appendChild(doc.createTextNode(input[1]));
			rootElement.appendChild(subtitle);
			Element link = doc.createElement("link");
			link.appendChild(doc.createTextNode(input[2]));
			rootElement.appendChild(link);
			Element updated = doc.createElement("updated");
			updated.appendChild(doc.createTextNode(input[3]));
			rootElement.appendChild(updated);
			Element author = doc.createElement("author");
			author.appendChild(doc.createTextNode(input[4]));
			rootElement.appendChild(author);
			Element id = doc.createElement("id");
			id.appendChild(doc.createTextNode(input[5]));
			rootElement.appendChild(id);
			int counter =6;
			// add entry tag 
			while(counter<length){
				// entry element
				Element entry = doc.createElement("entry");
				rootElement.appendChild(entry);
				// carname element
				Element title1 = doc.createElement("title");
				title1.appendChild(doc.createTextNode(input[counter]));
				entry.appendChild(title1);
				Element link1 = doc.createElement("link");
				link1.appendChild(doc.createTextNode(input[counter+1]));
				entry.appendChild(link1);
				Element id1 = doc.createElement("id");
				id1.appendChild(doc.createTextNode(input[counter+2]));
				entry.appendChild(id1);
				Element updated1 = doc.createElement("updated");
				updated1.appendChild(doc.createTextNode(input[counter+3]));
				entry.appendChild(updated1);
				Element summary1 = doc.createElement("summary");
				summary1.appendChild(doc.createTextNode(input[counter+4]));
				entry.appendChild(summary1);
				counter += 5;
			}
			// write the content into Stringwriter
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			transformer.transform(source, result);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
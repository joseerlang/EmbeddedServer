package es.udc.irlab;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;
import org.xml.sax.SAXException;

/**
 * Hello world!
 * 
 */
public class SorlErlangServer {
	public static void main(String[] args) throws IOException,
			ParserConfigurationException, SAXException, SolrServerException {
		try {
			if (args.length != 4)
				throw new IOException(
						"Args must be 4 . Ex: SolrErlangServer  [indexSolr] [nodeName] [peerName] [cookieName]");
			System.setProperty("solr.solr.home", args[0]);
			CoreContainer.Initializer initializer = new CoreContainer.Initializer();
			CoreContainer coreContainer = initializer.initialize();
			EmbeddedSolrServer server = new EmbeddedSolrServer(coreContainer,
					"");
			
			SolrQuery query = new SolrQuery();
			query.setQuery("*:*");
			QueryResponse rsp = server.query(query);
			
			SolrDocumentList docs = rsp.getResults();
			System.out.println("Index with " + docs.size() + " documents");

			JInterfaceProxy main = new JInterfaceProxy(args[1], args[2],
					args[3],server);
			main.process();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
}

package es.udc.irlab;

import java.io.UnsupportedEncodingException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;

public class Task implements Runnable {

	private String query;
	private EmbeddedSolrServer server;
	private OtpMbox mbox;
	private OtpErlangPid from;

	public Task(String query, EmbeddedSolrServer server, OtpMbox mbox,
			OtpErlangPid from) {
		this.query = query;
		this.server = server;
		this.from = from;
		this.mbox = mbox;
	}

	public void run() {
		try {
			// TODO Auto-generated method stub
			String[] params = query.split("&");
			if (params.length > 0) {
				SolrQuery querySolr = new SolrQuery();
				for (String param : params) {
					String[] keyValue = param.split("=");
					String key = keyValue[0].trim();
					String value = keyValue[1].trim();
					if (key.equals("rows"))
						querySolr.setRows(Integer.parseInt(value));
					if (key.equals("start"))
						querySolr.setStart(Integer.parseInt(value));
					if (key.equals("q"))
						querySolr.setQuery(value);
				}
				querySolr.setHighlight(true);
				querySolr.setHighlightFragsize(200);
				querySolr.setHighlightSimplePre("<em>");
				querySolr.setHighlightSimplePre("</em>");

				QueryResponse rsp;
				rsp = server.query(querySolr);

				SolrDocumentList docs = rsp.getResults();

				StringBuffer textIndex = new StringBuffer(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<response>");
				StringBuffer textHeader = new StringBuffer(
						"<responseHeader><status>")
						.append(rsp.getStatus())
						.append("</status><QTime>")
						.append(rsp.getQTime())
						.append(
								"</QTime><lst name=\"params\"><str name=\"start\">")
						.append(rsp.getResults().getStart()).append(
								"</str><str name=\"q\">").append(
								((SimpleOrderedMap<String>) rsp.getHeader()
										.get("params")).get("q")).append(
								"</str><str name=\"rows\">"
										+ rsp.getResults().size()).append(
								"</str></lst></responseHeader>");
				StringBuffer highlightText = new StringBuffer(
						"<lst name=\"highlighting\">");
				textHeader.append("<result name=\"response\" numFound=\"")
						.append(docs.getNumFound()).append("\" start=\"")
						.append(docs.getStart()).append("\">");
				for (SolrDocument doc : docs) {
					textHeader.append("<doc><str name=\"id\">").append(
							doc.getFieldValue("id")).append("</str></doc>");
					highlightText.append("<lst name=\"").append(
							doc.getFieldValue("id")).append(
							"\"><arr name=\"html\"><str>").append(
							java.net.URLEncoder.encode(rsp.getHighlighting()
									.get(doc.getFieldValue("id")).get("html")
									.get(0), "UTF-8")).append(
							"</str></arr></lst>");
				}
				highlightText.append("</lst>");
				textIndex.append("\n").append(textHeader).append("</result>")
						.append(highlightText).append("\n</response>");
				OtpErlangTuple outMsg = new OtpErlangTuple(
						new OtpErlangObject[] {
								mbox.self(),
								new OtpErlangString(String.valueOf(textIndex
										.toString())) });
				mbox.send(from, outMsg);

			}
		} catch (SolrServerException e) {
			OtpErlangTuple outMsg = new OtpErlangTuple(new OtpErlangObject[] {
					mbox.self(),
					new OtpErlangString(String.valueOf(e.getMessage())) });
			mbox.send(from, outMsg);
		} catch (UnsupportedEncodingException e) {
			OtpErlangTuple outMsg = new OtpErlangTuple(new OtpErlangObject[] {
					mbox.self(),
					new OtpErlangString(String.valueOf(e.getMessage())) });
			mbox.send(from, outMsg);
			;
		}
	}

}

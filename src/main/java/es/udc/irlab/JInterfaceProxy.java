package es.udc.irlab;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

public class JInterfaceProxy {

	private OtpMbox mbox;
	private OtpNode node;
	private EmbeddedSolrServer server;
	private ExecutorService exe;
	private Task myTask;

	public JInterfaceProxy(String nodeName, String mboxName, String nameCookie,
			EmbeddedSolrServer server) throws IOException {
		super();
		node = new OtpNode(nodeName, nameCookie);
		exe = Executors.newFixedThreadPool(10);
		mbox = node.createMbox();
		mbox.registerName(mboxName);
		this.server = server;
	}

	public void process() {
		while (true) {
			OtpErlangObject o;
			try {
				o = mbox.receive();
				if (o instanceof OtpErlangTuple) {
					OtpErlangTuple msg = (OtpErlangTuple) o;
					OtpErlangPid from = (OtpErlangPid) msg.elementAt(0);
					String path = ((OtpErlangString) msg.elementAt(1))
							.toString();
					path = path.substring(1, path.length() - 1); // delete "
					// from
					// String.
					String query = ((OtpErlangString) msg.elementAt(2))
							.toString();
					query = query.substring(1, query.length() - 1); // delete "
					// from
					// String.
					myTask = new Task(query,server,mbox,from);
					exe.submit(myTask);
				}
			} catch (OtpErlangExit e) {
				e.printStackTrace(System.out);
			} catch (OtpErlangDecodeException e) {
				e.printStackTrace(System.out);
			}

		}
	}
}

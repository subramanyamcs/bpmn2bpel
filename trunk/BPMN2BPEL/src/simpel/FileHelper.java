// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package simpel;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Map;

import javax.media.jai.operator.AddDescriptor;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.bpel.model.Process;

import simpel.Builder.ImportDetails;
import simpel.Builder.ServiceDetails;

public class FileHelper {
	
	public static String buildPLFileContents(Process proc, Map<String, ServiceDetails> plMap, Map<String, ImportDetails> nsMap) {
		if (proc.getImports().size() == 0)
			return null;
		Namespace plnk, wsdl;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("definitions").
			addAttribute("targetNamespace", "http://tempuri.org/plks").
			addNamespace("tns", "http://tempuri.org/plks").
			addNamespace("plnk", "http://docs.oasis-open.org/wsbpel/2.0/plnktype").
			addNamespace("pns", "http://www.uatx.mx/uri").
			addNamespace("", "http://schemas.xmlsoap.org/wsdl/").addNamespace("wsdl", "http://schemas.xmlsoap.org/wsdl/");
		
		wsdl = root.getNamespaceForPrefix("wsdl");
		plnk = root.getNamespaceForPrefix("plnk");
		
		LinkedList<Element> imports = new LinkedList<Element>();
		LinkedList<Element> plnks = new LinkedList<Element>();
		
		for (ServiceDetails sd : plMap.values()) {
			String prefix = sd.porttype.getQName().getPrefix();
			String localpart = sd.porttype.getQName().getLocalPart();
			String uri = sd.porttype.getQName().getNamespaceURI();
			String location = nsMap.get(prefix).location;
			
			root.addNamespace(prefix, uri);
			
			Element imp = DocumentHelper.createElement(new QName("import", wsdl)).
				addAttribute("namespace", uri).addAttribute("location", location);
			imports.add(imp);
			
			Element plnkt = DocumentHelper.createElement(new QName("partnerLinkType", plnk)).
				addAttribute("name", sd.partnerlink.getName() + "Type");
			
			Element role = plnkt.addElement(new QName("role", plnk));
			if (sd.partnerlink.getMyRole() != null)
				role.addAttribute("name", sd.partnerlink.getMyRole().getName());
			else
				role.addAttribute("name", sd.partnerlink.getPartnerRole().getName());
			
			role.addAttribute("portType", prefix + ":" + localpart);
			plnks.add(plnkt);
		}
		
		for (Element imp : imports)
			root.add(imp);
		for (Element plnkt : plnks)
			root.add(plnkt);

		StringWriter out = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(out, format );
		
        try {
			writer.write( doc );
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out.toString();
	}

	public static String buildDeployFileContents(Process proc, Map<String, ServiceDetails> plMap, Map<String, ImportDetails> nsMap) {
		if (proc.getImports().size() == 0)
			return null;
		Document doc = DocumentHelper.createDocument();
		Namespace ns = DocumentHelper.createNamespace("", "http://www.apache.org/ode/schemas/dd/2007/03");
		Element root = doc.addElement(new QName("deploy", ns)).
			addNamespace("pns", proc.getTargetNamespace());
		
		Element pelement = root.addElement(new QName("process", ns)).
			addAttribute("name", "pns:" + proc.getName());
		pelement.addElement(new QName("active", ns)).addText("true");
		
		LinkedList<Element> invokes = new LinkedList<Element>();
		
		for (ServiceDetails sd : plMap.values()) {
			String prefix = sd.porttype.getQName().getPrefix();
			String uri = sd.porttype.getQName().getNamespaceURI();
			
			Element elem;
			if (sd.partnerlink.getMyRole() != null)
				elem = pelement.addElement(new QName("provide", ns));
			else {
				elem = DocumentHelper.createElement(new QName("invoke", ns));
				invokes.add(elem);
			}
			
			elem.addAttribute("partnerLink", sd.partnerlink.getName()).
				addElement("service").
					addNamespace(prefix, uri).
					addAttribute("name", sd.service).
					addAttribute("port", sd.port);
		}
		
		for (Element inv : invokes)
			pelement.add(inv);
		
		StringWriter out = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(out, format );
		
        try {
			writer.write( doc );
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return out.toString();
	}
}

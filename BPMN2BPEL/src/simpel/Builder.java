// Copyright (C) 2008 Luciano Garcia-Banuelos <lgbanuelos@gmail.com>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package simpel;

import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.BPELFactory;
import org.eclipse.bpel.model.Copy;
import org.eclipse.bpel.model.EventHandler;
import org.eclipse.bpel.model.Exit;
import org.eclipse.bpel.model.Expression;
import org.eclipse.bpel.model.From;
import org.eclipse.bpel.model.Import;
import org.eclipse.bpel.model.Invoke;
import org.eclipse.bpel.model.OnAlarm;
import org.eclipse.bpel.model.OnEvent;
import org.eclipse.bpel.model.OnMessage;
import org.eclipse.bpel.model.PartnerActivity;
import org.eclipse.bpel.model.PartnerLink;
import org.eclipse.bpel.model.PartnerLinks;
import org.eclipse.bpel.model.Pick;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Receive;
import org.eclipse.bpel.model.Reply;
import org.eclipse.bpel.model.Throw;
import org.eclipse.bpel.model.To;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.Variables;
import org.eclipse.bpel.model.Wait;
import org.eclipse.bpel.model.partnerlinktype.PartnerLinkType;
import org.eclipse.bpel.model.partnerlinktype.PartnerlinktypeFactory;
import org.eclipse.bpel.model.partnerlinktype.Role;
import org.eclipse.bpel.model.proxy.MessageProxy;
import org.eclipse.bpel.model.proxy.OperationProxy;
import org.eclipse.bpel.model.proxy.PortTypeProxy;
import org.eclipse.emf.common.util.URI;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;

import uk.co.badgersinfoil.e4x.antlr.LinkedListToken;
import uk.co.badgersinfoil.e4x.antlr.LinkedListTree;

public class Builder {
	public static String plknamespace = "http://tempuri.org/plks";
	private Object creationActivity = null;
	private Assign lastAssign = null;
	
	private Object context = null;
	
	
	class ServiceDetails {
		String service;
		String port;
		PortType porttype;
		PartnerLink partnerlink;
		
		ServiceDetails(PartnerLink partnerlink, PortType porttype, String service, String port) {
			this.service = service;
			this.port = port;
			this.porttype = porttype;
			this.partnerlink = partnerlink;
		}
	}
	
	class ImportDetails {
		String prefix;
		String namespace;
		String location;
		ImportDetails(String p, String n, String l) {
			prefix = p;
			namespace = n;
			location = l;
		}
	}
	
	static class TimeExpression {
		enum TYPE {FOR, UNTIL};
	}
	
	class TimeExprDetails {
		TimeExpression.TYPE type;
		Object bpelObj;
	}
	
	public void resetAssign() { lastAssign = null; }
	public static String text(org.antlr.runtime.tree.Tree t) {
    	if (t == null) return null;
    	else return t.getText();
    }

	public static String deepText(org.antlr.runtime.tree.Tree t) {
    	LinkedListTree llt = ((LinkedListTree)t);
    	StringBuffer b = new StringBuffer();
    	LinkedListToken tok = ((LinkedListTree)t).getStartToken();
    	b.append(tok.getText());
    	while(tok != llt.getStopToken() && (tok = tok.getNext()) != null)
	    if (tok.getText() != null) b.append(tok.getText());
        return b.toString();
    }

	public void initPartnerActivity(PartnerActivity act, String partnerLink, String operation) {
        ServiceDetails details = plMap.get(partnerLink);
        act.setPartnerLink(details.partnerlink);
        act.setPortType(details.porttype);
        Operation oper = new OperationProxy(URI.createURI(details.porttype
				.getQName().getNamespaceURI()), details.porttype, operation);
        act.setOperation(oper);
	}
	
	public Object createReceive(LinkedListTree partnerLink, LinkedListTree operation, LinkedListTree correlation) {
        System.out.println("Recibir de partnerLink:" + text(partnerLink) + " operation:" + text(operation)); 
        Receive receive = BPELFactory.eINSTANCE.createReceive();
        initPartnerActivity(receive, text(partnerLink), text(operation));
        if (creationActivity == null) {
        	receive.setCreateInstance(true);
        	creationActivity = receive;
        }
		return receive;
	}

	public Object createReply(LinkedListTree partnerLink, LinkedListTree operation, LinkedListTree variable, LinkedListTree c) {
        System.out.println("Respone de partnerLink:" + text(partnerLink) + " operation:" + text(operation)); 
        Reply reply = BPELFactory.eINSTANCE.createReply();
        initPartnerActivity(reply, text(partnerLink), text(operation));
        reply.setVariable(varMap.get(text(variable)));
		return reply;
	}

	public Object createInvoke(LinkedListTree partnerLink, LinkedListTree operation,
			LinkedListTree inputVariable, LinkedListTree c) {
        System.out.println("Invocar de partnerLink:" + text(partnerLink) + " operation:" + text(operation)); 
        Invoke invoke = BPELFactory.eINSTANCE.createInvoke();
        initPartnerActivity(invoke, text(partnerLink), text(operation));
        if (inputVariable != null)
        	invoke.setInputVariable(varMap.get(text(inputVariable)));
		return invoke;
	}
	
	private Process process = null;
	public void setProcess(Process process) {
		this.process = process;
		Variables vars = BPELFactory.eINSTANCE.createVariables();
		process.setVariables(vars);
		process.getImports().clear();
		PartnerLinks pls = BPELFactory.eINSTANCE.createPartnerLinks();
		process.setPartnerLinks(pls);
	}

	private HashMap<String, ImportDetails> nsMap = new HashMap<String, ImportDetails>();
	private HashMap<String, Variable> varMap = new HashMap<String, Variable>();
	private HashMap<String, ServiceDetails> plMap = new HashMap<String, ServiceDetails>();
	
	public String getService(String plname) {
		return plMap.get(plname).service;
	}
	
	public String getPort(String plname) {
		return plMap.get(plname).port;
	}

	public void addNamespace(LinkedListTree prefix, LinkedListTree uri,
			LinkedListTree loc) {
		String _uri = text(uri).substring(1, text(uri).length() - 1);
		String _loc = text(loc).substring(1, text(loc).length() - 1);
		
		nsMap.put(text(prefix), new ImportDetails(text(prefix), _uri, _loc));
		System.out.printf("Agregar namespace %s=%s", text(prefix), text(uri));
		Import imp = BPELFactory.eINSTANCE.createImport();
		imp.setNamespace(_uri);
		imp.setLocation(_loc);
		imp.setImportType("http://schemas.xmlsoap.org/wsdl/");
		process.getImports().add(imp);
		
		if (nsMap.size() == 1) {
			process.setTargetNamespace(_uri);
		}
	}

	public void setOutputVariable(Object rvObj, LinkedListTree lv) {
		String varname = deepText(lv);
		Variable variable = varMap.get(varname);
		if (rvObj instanceof Receive)
			((Receive)rvObj).setVariable(variable);
		else if (rvObj instanceof Invoke)
			((Invoke)rvObj).setOutputVariable(variable);
		else if (rvObj instanceof OnMessage)
			((OnMessage)rvObj).setVariable(variable);
		else
			((OnEvent)rvObj).setVariable(variable);
	}

	public Object createAssign(LinkedListTree lv, LinkedListTree rv) {
		if (lastAssign == null)
			lastAssign = BPELFactory.eINSTANCE.createAssign();
		From from = BPELFactory.eINSTANCE.createFrom();
		Expression exp = BPELFactory.eINSTANCE.createExpression();
		String str = deepText(rv);
		exp.setBody(str.substring(1, str.length() - 1));
		from.setExpression(exp);
		To to = BPELFactory.eINSTANCE.createTo();
		exp = BPELFactory.eINSTANCE.createExpression();
		exp.setBody("$" + deepText(lv));
		to.setExpression(exp);
		Copy copy = BPELFactory.eINSTANCE.createCopy();
		copy.setFrom(from);
		copy.setTo(to);
		lastAssign.getCopy().add(copy);
		return lastAssign;
	}

	public QName mapQName(String str) {
		String prefix = "";
		String uri = "";
		String loc = "";
		
		StringTokenizer tokenizer = new StringTokenizer(str, ":");
		if (tokenizer.countTokens() == 2) {
			prefix = tokenizer.nextToken();
			uri = nsMap.get(prefix).namespace;
		} else
			uri = process.getTargetNamespace();
		
		loc = tokenizer.nextToken();
		
		return new QName(uri, loc, prefix);
	}
	
	public void addPartner(LinkedListTree _partnervar, LinkedListTree _porttype,
			LinkedListTree _service, LinkedListTree _port) {
		System.out.printf("Agregar partner %s %s %s/%s\n", text(_partnervar), deepText(_porttype), deepText(_service), text(_port));		
		String partner = text(_partnervar);

		// PartnerLinkType
		PartnerLinkType pltype = PartnerlinktypeFactory.eINSTANCE.createPartnerLinkType();
		pltype.setName("tns:" + partner + "Type");

		QName ptQName = mapQName(deepText(_porttype));
		PortTypeProxy porttype = new PortTypeProxy(URI.createURI(ptQName.getNamespaceURI()), ptQName);
		
		// Role
		Role role = PartnerlinktypeFactory.eINSTANCE.createRole();
		role.setName(partner + "Provider");
		role.setPortType(porttype);
		
		// Partner
		PartnerLink partnerlink = BPELFactory.eINSTANCE.createPartnerLink();
		partnerlink.setName(partner);
		partnerlink.setPartnerLinkType(pltype);
		partnerlink.setPartnerRole(role);

		process.getPartnerLinks().getChildren().add(partnerlink);
		plMap.put(partner, new ServiceDetails(partnerlink, porttype, deepText(_service), text(_port)));
	}

	public void addClient(LinkedListTree _partnervar, LinkedListTree _porttype,
			LinkedListTree _service, LinkedListTree _port) {
		System.out.printf("Agregar client %s %s %s/%s\n", text(_partnervar), deepText(_porttype), deepText(_service), text(_port));
		String partner = text(_partnervar);

		// PartnerLinkType
		PartnerLinkType pltype = PartnerlinktypeFactory.eINSTANCE.createPartnerLinkType();
		pltype.setName("tns:" + partner + "Type");

		QName ptQName = mapQName(deepText(_porttype));
		PortTypeProxy porttype = new PortTypeProxy(URI.createURI(ptQName.getNamespaceURI()), ptQName);
		
		// Role
		Role role = PartnerlinktypeFactory.eINSTANCE.createRole();
		role.setName(partner + "Provider");
		role.setPortType(porttype);
		
		// Partner
		PartnerLink partnerlink = BPELFactory.eINSTANCE.createPartnerLink();
		partnerlink.setName(partner);
		partnerlink.setPartnerLinkType(pltype);
		partnerlink.setMyRole(role);

		process.getPartnerLinks().getChildren().add(partnerlink);
		plMap.put(partner, new ServiceDetails(partnerlink, porttype, deepText(_service), text(_port)));
	}

	public void addVariable(LinkedListTree id, LinkedListTree vartype) {
		QName qName = null;
		String varname = deepText(id);
		Variable variable = BPELFactory.eINSTANCE.createVariable();
		variable.setName(varname);
		String type = deepText(vartype);
		if (type.equals("int") || type.equals("float") || type.equals("string"))
			qName = new QName("http://www.w3.org/2001/XMLSchema", type, "xs");
		else
			qName = mapQName(type.substring(1, type.length() - 1));
		Message messageType = new MessageProxy(URI.createURI(qName.getNamespaceURI()), qName);
		variable.setMessageType(messageType);		
		process.getVariables().getChildren().add(variable);
		varMap.put(varname, variable);
	}

	public void setProcessName(LinkedListTree processName) {
		process.setName(text(processName));
		
		// Add the import for the WSDL file which declares the partnerLinks
		Import imp = BPELFactory.eINSTANCE.createImport();
		imp.setNamespace(plknamespace);
		imp.setLocation(process.getName() + "_plnk.wsdl");
		imp.setImportType("http://schemas.xmlsoap.org/wsdl/");
		process.getImports().add(imp);
	}
	public HashMap<String, ImportDetails> getNsMap() {
		return nsMap;
	}
	public HashMap<String, ServiceDetails> getPlMap() {
		return plMap;
	}
	public Object createOnMessage(LinkedListTree p, LinkedListTree o,
			LinkedListTree c) {
		Object act = null;
		if (context == null)
			act = createReceive(p, o, c);
		if (context instanceof Pick) {
			OnMessage onMessage = BPELFactory.eINSTANCE.createOnMessage();
			
	        ServiceDetails details = plMap.get(text(p));
	        onMessage.setPartnerLink(details.partnerlink);
	        onMessage.setPortType(details.porttype);
	        Operation oper = new OperationProxy(URI.createURI(details.porttype
					.getQName().getNamespaceURI()), details.porttype, text(o));
	        onMessage.setOperation(oper);

	        act = onMessage;
		}
		else if (context instanceof EventHandler) {
			OnEvent onEvent = BPELFactory.eINSTANCE.createOnEvent();
			
	        ServiceDetails details = plMap.get(text(p));
	        onEvent.setPartnerLink(details.partnerlink);
	        onEvent.setPortType(details.porttype);
	        Operation oper = new OperationProxy(URI.createURI(details.porttype
					.getQName().getNamespaceURI()), details.porttype, text(o));
	        onEvent.setOperation(oper);

	        act = onEvent;
		}
		
		return act;
	}
	public Object createOnAlarm(Object time_exp) {
		Object result = null;
		TimeExprDetails details = (TimeExprDetails) time_exp;
		
		if (context == null) {
			Wait wait = BPELFactory.eINSTANCE.createWait();
			if (details.type == TimeExpression.TYPE.FOR)
				wait.setFor((Expression) details.bpelObj);
			else
				wait.setUntil((Expression) details.bpelObj);
			result = wait;
		} else if (context instanceof Pick || context instanceof EventHandler) {
			OnAlarm onAlarm = BPELFactory.eINSTANCE.createOnAlarm();
			if (details.type == TimeExpression.TYPE.FOR)
				onAlarm.setFor((Expression) details.bpelObj);
			else
				onAlarm.setUntil((Expression) details.bpelObj);
			result = onAlarm;
		}
		return result;
	}
	public Object createForTimeExpression(LinkedListTree timeexp) {
		TimeExprDetails details = new TimeExprDetails();
		details.type = TimeExpression.TYPE.FOR;
		
		Expression exp = BPELFactory.eINSTANCE.createExpression();
		String tstr = deepText(timeexp);
		exp.setBody(tstr.substring(1, tstr.length() - 1));
		
		details.bpelObj = exp;
		return details;
	}
	public Object createUntilTimeExpression(LinkedListTree timeexp) {
		TimeExprDetails details = new TimeExprDetails();
		details.type = TimeExpression.TYPE.UNTIL;
		
		Expression exp = BPELFactory.eINSTANCE.createExpression();
		String tstr = deepText(timeexp);
		exp.setBody(tstr.substring(1, tstr.length() - 1));
		
		details.bpelObj = exp;
		return details;
	}
	public Object createThrow(LinkedListTree exception_name) {
		Throw result = BPELFactory.eINSTANCE.createThrow();
		result.setFaultName(mapQName(deepText(exception_name)));
		return result;
	}
	public Object createExit() {
		Exit result = BPELFactory.eINSTANCE.createExit();
		return result;
	}
	public void setContext(Object context) {
		this.context = context;
	}
	public Object getContext() {
		return context;
	}
	public void resetContext() {
		context = null;
	}
}

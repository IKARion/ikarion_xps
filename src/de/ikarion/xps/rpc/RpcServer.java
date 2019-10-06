package de.ikarion.xps.rpc;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

/**
 * XML-RPC server for apache ws-xmlrpc Version 1.2
 */

public class RpcServer {

    private int port;
    private WebServer webServer = null;
    private PropertyHandlerMapping phm = null;
    private RequestHandler handler = null;
    private XmlRpcServer xmlRpcServer = null;

    public RpcServer(int port) {
        this.port = port;
        this.webServer = new WebServer(this.port);       
        this.xmlRpcServer = this.webServer.getXmlRpcServer();
        this.xmlRpcServer.setTypeFactory(new TypeFactory(this.xmlRpcServer));
        this.handler = new RequestHandler();
        this.phm = new PropertyHandlerMapping();
        this.phm.setRequestProcessorFactoryFactory(this.handler);
    }

    public void addHandler(String name, Object requestHandler) throws IOException {
        this.handler.setHandler(name, requestHandler);
        try {
            this.phm.addHandler(name, requestHandler.getClass());
        } catch (XmlRpcException e) {
            throw new IOException(e);
        }
    }

    public void serve_forever() throws IOException {
        this.xmlRpcServer.setHandlerMapping(phm);
        XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
        serverConfig.setEnabledForExtensions(true);
        this.webServer.start();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public WebServer getWebServer() {
        return webServer;
    }

    public void setWebServer(WebServer webServer) {
        this.webServer = webServer;
    }

    public PropertyHandlerMapping getPhm() {
        return phm;
    }

    public void setPhm(PropertyHandlerMapping phm) {
        this.phm = phm;
    }

    public RequestHandler getHandler() {
        return handler;
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }

    public XmlRpcServer getXmlRpcServer() {
        return xmlRpcServer;
    }

    public void setXmlRpcServer(XmlRpcServer xmlRpcServer) {
        this.xmlRpcServer = xmlRpcServer;
    }

}
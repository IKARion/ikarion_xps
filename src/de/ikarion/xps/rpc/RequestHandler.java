package de.ikarion.xps.rpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestProcessorFactory;

/**
 * XMLRPC request handler factory
 */

public class RequestHandler implements RequestProcessorFactoryFactory, RequestProcessorFactory {
    
    private Map<String, Object> handlerMap = null;

    public void setHandler(String name, Object handler) {
        this.handlerMap = new HashMap<String, Object>();
        this.handlerMap.put(name, handler);
    }

    public Object getHandler(String name) {
        return this.handlerMap.get(name);
    }

    public RequestProcessorFactory getRequestProcessorFactory(Class arg) throws XmlRpcException {
        return this;
    }

    public Object getRequestProcessor(XmlRpcRequest request) throws XmlRpcException {
        String handlerName = request.getMethodName().substring(0, request.getMethodName().lastIndexOf("."));
        if (!handlerMap.containsKey(handlerName)) {
            throw new XmlRpcException("Unknown handler: " + handlerName);
        }
        return handlerMap.get(handlerName);
    }

}
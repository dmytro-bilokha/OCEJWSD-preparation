package bilokhado.countrysoapclient;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to setup client-side handlers
 */
public class ClientHandlerResolver implements HandlerResolver {

    private String key;

    public ClientHandlerResolver(String key) {
        this.key = key;
    }

    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<>();
        handlerChain.add(new ClientHashHandler(key));
        return handlerChain;
    }

}

package bilokhado.tomcatmetrosoap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Set;

/**
 * Class represents SOAP handler to check signatures from the client
 */
public class ServiceHashHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceHashHandler.class);

    byte[] keyBytes = getBytes("lookslikekeyisnotsecure");

    private byte[] getBytes(String data) {
        if (data == null)
            return new byte[0];
        try {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("UTF-8 encoding is needed, but not supported");
        }
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean directionIsOut = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (!directionIsOut) {
            verifySignature(context.getMessage());
        }
        return true;
    }

    private void verifySignature(SOAPMessage message) {
        SOAPHeader header = null;
        try {
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
            header = envelope.getHeader();
        } catch (SOAPException ex) {
            LOG.error("Got SOAPException", ex);
            return;
        }
        if (header == null) {
            LOG.error("Message has no header");
            return;
        }
        Node node = header.getFirstChild();
        NodeList nodeList = node.getChildNodes();
        if (nodeList.getLength() != 2) {
            LOG.error("Got {} header child nodes instead of 2 needed", nodeList.getLength());
            return;
        }
        String timestamp = nodeList.item(0).getFirstChild().getNodeValue();
        String signature = nodeList.item(1).getFirstChild().getNodeValue();
        LOG.info("Got from client request timestamp='{}', signature='{}'", timestamp, signature);
        if (timestamp == null || signature == null)
            return;
        if (getSignature(timestamp, keyBytes).equals(signature))
            LOG.info("Signature verified and it is OK");
        else
            LOG.error("Signature verified and it is NOT GOOD");
    }

    private String getSignature(String data, byte[] keyBytes) {
        try {
            byte[] bytesToSign = getBytes(data);
            Mac signer = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
            signer.init(keySpec);
            signer.update(bytesToSign);
            byte[] signatureBytes = signer.doFinal();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new IllegalStateException("Unable to sign", ex);
        }
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}

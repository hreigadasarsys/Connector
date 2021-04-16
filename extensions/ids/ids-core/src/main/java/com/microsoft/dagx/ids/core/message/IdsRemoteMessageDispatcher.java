package com.microsoft.dagx.ids.core.message;

import com.microsoft.dagx.spi.DagxException;
import com.microsoft.dagx.spi.message.RemoteMessageDispatcher;
import com.microsoft.dagx.spi.types.domain.message.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Binds and sends remote messages using the IDS REST protocol by dispatching to {@link IdsMessageSender}s.
 */
public class IdsRemoteMessageDispatcher implements RemoteMessageDispatcher {
    private Map<Class<? extends RemoteMessage>, IdsMessageSender<? extends RemoteMessage, ?>> senders = new HashMap<>();

    public void register(IdsMessageSender<? extends RemoteMessage, ?> handler) {
        senders.put(handler.messageType(), handler);
    }

    @Override
    public String protocol() {
        return "ids-rest";
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> send(Class<T> responseType, RemoteMessage message) {
        Objects.requireNonNull(message, "Message was null");
        IdsMessageSender<RemoteMessage, ?> handler = (IdsMessageSender<RemoteMessage, ?>) senders.get(message.getClass());
        if (handler == null) {
            throw new DagxException("Message sender not found for message type: " + message.getClass().getName());
        }
        return (CompletableFuture<T>) handler.send(message);
    }

}

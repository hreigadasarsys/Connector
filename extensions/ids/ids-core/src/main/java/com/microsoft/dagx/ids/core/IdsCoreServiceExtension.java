package com.microsoft.dagx.ids.core;

import com.microsoft.dagx.ids.core.daps.DapsServiceImpl;
import com.microsoft.dagx.ids.core.descriptor.IdsDescriptorServiceImpl;
import com.microsoft.dagx.ids.core.message.DataRequestMessageSender;
import com.microsoft.dagx.ids.core.message.IdsRemoteMessageDispatcher;
import com.microsoft.dagx.ids.spi.daps.DapsService;
import com.microsoft.dagx.ids.spi.descriptor.IdsDescriptorService;
import com.microsoft.dagx.spi.iam.IdentityService;
import com.microsoft.dagx.spi.monitor.Monitor;
import com.microsoft.dagx.spi.message.RemoteMessageDispatcherRegistry;
import com.microsoft.dagx.spi.system.ServiceExtension;
import com.microsoft.dagx.spi.system.ServiceExtensionContext;
import okhttp3.OkHttpClient;

import java.util.Set;

import static com.microsoft.dagx.ids.spi.IdsConfiguration.CONNECTOR_NAME;

/**
 * Implements the IDS Controller REST API.
 */
public class IdsCoreServiceExtension implements ServiceExtension {
    private Monitor monitor;

    @Override
    public Set<String> provides() {
        return Set.of("ids.core");
    }

    @Override
    public Set<String> requires() {
        return Set.of("iam");
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        monitor = context.getMonitor();

        var descriptorService = new IdsDescriptorServiceImpl();
        context.registerService(IdsDescriptorService.class, descriptorService);

        var identityService = context.getService(IdentityService.class);
        var connectorName = context.getSetting(CONNECTOR_NAME, "connectorName");
        var dapsService = new DapsServiceImpl(connectorName, identityService);
        context.registerService(DapsService.class, dapsService);

        assembleIdsDispatcher(context, identityService);

        monitor.info("Initialized IDS Core extension");
    }

    @Override
    public void start() {
        monitor.info("Started IDS Core extension");
    }

    @Override
    public void shutdown() {
        monitor.info("Shutdown IDS Core extension");
    }

    /**
     * Assembles the IDS remote message dispatcher and its senders.
     */
    private void assembleIdsDispatcher(ServiceExtensionContext context, IdentityService identityService) {
        var httpClient = context.getService(OkHttpClient.class);

        var dispatcher = new IdsRemoteMessageDispatcher();

        dispatcher.register(new DataRequestMessageSender(identityService, httpClient));

        var registry = context.getService(RemoteMessageDispatcherRegistry.class);
        registry.register(dispatcher);
    }

}

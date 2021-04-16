package com.microsoft.dagx.spi.types.domain.transfer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.microsoft.dagx.spi.types.domain.Polymorphic;
import com.microsoft.dagx.spi.types.domain.message.RemoteMessage;
import com.microsoft.dagx.spi.types.domain.metadata.DataEntry;

/**
 * Polymorphic data request.
 */
@JsonTypeName("dagx:datarequest")
@JsonDeserialize(builder = DataRequest.Builder.class)
public class DataRequest implements RemoteMessage, Polymorphic {
    private String id;

    private String connectorAddress;

    private String protocol;

    private String connectorId;

    private DataEntry<?> dataEntry;

    private DataTarget dataTarget;

    /**
     * The unique request id.
     */
    public String getId() {
        return id;
    }

    public String getConnectorAddress() {
        return connectorAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public DataEntry<?> getDataEntry() {
        return dataEntry;
    }

    private DataRequest() {
    }

    public DataTarget getDataTarget() {
        return dataTarget;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final DataRequest request;

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public DataRequest build() {
            return request;
        }

        public Builder id(String id) {
            request.id = id;
            return this;
        }

        public Builder connectorAddress(String address) {
            request.connectorAddress = address;
            return this;
        }

        public Builder protocol(String protocol) {
            request.protocol = protocol;
            return this;
        }

        public Builder connectorId(String connectorId) {
            request.connectorId = connectorId;
            return this;
        }

        public Builder dataEntry(DataEntry<?> entry) {
            request.dataEntry = entry;
            return this;
        }

        public Builder dataTarget(DataTarget target){
            request.dataTarget= target;
            return this;
        }

        private Builder() {
            request = new DataRequest();
        }
    }
}

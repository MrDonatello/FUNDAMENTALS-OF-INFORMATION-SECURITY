package signature.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
public class StorageProperties {

    private String locationNotSigned = "documents/not signed";
    private String locationSigned = "documents/signed";
    private String locationKeys = "documents/public keys";

    public StorageProperties() {
    }

    public String getLocationNotSigned() {
        return locationNotSigned;
    }

    public void setLocationNotSigned(String locationNotSigned) {
        this.locationNotSigned = locationNotSigned;
    }

    public String getLocationSigned() {
        return locationSigned;
    }

    public void setLocationSigned(String locationSigned) {
        this.locationSigned = locationSigned;
    }

    public String getLocationKeys() {
        return locationKeys;
    }

    public void setLocationKeys(String locationKeys) {
        this.locationKeys = locationKeys;
    }
}

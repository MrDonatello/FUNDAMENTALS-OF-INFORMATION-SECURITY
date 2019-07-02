package signature.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("service")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String locationNotSigned = "documents/not signed";
    private String locationSigned = "documents/signed";

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
}

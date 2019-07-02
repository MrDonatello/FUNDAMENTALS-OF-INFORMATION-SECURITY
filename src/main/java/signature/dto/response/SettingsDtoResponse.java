package signature.dto.response;

import org.springframework.stereotype.Component;

@Component
public class SettingsDtoResponse {

    private int maxNameLength;
    private int minPasswordLength;

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }
}

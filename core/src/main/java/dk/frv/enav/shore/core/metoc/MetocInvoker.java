package dk.frv.enav.shore.core.metoc;

import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;

public interface MetocInvoker {
    MetocForecastResponse makeRequest() throws MetocInvokerException;
}

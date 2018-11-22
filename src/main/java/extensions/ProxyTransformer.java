package extensions;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.servlet.WireMockHttpServletRequestAdapter;

import java.lang.reflect.Field;
import java.util.Objects;

public class ProxyTransformer extends ResponseDefinitionTransformer {
    public static final String NAME = "devops-proxy";
    public static final String PREFIX_FIELD_NAME = "urlPrefixToRemove";

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        if (Objects.isNull(parameters) || parameters.isEmpty()) {
            return responseDefinition;
        }

        try {
            String urlPrefixToRemove = parameters.getString(PREFIX_FIELD_NAME);
            Field field = WireMockHttpServletRequestAdapter.class.getDeclaredField(PREFIX_FIELD_NAME);
            field.setAccessible(true);
            field.set(request, urlPrefixToRemove);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseDefinition;
    }

    public String getName() {
        return NAME;
    }
}
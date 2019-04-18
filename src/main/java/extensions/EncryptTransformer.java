package extensions;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class EncryptTransformer extends ResponseDefinitionTransformer {
    private static final String NAME = "mock-encrypt";

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        List<String> transformers = responseDefinition.getTransformers();

        if (Objects.nonNull(transformers) && transformers.contains(NAME)) {
            final String bodyText = responseDefinition.getBody();
            final String bodyFileName = responseDefinition.getBodyFileName();
            String body = "";

            if (Objects.nonNull(bodyText)) {
                body = encrypt(bodyText);
            } else if (StringUtils.isNotBlank(bodyFileName)) {
                TextFile textFile = files.getTextFileNamed(bodyFileName);
                String fileContent = textFile.readContentsAsString();
                body = encrypt(fileContent);
            }

            return new ResponseDefinitionBuilder()
                    .withHeaders(responseDefinition.getHeaders())
                    .withBody(body)
                    .build();
        }

        return responseDefinition;
    }

    private String encrypt(String content) {
        return Base64.getEncoder().encodeToString(content.getBytes());
    }

    public String getName() {
        return NAME;
    }
}

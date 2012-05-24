package com.heroku.api.request.sharing;

import com.heroku.api.Heroku;
import com.heroku.api.exception.RequestFailedException;
import com.heroku.api.http.Http;
import com.heroku.api.http.HttpUtil;
import com.heroku.api.request.Request;
import com.heroku.api.request.RequestConfig;
import com.heroku.api.response.Unit;

import java.util.Collections;
import java.util.Map;

/**
 * TODO: Javadoc
 *
 * @author James Ward
 */
public class SharingRemove implements Request<Unit> {

    // delete("/apps/#{app_name}/collaborators/#{escape(email)}").to_s

    private final RequestConfig config;

    public SharingRemove(String appName, String collaboratorEmail) {
        this.config = new RequestConfig().app(appName).with(Heroku.RequestKey.Collaborator, collaboratorEmail);
    }

    @Override
    public Http.Method getHttpMethod() {
        return Http.Method.DELETE;
    }

    @Override
    public String getEndpoint() {
        return Heroku.Resource.Collaborator.format(config.get(Heroku.RequestKey.AppName),
                HttpUtil.urlencode(config.get(Heroku.RequestKey.Collaborator), "Unable to encode the endpoint"));
    }

    @Override
    public boolean hasBody() {
        return false;
    }

    @Override
    public String getBody() {
        throw HttpUtil.noBody();
    }

    @Override
    public Http.Accept getResponseType() {
        return Http.Accept.TEXT;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public Unit getResponse(byte[] in, int code) {
        if (code == Http.Status.OK.statusCode)
            return Unit.unit;
        else
            throw new RequestFailedException("SharingRemove failed", code, in);
    }
}
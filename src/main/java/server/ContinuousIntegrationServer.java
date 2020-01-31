package server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;

import ci.ProjectTester;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import org.jetbrains.annotations.NotNull;
import org.json.*;
import utilities.Helpers;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook
 * See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {
    private static final Logger logger = Logger.getLogger(ContinuousIntegrationServer.class);

    @Override
    public void handle(String target, @NotNull Request baseRequest, @NotNull HttpServletRequest request, @NotNull HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String payload = IOUtils.toString(request.getReader());
            try {
                JSONObject jsonObject = new JSONObject(payload);
                ProjectTester projectTester = new ProjectTester(jsonObject);
                projectTester.processPush();
            } catch (JSONException e) {
                logger.error("Server failed while parsing received payload", e);
            }
        }
        response.getWriter().println("CI job running");
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Helpers.setUpConfiguration(args);
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
package de.hessen.hzd.concurrency;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    @Resource(lookup = "java:jboss/ee/concurrency/executor/batchMainThreadExecutorService")
    private ManagedExecutorService executorService;

    @Inject
    private PollMembersTask task;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Future<Integer> submit = executorService.submit(task);
        Integer integer = null;
        try {
            integer = submit.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        PrintWriter writer = resp.getWriter();
        writer.println(integer);
        writer.close();
    }
}

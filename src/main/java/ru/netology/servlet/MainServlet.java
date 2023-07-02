package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.netology.constants.Methods;
import ru.netology.controller.PostController;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {
    private PostController controller;
    private final static String PATH_NAME = "/api/posts";

    @Override
    public void init() {
        // отдаём список пакетов, в которых нужно искать аннотированные классы
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.netology");

        // получаем по имени бина
        controller = context.getBean(PostController.class); 

        // получаем по классу бина
        final var service = context.getBean(PostService.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            // primitive routing
            if (method.equals(Methods.GET.name()) && path.equals(PATH_NAME)) {
                controller.all(resp);
                return;
            }
            if (method.equals(Methods.GET.name()) && path.matches(PATH_NAME +"/\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(Methods.POST.name()) && path.equals(PATH_NAME)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(Methods.DELETE.name()) && path.matches(PATH_NAME +"/\\d+")) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}


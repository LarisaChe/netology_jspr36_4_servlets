package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    final var data = service.all();
    final var gson = new Gson();
    sendResponse(gson.toJson(data), response);
  }

  public void getById(long id, HttpServletResponse response) throws IOException {
    final var data = service.getById(id);
    final var gson = new Gson();
    sendResponse(gson.toJson(data), response);
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    final var gson = new Gson();
    final var post = gson.fromJson(body, Post.class);
    final var data = service.save(post);
    sendResponse(gson.toJson(data), response);
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    service.removeById(id);
    final var gson = new Gson();
    final var data = service.all();
    sendResponse(gson.toJson(data), response);
  }

  private static void sendResponse(String gsonData, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    response.getWriter().print(gsonData);
  }
}

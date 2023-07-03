package ru.netology.repository;

import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PostRepository {

    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private static long nextId = 1;

    public List<Post> all() {
        return posts.values().stream().collect(Collectors.toList()); //Collections.emptyList();
    }

    public Optional<Post> getById(long id) {
        if (posts.containsKey(id)) {
            return Optional.ofNullable(posts.get(id));
        }
        else {
            return Optional.empty();
        }
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            while (posts.containsKey(nextId)) {
                nextId++;
            }
            post.setId(nextId);
            nextId++;
        }
        posts.put(post.getId(), post);
        return posts.get(post.getId());
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}

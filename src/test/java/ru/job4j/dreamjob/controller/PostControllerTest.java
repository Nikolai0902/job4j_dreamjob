package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post"),
                new Post(2, "New post")
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);

        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        HttpSession httpSession = mock(HttpSession.class);
        String page = postController.posts(model, httpSession);

        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenFormAddPost() {
        CityService city = new CityService();
        List<City> cities = Arrays.asList(
                city.findById(1),
                city.findById(2)
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        HttpSession httpSession = mock(HttpSession.class);

        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.formAddPost(model, httpSession);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("addPost"));
    }

    @Test
    public void whenCreatePost() {
        int id = 1;
        Post input = new Post(id, "New post");
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.createPost(input, id);
        verify(postService).add(input);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        CityService city = new CityService();
        List<City> cities = Arrays.asList(
                city.findById(1),
                city.findById(2)
        );
        int id = 1;
        Post input = new Post(id, "New post");

        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        HttpSession httpSession = mock(HttpSession.class);

        when(postService.findById(id)).thenReturn(input);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.formUpdatePost(model, id, httpSession);
        verify(model).addAttribute("post", input);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("updatePost"));
    }

    @Test
    public void whenUpdatePost() {
        CityService city = new CityService();
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        LocalDate localDate = LocalDate.of(2022, 12, 17);

        Post input = new Post(1, "New post", "New post",
                localDate, city.findById(2));
        PostController postController = new PostController(
                postService,
                cityService
        );

        String page = postController.updatePost(input, 1);
        verify(postService).update(input);
        assertThat(page, is("redirect:/posts"));
    }
}
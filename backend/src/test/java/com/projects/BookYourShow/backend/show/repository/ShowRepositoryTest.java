package com.projects.BookYourShow.backend.show.repository;

import com.projects.BookYourShow.backend.support.RepositoryTestSupport;
import com.projects.BookYourShow.backend.show.entity.Show;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ShowRepositoryTest extends RepositoryTestSupport {
    @Autowired ShowRepository repository;
    @Test void detectsPartialContainedAndEnclosingOverlapsButAllowsAdjacentShows() {
        var screen = screen(theater("Pune")); show(movie("Film"), screen,date,10); em.flush(); em.clear();
        assertTrue(repository.existsOverlappingShow(screen.getId(),date,date.atTime(9,0),date.atTime(11,0)));
        assertTrue(repository.existsOverlappingShow(screen.getId(),date,date.atTime(11,0),date.atTime(13,0)));
        assertTrue(repository.existsOverlappingShow(screen.getId(),date,date.atTime(10,30),date.atTime(11,30)));
        assertTrue(repository.existsOverlappingShow(screen.getId(),date,date.atTime(9,0),date.atTime(13,0)));
        assertFalse(repository.existsOverlappingShow(screen.getId(),date,date.atTime(8,0),date.atTime(10,0)));
        assertFalse(repository.existsOverlappingShow(screen.getId(),date,date.atTime(12,0),date.atTime(14,0)));
        assertFalse(repository.existsOverlappingShow(screen.getId(),date.plusDays(1),date.plusDays(1).atTime(10,0),date.plusDays(1).atTime(12,0)));
        assertFalse(repository.existsOverlappingShow(-1L,date,date.atTime(10,0),date.atTime(12,0)));
    }
    @Test void titleAndCitySearchIgnoresCaseAndDeduplicatesMovies() {
        var screen = screen(theater("Pune")); var movie = movie("Interstellar");
        show(movie,screen,date,10); show(movie,screen,date,14); show(movie("Other"),screen(theater("Mumbai")),date,10); em.flush(); em.clear();
        assertEquals(movie.getId(),repository.findMovieByTitleAndCity("INTERSTELLAR","pUnE").orElseThrow().getId());
        assertTrue(repository.findMovieByTitleAndCity("Interstellar","Mumbai").isEmpty());
        assertTrue(repository.findMovieByTitleAndCity("Missing","Pune").isEmpty());
    }
    @Test void citySearchPaginatesDistinctMovies() {
        var screen = screen(theater("Pune")); var alpha = movie("Alpha"); var beta = movie("Beta");
        show(alpha,screen,date,10); show(alpha,screen,date,14); show(beta,screen,date,18);
        show(movie("Outside"),screen(theater("Mumbai")),date,10); em.flush(); em.clear();
        var first = repository.findMoviesByCity("PUNE",PageRequest.of(0,1,Sort.by("movie.title")));
        assertEquals(2,first.getTotalElements()); assertEquals(2,first.getTotalPages()); assertEquals("Alpha",first.getContent().getFirst().getTitle());
        assertEquals("Beta",repository.findMoviesByCity("Pune",PageRequest.of(1,1,Sort.by("movie.title"))).getContent().getFirst().getTitle());
        assertTrue(repository.findMoviesByCity("Unknown",PageRequest.of(0,5)).isEmpty());
    }
    @Test void showSearchFiltersMovieCityAndDateAndOrdersByStartTime() {
        var movie = movie("Film"); var screen = screen(theater("Pune"));
        Show late = show(movie,screen,date,18), early = show(movie,screen,date,10);
        show(movie,screen,date.plusDays(1),10); show(movie,screen(theater("Mumbai")),date,10);
        show(movie("Other"),screen,date,14); em.flush(); em.clear();
        assertEquals(List.of(early.getId(),late.getId()),repository.findShowsByMovieAndCityAndDate(movie.getId(),"PUNE",date).stream().map(Show::getId).toList());
        assertTrue(repository.findShowsByMovieAndCityAndDate(movie.getId(),"Unknown",date).isEmpty());
    }
}

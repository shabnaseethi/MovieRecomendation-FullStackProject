package org.issk.dto;
import java.util.List;

public class MovieResponse {
    private int page;
    private List<Movie> results;
    private int totalPages;
    private int totalResults;

    // Getters and setters

    public int getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", results=" + results +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                '}';
    }
}

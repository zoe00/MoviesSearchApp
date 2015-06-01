package movies.search.app.bo;

import java.io.Serializable;

public class Movie implements Serializable{

    private String title = "";
    private String overview = "";
    private long id = -1;
    private String release_date = "";
    private String poster_path = "";
    private float vote_average = 0.0f;
    private long vote_count = -1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(float vote_average) {
        this.vote_average = vote_average;
    }

    public long getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(long vote_count) {
        this.vote_count = vote_count;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}


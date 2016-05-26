package com.example.user01.planit;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Movie {

    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;


    public Boolean getAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }


    public List<Integer> getGenreIds() {
        return genreIds;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }


    public String getOverview() {
        return overview;
    }


    public String getReleaseDate() {
        return releaseDate;
    }


    public String getPosterPath() {
        return posterPath;
    }



    public Double getPopularity() {
        return popularity;
    }


    public String getTitle() {
        return title;
    }


    public Double getVoteAverage() {
        return voteAverage;
    }


    public Integer getVoteCount() {
        return voteCount;
    }


}
package com.ywk.popmovies.ui.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/1.
 */
public class MoviesEntity implements Parcelable{


    /**
     * poster_path : /5N20rQURev5CNDcMjHVUZhpoCNC.jpg
     * adult : false
     * overview : Following the events of Age of Ultron, the collective governments of the world pass an act designed to regulate all superhuman activity. This polarizes opinion amongst the Avengers, causing two factions to side with Iron Man or Captain America, which causes an epic battle between former allies.
     * release_date : 2016-04-27
     * genre_ids : [28,878,53]
     * id : 271110
     * original_title : Captain America: Civil War
     * original_language : en
     * title : Captain America: Civil War
     * backdrop_path : /m5O3SZvQ6EgD5XXXLPIP1wLppeW.jpg
     * popularity : 84.55468
     * vote_count : 374
     * video : false
     * vote_average : 6.66
     */

    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private int id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean video;
    private double vote_average;
    private List<Integer> genre_ids;

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster_path);
        dest.writeByte(adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeInt(this.id);
        dest.writeString(this.original_title);
        dest.writeString(this.original_language);
        dest.writeString(this.title);
        dest.writeString(this.backdrop_path);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeByte(video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.vote_average);
        dest.writeList(this.genre_ids);
    }

    public MoviesEntity() {
    }

    protected MoviesEntity(Parcel in) {
        this.poster_path = in.readString();
        this.adult = in.readByte() != 0;
        this.overview = in.readString();
        this.release_date = in.readString();
        this.id = in.readInt();
        this.original_title = in.readString();
        this.original_language = in.readString();
        this.title = in.readString();
        this.backdrop_path = in.readString();
        this.popularity = in.readDouble();
        this.vote_count = in.readInt();
        this.video = in.readByte() != 0;
        this.vote_average = in.readDouble();
        this.genre_ids = new ArrayList<Integer>();
        in.readList(this.genre_ids, Integer.class.getClassLoader());
    }

    public static final Creator<MoviesEntity> CREATOR = new Creator<MoviesEntity>() {
        @Override
        public MoviesEntity createFromParcel(Parcel source) {
            return new MoviesEntity(source);
        }

        @Override
        public MoviesEntity[] newArray(int size) {
            return new MoviesEntity[size];
        }
    };
}

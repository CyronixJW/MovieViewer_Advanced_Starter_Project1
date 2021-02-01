package com.nyp.sit.movieviewer_advanced_starter_project.entity

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "MovieItem_table")
class   MovieItem(
        @ColumnInfo(name = "poster_path") var poster_path: String? = null,
        @ColumnInfo(name = "adult") var adult: Boolean? = null,
        @ColumnInfo(name = "overview") var overview: String? = null,
        @ColumnInfo(name = "release_date") var release_date: String? = null,
        @ColumnInfo(name = "genre_ids") var genre_ids: String? = null,
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,
        @ColumnInfo(name = "original_title") var original_title:String? = null,
        @ColumnInfo(name = "original_language") var original_langauge: String? = null,
        @ColumnInfo(name = "title") var title: String? = null,
        @ColumnInfo(name = "backdrop_path") var backdrop_path: String? = null,
        @ColumnInfo(name = "popularity") var popularity: Double = 0.0,
        @ColumnInfo(name = "vote_count") var vote_count: Int = 0,
        @ColumnInfo(name = "video") var video: Boolean? = null,
        @ColumnInfo(name = "vote_average") var vote_average: Double = 0.0
    ): Serializable
{
   init{

       this.poster_path = poster_path
       this.adult = adult
       this.overview= overview
       this.release_date= release_date
       this.genre_ids = genre_ids
       this.id = id
       this.original_title = original_title
       this.original_langauge = original_langauge
       this.title = title
       this.backdrop_path = backdrop_path
       this.popularity = popularity
       this.vote_count = vote_count
       this.video = video
       this.vote_average = vote_average

   }




}
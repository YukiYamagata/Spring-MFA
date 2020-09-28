package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class Goods implements Serializable {

	private static final long serialVersionUID = 1L;

	private String trackId;

	private String artistName;

	private String trackName;

	private String trackCensoredName;

	private String trackViewUrl;

	private String previewUrl;

	private String primaryGenreName;

	private String longDescription;

	private Date releaseDate;

	private int collectionPrice;

	private String trackTimeMillis;

	private String artworkUrl30;

	private String artworkUrl60;

	private String artworkUrl100;

}

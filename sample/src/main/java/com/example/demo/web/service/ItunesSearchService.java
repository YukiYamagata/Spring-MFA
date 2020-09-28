package com.example.demo.web.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.SampleApplication;
import com.example.demo.entity.ItunesSearchResult;
import com.example.demo.entity.Media;
import com.example.demo.log.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ItunesSearchService {

	private static Log logger = new Log(SampleApplication.class);

	@Autowired
	private HttpSession session;

	public ItunesSearchResult search(Media media, String term) {

		session.setAttribute("test", "test");

		String data = getItunesApiData(media, term);
		logger.info(data);
		return parse(data);
	}

	private ItunesSearchResult parse(String data) {

		ObjectMapper mapper = new ObjectMapper();
		ItunesSearchResult itunesSearchResult = null;

		try {
			itunesSearchResult = mapper.readValue(data,
					new TypeReference<ItunesSearchResult>() {
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return itunesSearchResult;
	}

	private String getItunesApiData(Media media, String term) {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(10000).setSocketTimeout(10000).build();

		String url = null;

		try {
		url = "https://itunes.apple.com/search?term="
				+ URLEncoder.encode(term, "UTF-8") + "&media="
				+ media.getValue() + "&country=JP&lang=ja_jp";
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException("illegal encode.");
		}

		logger.info(url);

		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Accept-Charset", "utf-8"));
		headers.add(new BasicHeader("Accept-Language", "ja, en;q=0.8"));
		headers.add(new BasicHeader("User-Agent", "UA"));

		HttpClient httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setDefaultHeaders(headers)
				.useSystemProperties()
				.build();

		HttpGet httpGet = new HttpGet(url);
		HttpResponse response;
		String body = null;
		try {
			response = httpClient.execute(httpGet);
			int responseStatus = response.getStatusLine().getStatusCode();
			if (responseStatus != 200) {
				throw new RuntimeException("status code is not 200.");
			}
			body = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return body;
	}
}

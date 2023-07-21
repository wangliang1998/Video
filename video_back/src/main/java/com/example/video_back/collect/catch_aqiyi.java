package com.example.video_back.collect;

import com.alibaba.druid.util.StringUtils;
import com.example.video_back.pojo.Aqiyi;
import com.example.video_back.pojo.Kinds;
import com.example.video_back.pojo.Movie;
import com.example.video_back.pojo.movieKind;
import com.example.video_back.service.KindService;
import com.example.video_back.service.MovieService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


@Service
public class catch_aqiyi {

    @Autowired
	private MovieService movieService;

    @Autowired
    private KindService kindService;



	/**
	 * 爱奇艺电影数据爬取
	 * @param catch_url  爬取连接
	 * @return aqiyi
	 */
	public List<Aqiyi> getCatch_aqiyis(String catch_url){
		try {
            List<Aqiyi> result = new ArrayList<>();
			/** 初始化操作
			 * 1.先数据库读取读取电影类型(id name)
			 * 2.转换为map类型，方便判断该电影类型是否在类型表中
			 */
			List<Kinds> lists  = kindService.getAllKinds();
			Map<String, Integer> kind_map = new HashMap<String, Integer>();
			for(int z=0;z<lists.size();z++) {
				kind_map.put(lists.get(z).getName(), lists.get(z).getKind());
			}
			/**
			 * 1.获取json文件
			 */
			Connection connection = Jsoup.connect(catch_url);
			connection.ignoreContentType(true);
			connection.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36");
			Document document = connection.get();
			String jsonsString = document.text();
			/**
			 * 2.开始解析json数据
			 */
			JSONObject object = new JSONObject(jsonsString).optJSONObject("data");
			String data = object.optString("list", null);
			JSONArray jsonArray = new JSONArray(data);
			for(int i=0;i<jsonArray.length();i++) {
				try {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String name = jsonObject.getString("title");
					/**
					 * 判断电影是否存在于数据库，若不存在则增加到数据库，存在则跳过
					 */
					boolean isExist = movieService.isNameExist(name);
					//如果电影存在，跳过
					if(isExist) {
						continue;
					}

					String information = jsonObject.getString("description");
					String picture_url = jsonObject.getString("imageUrl");
					String movie_url = jsonObject.getString("playUrl");

					float score;
					if(jsonObject.has("score"))
						score= (float) jsonObject.getDouble("score");
					else {
						score = (float) 0.0;
					}

					Integer popular=(int) (score*100);
					String director = "";

					String time = jsonObject.getString("period");

					//获取演员
					String actorsString = "";
					JSONArray actors = null;
					try {
						actors = jsonObject.getJSONObject("people").getJSONArray("main_charactor");
					} catch (Exception e) {
						continue;
					}
					director = actors.getJSONObject(0).getString("name");
					for(int j=0;j<actors.length();j++) {
						JSONObject jsonactor = actors.getJSONObject(j);
						if("".equals(actorsString)) {
							actorsString = actorsString + jsonactor.getString("name");
						}else {
							actorsString = actorsString + "#"+jsonactor.getString("name");
						}
					}
					//  2.获取电影类型：并对电影类型进行判断
                    JSONArray categories = null;
					try {
                        categories = jsonObject.getJSONArray("categories");
					} catch (Exception e) {
						continue;
					}
					int count = 0;//用于检测99的个数，只能一个，多了重复
                    List<movieKind> movie_kinds = new ArrayList<>();
					for(int j=0;j<categories.length();j++) {
						String kindString= categories.get(j).toString();
						Integer kind_id = kind_map.get(kindString);
						int id;
						if(kind_id==null) {
							count++;
							if(count<=1) {
								id = 99;
								movie_kinds.add(new movieKind(0, id));
							}
						}else {
							id = kind_id;
							movie_kinds.add(new movieKind(0, id));
						}
					}
					Movie movie = new Movie(0, name, actorsString, information, picture_url,
                            movie_url, "", "", score, popular, "", time, director);

                    result.add(new Aqiyi(movie,movie_kinds));
                }catch(Exception e) {
					continue;
				}
			}
			return result;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;

	}
	public String LanguageAndArea_Is(String language) {
		if("普通话".equals(language)||"英语".equals(language)
				||"韩语".equals(language)||"日话".equals(language)||"粤语".equals(language)) {
			return language;
		}else if ("美国".equals(language)||"韩国".equals(language)|"日本".equals(language)||"印度".equals(language)||"欧洲".equals(language)) {
			return language;
		}else if ("华语".equals(language)) {
			return "中国";
		}
		return "其他";
	}

}

package com.sist.manager;
import java.util.*;
import java.text.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sist.dao.MovieDAO;
import com.sist.dao.MovieVO;

public class MovieManager {

	public static void main(String[] args) {
		MovieManager m = new MovieManager();
		m.movieDetailData();
		System.out.println("저장 완료!!!");
	}
	//http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20171205
	//	<td class="title">
	//		<div class="tit5">
	//			<a href="/movie/bi/mi/basic.nhn?code=17421" title="쇼생크 탈출">쇼생크 탈출</a>
	//		</div>
	//	</td>
	public List<String> movieLinkData() {
		List<String> list = new ArrayList<String>();
		
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			int k = 1;
			for(int i=1; i<=40; i++) {
				Document doc = Jsoup.connect(
						"http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=" + sdf.format(date)+"&page="+i).get();
				Elements elem = doc.select("td.title div.tit5 a"); //50개 가져옴 / Elements => 
				for(int j=0; j<elem.size(); j++) {
					Element code = elem.get(j);
					//System.out.println(k+" : " + code.attr("href"));
					list.add("http://movie.naver.com" + code.attr("href"));
					k++;
				}
			}
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return list;
	}
	
	public List<MovieVO> movieDetailData() {
		List<MovieVO> list = new ArrayList<MovieVO>();
		MovieDAO dao = new MovieDAO();
		try {
			List<String> linkList = movieLinkData();
			for(int i=0; i<linkList.size(); i++) {
				try {
					String link = linkList.get(i);
					Document doc = Jsoup.connect(link).get();
					Element title = doc.select("div.mv_info h3.h_movie a").first();
//					System.out.println((i+1) + " : " + title.text()); //.text()태그와 태그 사이의 값
					Element director = doc.select("div.info_spec2 dl.step1 dd a").first();
					Element actor = doc.select("div.info_spec2 dl.step2 dd a").first();
					Elements temp = doc.select("p.info_spec span");
					Element genre = temp.get(0);
					Element time = temp.get(2);
					Element regdate = temp.get(3);
					Element grade = temp.get(4);
					Element poster = doc.select("div.poster a img").first();
					//Element soso = doc.select("").first();;
					Element story = doc.select("div.story_area p.con_tx").first();
					System.out.println((i+1) + " : " + title.text());
					
					MovieVO vo = new MovieVO();
					vo.setMno(i+1);
					vo.setTitle(title.text());
					vo.setDirector(director.text());
					vo.setActor(actor.text());
					vo.setPoster(poster.attr("src"));
					vo.setGenre(genre.text());
					vo.setTime(time.text());
					vo.setRegdate(regdate.text());
					vo.setGrade(grade.text());
					
					//DB에 올리기 위해 ', " 없애기
					String s = story.text();
					s = s.replace("'", "");
					s = s.replace("\"", "");
					vo.setStory(s);
					dao.movieInsert(vo);
					list.add(vo);
				} catch(Exception e) { }
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return list;
	}
}

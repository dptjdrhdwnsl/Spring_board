package min.board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import min.board.model.ReplyDTO;
import min.board.model.BoardDTO;

public class BoardDAO {
	SqlSessionTemplate template;

	public void setTemplate(SqlSessionTemplate template) {
		this.template = template;
	}

	public void makeTemplate() {

		// xml파일 불러오는 구문
		// xml 파일경로
		// 공통적으로 아래의 메서드들에서 xml 불러오는 메서드를 불러서 사용
		ApplicationContext context = new ClassPathXmlApplicationContext("config/root-context.xml");
		template = (SqlSessionTemplate) context.getBean("sqlSessionTemplate");
	}

	public int getListCount() {
		makeTemplate();
		// selectOne() 데이터베이스로부터 1개의 열(row)을 가져올 때 사용한다.
		// 한개 이상의 열(row)이나 null이 리턴된다면 Exception이 발생.
		return template.selectOne("getListCount");
	}

	public int getReplyCount(ReplyDTO replyDTO) {
		makeTemplate();
		return template.selectOne("getReplyCount", replyDTO);
	}
	
	
	
	public List<BoardDTO> getBoardList(int page, int limit) {
		makeTemplate();
		int startrow = (page - 1) * 10 + 1;
		int endrow = startrow + limit - 1;
		// mapper.xml 에서 getBoardList 가 parameterType 을 Map으로 해둠.
		Map map = new HashMap();
		map.put("startrow", startrow);
		map.put("endrow", endrow);
		// selectList() 데이트베이스로부터 1개 또는 그 이상의 열(row)을 가져올 때 사용.
		// mapper.xml 의 id getBoardList
		return template.selectList("getBoardList", map);

	}

	public int boardInsert(BoardDTO boardDTO) {
		makeTemplate();
		// maxCount 변수를 만들어서 글을 쓸 때 최고 번호+1을 시켜줌.
		// 글 자료가 아예 없을 때 에러 발생. 
		int maxCount;
		Object count = template.selectOne("getMaxCount", boardDTO);
		if(count == null) {
			maxCount = 0;
		}
		else {
			maxCount = template.selectOne("getMaxCount", boardDTO);
		}
		System.out.println(maxCount);
		boardDTO.setNum(maxCount+1);
		
		template.insert("boardInsert", boardDTO);
		return template.selectOne("getMaxCount");
	}

	public BoardDTO getDetail(BoardDTO boardDTO) {
		makeTemplate();
		return template.selectOne("getDetail", boardDTO);

	}

	public void setReadCountUpdate(BoardDTO boardDTO) {
		makeTemplate();
		template.update("setReadCountUpdate", boardDTO);
	}

	public int boardModify(BoardDTO boardDTO) {
		makeTemplate();
		return template.update("boardModify", boardDTO);
		// int result = template.update("boardModify",boardDTO);
		// return result;
	}

	// 글쓴이 확인(글번호와 비밀번호 이용)
	public boolean isBoardWriter(int num, String pass) {
		makeTemplate();
		Map<String, Object> map = new HashMap();
		map.put("num", num);
		map.put("pass", pass);
		BoardDTO searchBoardDTO = template.selectOne("isBoardWriter", map);
		if (searchBoardDTO == null) {
			return false;
		}
		return true;

	}

	public int boardReply(BoardDTO boardDTO) {
		makeTemplate();
		int num = template.selectOne("getMaxNum");
		num++;
		template.update("boardReplyUpdate", boardDTO);
		boardDTO.setAnswer_seq(boardDTO.getAnswer_seq() + 1);
		boardDTO.setAnswer_lev(boardDTO.getAnswer_lev() + 1);
		boardDTO.setNum(num);
		template.insert("boardReply", boardDTO);
		return template.selectOne("getMaxCount");
	}

	public List<BoardDTO> getSearchList(String keyword, String keyfield, int page, int limit) {
		makeTemplate();
		String searchCall = "";
		// System.out.println("2keyword : " + keyword);
		// System.out.println("2keyFile : " + keyfield);
		// System.out.println("2page : " + page);
		// System.out.println("2limit : " + limit);
		if (!"".equals(keyword)) {
			if ("all".equals(keyfield)) {
				searchCall = "(subject  like  '%'  ||  '" + keyword + "'  ||  '%'  ) or  ( name  like  '%'  ||  '"
						+ keyword + "'  ||  '%') or  (  content  like  '%'  ||  '" + keyword + "'  ||  '%')";
			} else if ("subject".equals(keyfield)) {
				searchCall = " subject like '%' || '" + keyword + "' || '%'";
			} else if ("name".equals(keyfield)) {
				searchCall = " name like '%' || '" + keyword + "' || '%'";
			} else if ("content".equals(keyfield)) {
				searchCall = " content like '%' || '" + keyword + "' || '%'";
			}
		}
		int startrow = (page - 1) * 10 + 1;
		int endrow = startrow + limit - 1;
		Map map = new HashMap();
		map.put("searchCall", searchCall);
		map.put("startrow", startrow);
		map.put("endrow", endrow);
		return template.selectList("getSearchList", map);
	}

	public int getSearchListCount(String keyword, String keyfield) {
		makeTemplate();
		String searchCall = "";
		if (!"".equals(keyword)) {
			if ("all".equals(keyfield)) {
				searchCall = "(subject like '%' || '" + keyword + "' || '%' ) or ( name like '%' || '" + keyword
						+ "' || '%') or ( content like '%' || '" + keyword + "' || '%')";
			} else if ("subject".equals(keyfield)) {
				searchCall = " subject like '%' || '" + keyword + "' || '%'";
			} else if ("name".equals(keyfield)) {
				searchCall = " name like '%' || '" + keyword + "' || '%'";
			} else if ("content".equals(keyfield)) {
				searchCall = " content like '%' || '" + keyword + "' || '%'";
			}
		}
		Map map = new HashMap();
		map.put("value", searchCall);
		// System.out.println(map.get("value"));
		int count = template.selectOne("getSearchListCount", map);
		return count;
	}

	public void boardDelete(int num) {
		makeTemplate();
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setNum(num);
		template.selectOne("boardDelete", boardDTO);
	}
	
	public List<ReplyDTO> getReplyList(BoardDTO boardDTO) {
		makeTemplate();
		return template.selectList("getReplyList", boardDTO);
	}
	
	public int replyInsert(ReplyDTO replyDTO) {
		makeTemplate();
		int maxCount;
		Object count = template.selectOne("getReplyMaxCount", replyDTO);
		if(count == null) {
			maxCount = 0;
		}
		else {
			maxCount = template.selectOne("getReplyMaxCount", replyDTO);
		}
		System.out.println(maxCount);
		replyDTO.setReply_no(maxCount+1);
		
		template.insert("replyInsert", replyDTO);
		return template.selectOne("getReplyMaxCount");
	}

	public boolean isReplyWriter(int replyNum, String reply_password) {
		makeTemplate();
		Map map = new HashMap();
		map.put("replyNum", replyNum);
		map.put("reply_password", reply_password);
		ReplyDTO searchReplyDTO = template.selectOne("isReplyWriter", map);
		if (searchReplyDTO == null) {
			return false;
		}
		return true;
	}

	public void replyDelete(int replyNum) {
		makeTemplate();
		ReplyDTO replyDTO = new ReplyDTO();
		replyDTO.setReply_no(replyNum);
		template.selectOne("replyDelete", replyDTO);
		
	}

	public void replyChange(Map map) {
		makeTemplate();
		template.selectOne("replyChange", map);
		
	}
}
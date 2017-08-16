package min.board.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



import min.board.service.BoardService;

// 컨트롤러 어노테이션
@Controller
public class BoardController {
	// service들을 Autowired 로 설정 , 의존관계를 자동으로 설정하며 기본적으로 byType 으로 설정된다.
	// Bean 객체들 중에서 특정 Bean 객체인 searchService를 사용하도록 설정
	
	@Autowired
	private BoardService boardService;

	// 각 서비스에 @RequestMapping으로 매핑

	// 메인리스트
	@RequestMapping("/BoardMain")
	public String boardMain(Model model, HttpServletRequest request) {
		if (request.getParameter("page") != null) {
			model.addAttribute("page", Integer.parseInt(request.getParameter("page")));
		}
		model = boardService.listService(model);
		return "/board/board_list"; // jsp경로

	}

	// 글쓰기 폼
	@RequestMapping(value = "/BoardWrite", method = RequestMethod.GET)
	public String boardWriteForm() {
		return "/board/board_write";
	}

	// 글쓰기 처리 -> 상세보기 서비스 반환
	// 값을 넘겨줄 때 POST 방식
	@RequestMapping(value = "/BoardWrite", method = RequestMethod.POST)
	public String boardWrite(Model model, HttpServletRequest request) throws IOException {
		// boardAddService 실행
		boardService.addService(model, request);
		return boardDetail(model, request);
	}

	// 글상세보기
	@RequestMapping(value = "/BoardDetail", method = RequestMethod.GET)
	public String boardDetail(Model model, HttpServletRequest request) {
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
		}
		model = boardService.detailService(model);
		return "/board/board_view";
	}

	// 첨부파일 다운로드
	@RequestMapping(value = "/BoardDownload", method = RequestMethod.GET)
	public void boardDownload(Model model, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException {
		boardService.downloadService(model, request, response);
	}

	// 글수정상세보기
	@RequestMapping(value = "/BoardModify", method = RequestMethod.GET)
	public String boardModifyDetail(Model model, HttpServletRequest request) {
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
		}
		model = boardService.modifyDetailService(model);
		return "/board/board_modify";
	}

	// 글수정처리
	@RequestMapping(value = "/BoardModify", method = RequestMethod.POST)
	public String boardModify(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		boardService.modifyService(model, request, response);
		return boardDetail(model, request);
	}

	// 답글폼
	@RequestMapping(value = "/BoardReply", method = RequestMethod.GET)
	public String boardReplyForm(Model model, HttpServletRequest request) {
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
		}
		model = boardService.replyMoveService(model);
		return "/board/board_reply";
	}

	// 답글처리 -> 상세보기 서비스반환
	@RequestMapping(value = "/BoardReply", method = RequestMethod.POST)
	public String boardReply(Model model, HttpServletRequest request) throws IOException {
		int num = boardService.replyService(model, request);
		model.addAttribute("num", num);
		return boardDetail(model, request);
	}

	// 검색결과리스트
	@RequestMapping(value = "/BoardSearchList", method = RequestMethod.POST)
	public String boardSerchList(Model model, HttpServletRequest request) throws UnsupportedEncodingException {

		// request.setCharacterEncoding("UTF-8");
		Map map = model.asMap();
		model.addAttribute("keyfield", request.getParameter("keyfield"));
		model.addAttribute("keyword", request.getParameter("keyword"));
		if (request.getParameter("page") != null) {
			model.addAttribute("page", Integer.parseInt(request.getParameter("page")));
		}
		model = boardService.searchListService(model);
		return "/board/board_search_list";
	}

	// 삭제폼
	@RequestMapping(value = "/BoardDelete", method = RequestMethod.GET)
	public String boardDeleteForm(Model model, HttpServletRequest request) {
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
		}
		return "/board/board_delete";
	}

	// 삭제처리->메인리스트로 반환
	@RequestMapping(value = "/BoardDelete", method = RequestMethod.POST)
	public String boardDelete(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
		}
		if (request.getParameter("pass") != null) {
			model.addAttribute("pass", request.getParameter("pass"));
		}
		model = boardService.deleteService(model, response);
		return boardMain(model, request);
	}
	
	// 댓글쓰기 처리 -> 글상세보기
	@RequestMapping(value = "/Reply_Write", method = RequestMethod.POST)
	public String replyInsert(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
		int num = Integer.parseInt(request.getParameter("boardNum"));
		String reply_content = request.getParameter("reply_content");
		String reply_writer = request.getParameter("reply_writer");
		String reply_pass = request.getParameter("reply_pass");
		model.addAttribute("num", num);
		model.addAttribute("reply_content", reply_content);
		model.addAttribute("reply_writer", reply_writer);
		model.addAttribute("reply_pass", reply_pass);
		boardService.replyAddService(model);
		return boardDetail(model, request);
	}
	// 댓글삭제폼
	@RequestMapping(value = "/ReplyDelete", method = RequestMethod.GET)
	public String replyDeleteForm(Model model, HttpServletRequest request) {
		int replyNum=-1;
		int num=-1;
		
		if (request.getParameter("reply_num") != null) {
			replyNum = Integer.parseInt(request.getParameter("reply_num"));
			model.addAttribute("reply_num", Integer.parseInt(request.getParameter("reply_num")));
		}
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
			num = Integer.parseInt(request.getParameter("num"));
		}
		request.setAttribute("num", num);
		request.setAttribute("replyNum", replyNum);
		return "/board/reply_delete";
	}

	// 댓글삭제처리
	@RequestMapping(value = "/ReplyDelete", method = RequestMethod.POST)
	public String replyDelete(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (request.getParameter("num") != null) {
			model.addAttribute("num", Integer.parseInt(request.getParameter("num")));
		}
		if (request.getParameter("reply_password") != null) {
			model.addAttribute("reply_password", Integer.parseInt(request.getParameter("reply_password")));
		}
		if (request.getParameter("replyNum") != null) {
			model.addAttribute("replyNum", Integer.parseInt(request.getParameter("replyNum")));
		}
		Map map = model.asMap();
		model = boardService.replyDeleteService(model, response);
		return boardDetail(model, request);
	}
	
	// 댓글 수정
	@RequestMapping(value = "/ReplyChange", method = RequestMethod.POST)
	public String replyChange(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		String pass = request.getParameter("pass");
		String board_no = request.getParameter("board_no");
		int no = Integer.parseInt(request.getParameter("no"));
		if(!boardService.isReplyWriter(no, pass)) {
			return "fail";
		}
		boardService.replyChangeService(writer, content, pass, no);
		return "redirect:/BoardDetail?num="+board_no;
	}
}

// 17-07-22 PDF, 한결코드 참고 데이터베이스 테이블 구성,BoardDTO,root-context.xml,mapper.xml
// 17-07-23 BoardDAO 완성
// 17-07-24 서비스,jsp부분 css 적용 상태로 리스트 웹 호출 구현
// resources 아래로 css/js/img 를 두면 접근이 안 된다. mapping 을 resources 위쪽으로 webapp 아래로
// 내리는 것으로 해결
// =>17-07-25 다시 resources 폴더 아래로 내리고 servelet-context.xml 에 resources 에 css 부분도
// 넣어줌.
// 17-07-25
// C:\spring_work\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\board_spring
// 경로에
// =>boardUpload 폴더가 없어서 에러나서 만들어줌.
// There is no getter for property named 'context' in 'class
// min.board.model.BoardDTO'
// mapper.xml에 SQL 문에 content 를 context 로 오타침. DTO 에 getter&setter 는 설정되어있었음.
// 글 올리고 사진첨부까지 성공. 글번호 첫번호가 1번이어야 하나 0번이 뜸.
// =>BoardDAO에 boardInsert() 에 아래 코드 입력해서 해결.
// int maxCount = template.selectOne("getMaxCount", boardDTO);
// boardDTO.setNum(maxCount+1);
// 글 자료가 없을 때 글 번호쪽에 NULLPOINT 발생 sql 문 찍어보면 max(num) 이 null 로 나옴.
// =>int maxCount;
/*
 * Object count = template.selectOne("getMaxCount", boardDTO); if(count == null)
 * { maxCount = 0; } else { maxCount = template.selectOne("getMaxCount",
 * boardDTO); }
 */
// 한글파일 업로드는 에러없이 되는데 다운로드할 때 에러
// =>다운로드 서비스에 익스플로러 관련 코드와 view.jsp에 코드 추가. 익스플로러 엣지는 아직 못찾음.
// 답변글이 원래글의 아래로 안들어가고 위로 들어감.
// =>17-07-26 리스트 쿼리문에서 맨뒤에 order by 뺌
// 17-07-26 글삭제에서 405 오류
// WARN : org.springframework.web.servlet.PageNotFound - Request method 'POST'
// not supported
// =>글삭제처리 부분 RequestMethod 방식을 GET 으로 바꿔주면 지워짐.
// 17-07-27 글자비밀번호 500에러 java.lang.NumberFormatException: For input string:
// "qqqq"
// =>Controller Delete 부분에 pass 값을 Integer.parseInt로 넘겨줘서 숫자만 되었던 것을 없애줌.
// 수정할 때 기존파일 유지건 문제(수정안하면 null로 바뀌는 현상) =>else 부분에 null 에 old_file 을 넣어주면 된다고함.(이미 코드를 바꿔서 그대로 둠)
//=>Modifyservice 랑 modify.jsp에서 기존 attached_file을 old_file로 수정
// 
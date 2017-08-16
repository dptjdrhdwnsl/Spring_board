package min.board.model;

import java.sql.Date;

public class ReplyDTO {
	private int reply_no;
	private int board_no;
	private String reply_writer;
	private String reply_content;
	private String reply_pass;
	private Date reply_date;
	public int getReply_no() {
		return reply_no;
	}
	public void setReply_no(int reply_no) {
		this.reply_no = reply_no;
	}
	public int getBoard_no() {
		return board_no;
	}
	public void setBoard_no(int board_no) {
		this.board_no = board_no;
	}
	public String getReply_writer() {
		return reply_writer;
	}
	public void setReply_writer(String reply_writer) {
		this.reply_writer = reply_writer;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public String getReply_pass() {
		return reply_pass;
	}
	public void setReply_pass(String reply_pass) {
		this.reply_pass = reply_pass;
	}
	public Date getReply_date() {
		return reply_date;
	}
	public void setReply_date(Date reply_date) {
		this.reply_date = reply_date;
	}
	@Override
	public String toString() {
		return "ReplyDTO [reply_no=" + reply_no + ", board_no=" + board_no + ", reply_writer=" + reply_writer
				+ ", reply_content=" + reply_content + ", reply_pass=" + reply_pass + "]";
	}
	
}

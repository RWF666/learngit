package com.weige.user.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.github.rjeschke.txtmark.Processor;

/**
 * Blog 实体
 * @author RWF
 *
 */
@Entity // 实体
public class Blog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
	private Long id; // 用户的唯一标识
	
	@NotEmpty(message = "标题不能为空")
	@Size(min=2, max=50)
	@Column(nullable = false, length = 50) // 映射为字段，值不能为空
	private String title;
	
	@NotEmpty(message = "摘要不能为空")
	@Size(min=2, max=300)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String summary;

	@Lob  // 大对象，映射 MySQL 的 Long Text 类型
	@Basic(fetch=FetchType.LAZY) // 懒加载
	@NotEmpty(message = "内容不能为空")
	@Size(min=2)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String content;
	
	@Lob  // 大对象，映射 MySQL 的 Long Text 类型
	@Basic(fetch=FetchType.LAZY) // 懒加载
	@NotEmpty(message = "内容不能为空")
	@Size(min=2)
	@Column(nullable = false) // 映射为字段，值不能为空
	private String htmlContent; // 将 md 转为 html


	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable = false) // 映射为字段，值不能为空
	@org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
	private Timestamp createTime;

	@Column(name="reading")
	private Long reading = 0L; // 访问量、阅读量
	 
	@Column(name="comments")
	private Long comments = 0L;  // 评论量

	@Column(name="likes")
	private Long likes = 0L;  // 点赞量
	
	@Column(name="tags", length = 100) 
	private String tags;  // 标签
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "blog_comment", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"))
	private List<Comment> commentContents;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "blog_vote", joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "vote_id", referencedColumnName = "id"))
	private List<Vote> votes;
	
	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name="catalog_id")
	private Catalog catalog;
	
	protected Blog() {
		// TODO Auto-generated constructor stub
	}
	public Blog(String title, String summary,String content) {
		this.title = title;
		this.summary = summary;
		this.content = content;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.htmlContent = Processor.process(content);
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	
	public String getHtmlContent() {
		return htmlContent;
	}
 
	public Long getComments() {
		return comments;
	}
	public void setComments(Long comments) {
		this.comments = comments;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getReading() {
		return reading;
	}
	public void setReading(Long reading) {
		this.reading = reading;
	}
	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", reading=" + reading+", summary=" + summary + ", content=" + content + "]";
	}
	
	public List<Comment> getCommentContents() {
		return commentContents;
	}
	public void setCommentContents(List<Comment> commentContents) {
		this.commentContents = commentContents;
		this.comments = new Long(this.commentContents.size());
	}
 
	/**
	 * 添加评论
	 * @param comment
	 */
	public void addComment(Comment comment) {
		this.commentContents.add(comment);
		this.comments = new Long(this.commentContents.size());
	}
	/**
	 * 删除评论
	 * @param comment
	 */
	public void removeComment(Long commentId) {
		for (int index=0; index < this.commentContents.size(); index ++ ) {
			if (commentContents.get(index).getId() == commentId) {
				this.commentContents.remove(index);
				break;
			}
		}
		this.comments = new Long(this.commentContents.size());
	}
	
	/**
	 * 点赞
	 * @param vote
	 * @return
	 */
	public boolean addVote(Vote vote) {
		boolean isExist = false;
		// 判断重复
		for (int index=0; index < this.votes.size(); index ++ ) {
			if (this.votes.get(index).getUser().getId() == vote.getUser().getId()) {
				isExist = true;
				break;
			}
		}
		
		if (!isExist) {
			this.votes.add(vote);
			this.likes = new Long(this.votes.size());
		}

		return isExist;
	}
	/**
	 * 取消点赞
	 * @param voteId
	 */
	public void removeVote(Long voteId) {
		for (int index=0; index < this.votes.size(); index ++ ) {
			if (this.votes.get(index).getId() == voteId) {
				this.votes.remove(index);
				break;
			}
		}
		
		this.likes = new Long(this.votes.size());
	}
	public List<Vote> getVotes() {
		return votes;
	}
	public void setVotes(List<Vote> votes) {
		this.votes = votes;
		this.likes = new Long(this.votes.size());
	}
	
	public Catalog getCatalog() {
		return catalog;
	}
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
}

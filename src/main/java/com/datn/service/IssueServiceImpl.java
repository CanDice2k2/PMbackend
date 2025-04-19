package com.datn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.exception.IssueException;
import com.datn.exception.ProjectException;
import com.datn.exception.UserException;
import com.datn.model.Issue;
import com.datn.model.Project;
import com.datn.model.User;
import com.datn.repository.IssueRepository;
import com.datn.request.IssueRequest;

@Service
public class IssueServiceImpl implements IssueService {

	@Autowired
	private IssueRepository issueRepository;
//
	@Autowired
	private UserService userService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private NotificationServiceImpl notificationServiceImpl;

//    @Override
//    public List<Issue> getAllIssues() throws IssueException {
//        List<Issue> issues = issueRepository.findAll();
//        if(issues!=null) {
//        	return issues;
//        }
//        throw new IssueException("No issues found");
//    }

	@Override
	public Optional<Issue> getIssueById(Long issueId) throws IssueException {
		Optional<Issue> issue = issueRepository.findById(issueId);
		if (issue.isPresent()) {
			return issue;
		}
		throw new IssueException("No issues found with issueid" + issueId);
	}

	@Override
	public List<Issue> getIssueByProjectId(Long projectId) throws ProjectException {
		projectService.getProjectById(projectId);
		return issueRepository.findByProjectId(projectId);
	}

	@Override
	public Issue createIssue(IssueRequest issueRequest, Long userId)
			throws UserException, IssueException, ProjectException {
		User user = getUserOrThrow(userId);

		// Check if the project exists
		Project project = projectService.getProjectById(issueRequest.getProjectId());
		System.out.println("projid---------->"+issueRequest.getProjectId());
		if (project == null) {
			throw new IssueException("Project not found with ID: " + issueRequest.getProjectId());
		}

		// Create a new issue
		Issue issue = new Issue();
		issue.setTitle(issueRequest.getTitle());
		issue.setDescription(issueRequest.getDescription());
		issue.setStatus(issueRequest.getStatus());
		issue.setProjectID(issueRequest.getProjectId());
		issue.setPriority(issueRequest.getPriority());
		issue.setDueDate(issueRequest.getDueDate());
		issue.setTags(issueRequest.getTags());

         
		// Set the project for the issue
		issue.setProject(project);
		issueRepository.save(issue);
		projectService.updateProjectStatus(issue.getProjectID());
		// Save the issue
		return issue;
	}

	@Override
	public Optional<Issue> updateIssue(Long issueId, IssueRequest updatedIssue, Long userId)
			throws IssueException, UserException, ProjectException {
		User user = getUserOrThrow(userId);
		Optional<Issue> existingIssue = issueRepository.findById(issueId);
                           
		if (existingIssue.isPresent()) {
			// Check if the project exists
			Project project = projectService.getProjectById(updatedIssue.getProjectId());
			if (project == null) {
				throw new IssueException("Project not found with ID: " + updatedIssue.getProjectId());
			}

			User assignee = userService.findUserById(updatedIssue.getUserId());
			if (assignee == null) {
				throw new UserException("Assignee not found with ID: " + updatedIssue.getUserId());
			}

			Issue issueToUpdate = existingIssue.get();


			if (updatedIssue.getDescription() != null) {
				issueToUpdate.setDescription(updatedIssue.getDescription());
			}

			if (updatedIssue.getDueDate() != null) {
				issueToUpdate.setDueDate(updatedIssue.getDueDate());
			}

			if (updatedIssue.getPriority() != null) {
				issueToUpdate.setPriority(updatedIssue.getPriority());
			}

			if (updatedIssue.getStatus() != null) {
				issueToUpdate.setStatus(updatedIssue.getStatus());
			}

			if (updatedIssue.getTitle() != null) {
				issueToUpdate.setTitle(updatedIssue.getTitle());
			}

			// Save the updated issue
			return Optional.of(issueRepository.save(issueToUpdate));
		}

		throw new IssueException("Issue not found with issueid" + issueId);
	}

	@Override
	public String deleteIssue(Long issueId, Long userId) throws UserException, IssueException {
		getUserOrThrow(userId);
		Optional<Issue> issueById = getIssueById(issueId);
		if (issueById.isPresent()) {
			issueRepository.deleteById(issueId);
			return "issue with the id" + issueId + "deleted";
		}
		throw new IssueException("Issue not found with issueid" + issueId);
	}

	@Override
	public List<Issue> getIssuesByAssigneeId(Long assigneeId) throws IssueException {
		List<Issue> issues = issueRepository.findByAssigneeId(assigneeId);
		if (issues != null) {
			return issues;
		}
		throw new IssueException("Issues not found");
	}

	private User getUserOrThrow(Long userId) throws UserException {
		User user = userService.findUserById(userId);

		if (user != null) {
			return user;
		} else {
			throw new UserException("User not found with id: " + userId);
		}
	}

	@Override
	public List<Issue> searchIssues(String title, String status, String priority, Long assigneeId)
			throws IssueException {
		List<Issue> searchIssues = issueRepository.searchIssues(title, status, priority, assigneeId);
		if (searchIssues != null) {
			return searchIssues;
		}
		throw new IssueException("No Issues found");
	}

	@Override
	public List<User> getAssigneeForIssue(Long issueId) throws IssueException {
	return null;
	}

	@Override
	public Issue addUserToIssue(Long issueId, Long userId) throws UserException, IssueException {
		User user = userService.findUserById(userId);
		Optional<Issue> issue=getIssueById(issueId);

		if(issue.isEmpty())throw new IssueException("issue not exist");

		issue.get().setAssignee(user);
		String emailBody = "Kính gửi " + (user != null ? user.getFullName() : "Người dùng") + ",\n\n" +
				"Bạn đã được giao một công việc mới trong dự án " + issue.get().getProject().getName() + ".\n\n" +
				"CHI TIẾT CÔNG VIỆC:\n" +
				"- Tiêu đề: " + issue.get().getTitle() + "\n" +
				"- Mô tả: " + issue.get().getDescription() + "\n" +
				"- Mức độ ưu tiên: " + issue.get().getPriority() + "\n" +
				"- Trạng thái: " + issue.get().getStatus() + "\n" +
				"- Hạn chót: " + issue.get().getDueDate() + "\n\n" +
				"Vui lòng đăng nhập vào hệ thống để xem chi tiết và cập nhật tiến độ công việc.\n\n" +
				"Link truy cập: http://http://localhost:5173/project/" + issue.get().getProject().getId() + "/issue/" + issue.get().getId() + "\n\n" +
				"Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ với quản lý dự án.\n\n" +
				"Trân trọng,\n" +
				"Hệ thống quản lý dự án";
		notifyAssignee("nguyenvanduc2.384@gmail.com","Công việc mới được giao cho bạn",emailBody);
		return issueRepository.save(issue.get());


	}

	@Override
	public Issue removeUserFromIssue(Long issueId, Long userId) throws IssueException {
		Optional<Issue> optionalIssue = issueRepository.findById(issueId);
//		User user = userService.findUserById(userId);
		if(optionalIssue.isEmpty()) {
			throw new IssueException("Issue not found with id: " + issueId);
		}

		Issue issue = optionalIssue.get();

		// Simply set the assignee to null, preserving any comments
		issue.setAssignee(null);

		// Save and return the updated issue
		return issueRepository.save(issue);
	}

	@Override
	public Issue updateStatus(Long issueId, String status) throws IssueException, ProjectException {
		Optional<Issue> optionalIssue=issueRepository.findById(issueId);
		if(optionalIssue.isEmpty()){
			throw new IssueException("issue not found");
		}
		Issue issue=optionalIssue.get();
		issue.setStatus(status);
		projectService.updateProjectStatus(issue.getProject().getId());

		return issueRepository.save(issue);
	}

	private void notifyAssignee(String email, String subject, String body) {
		 System.out.println("IssueServiceImpl.notifyAssignee()");
	        notificationServiceImpl.sendNotification(email, subject, body);
	    }

}

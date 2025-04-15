package com.datn.service;

import java.util.List;
import java.util.Optional;

import com.datn.exception.IssueException;
import com.datn.exception.ProjectException;
import com.datn.exception.UserException;
import com.datn.model.Issue;
import com.datn.model.User;
import com.datn.request.IssueRequest;

public interface IssueService {
//	 List<Issue> getAllIssues() throws IssueException;

	    Optional<Issue> getIssueById(Long issueId) throws IssueException;

	List<Issue> getIssueByProjectId(Long projectId) throws ProjectException;

	    Issue createIssue(IssueRequest issue,Long userid) throws UserException, IssueException, ProjectException;

	    Optional<Issue> updateIssue(Long issueid,IssueRequest updatedIssue,Long userid ) throws IssueException, UserException, ProjectException;

	    String deleteIssue(Long issueId,Long userid) throws UserException, IssueException;

	    List<Issue> getIssuesByAssigneeId(Long assigneeId) throws IssueException;
	    
	    List<Issue> searchIssues(String title, String status, String priority, Long assigneeId) throws IssueException;
	    
	    List<User> getAssigneeForIssue(Long issueId) throws IssueException;

	    Issue addUserToIssue(Long issueId, Long userId) throws UserException, IssueException;

		Issue removeUserFromIssue(Long issueId, Long userId) throws IssueException;

		Issue updateStatus(Long issueId, String status) throws IssueException;

}

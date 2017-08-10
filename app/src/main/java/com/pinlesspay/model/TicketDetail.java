package com.pinlesspay.model;

/*
 * Created by arun.sharma on 8/10/2017.
 */

public class TicketDetail {

    private String TicketDesc, DateCreated, IsUserComment, CommentBy;

    public String getTicketDesc() {
        return TicketDesc;
    }

    public void setTicketDesc(String ticketDesc) {
        TicketDesc = ticketDesc;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getIsUserComment() {
        return IsUserComment;
    }

    public void setIsUserComment(String isUserComment) {
        IsUserComment = isUserComment;
    }

    public String getCommentBy() {
        return CommentBy;
    }

    public void setCommentBy(String commentBy) {
        CommentBy = commentBy;
    }
}

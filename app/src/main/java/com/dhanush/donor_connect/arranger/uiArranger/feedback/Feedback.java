package com.dhanush.donor_connect.arranger.uiArranger.feedback;

public class Feedback {
    public String review_r,email_r;

    public Feedback() {
    }

    public Feedback(String review_r, String email_r) {
        this.review_r = review_r;
        this.email_r=email_r;
    }

    public String getReview_r() {
        return review_r;
    }

    public void setReview_r(String review_r) {
        this.review_r = review_r;
    }

    public String getEmail_r() {
        return email_r;
    }

    public void setEmail_r(String email_r) {
        this.email_r = email_r;
    }
}

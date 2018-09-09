package controllers;

import models.Assessment;
import models.Member;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

import java.util.List;

public class ViewAssessments extends Controller {

    public static void index(Long memberid) {
        Logger.info("Rendering Assessments");

        Trainer trainer = Accounts.getLoggedInTrainer();
        Member member = Member.findById(memberid);
        render("viewassessments.html", trainer, member);
    }

    public static void updateComment(Long memberid, Long assessmentid, String comment)
    {
        Logger.info("Rendering Assessments");

        Member member = Member.findById(memberid);
        Assessment assessment = Assessment.findById(assessmentid);
        assessment.setComment(comment);
        assessment.save();

        redirect("viewassessments.index", memberid);
    }
}

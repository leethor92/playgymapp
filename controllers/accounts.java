ackage controllers;

import models.Member;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

public class Accounts extends Controller {

    //render signup page
    public static void signup() {
        render("signup.html");
    }

    //render login page
    public static void login() {
        render("login.html");
    }

    //deletes the session
    public static void logout() {
        session.clear();
        redirect("/");
    }

    //registers a member and saves there data
    public static void register(String name,
                                String email,
                                String password,
                                String address,
                                String gender,
                                double height,
                                double startingWeight){
        Logger.info("Registering a new user " + email);
        Member member = new Member(name, email, password, address, gender, height, startingWeight);
        member.save();
        redirect("/");
    }

    //authenticates login for either a member or trainer, if matches password & email,
    // the person is directed to the correct dashboard
    public static void authenticate(String email, String password) {
        Logger.info("Attempting to authenticate with " + email + ":" + password);

        Member member = Member.findByEmail(email);
        Trainer trainer = Trainer.findByEmail(email);
        if ((member != null) && (member.checkPassword(password) == true)) {
            Logger.info("Authentication successful");
            session.put("logged_in_Memberid", member.id);
            redirect("/dashboard");
        } else if((trainer != null) && (trainer.checkPassword(password) == true)) {
            Logger.info("Authentication successful");
            session.put("logged_in_Trainerid", trainer.id);
            redirect("/trainerdashboard");
        } else
            {
                Logger.info("Authentication failed");
                redirect("/login");
            }
    }

    //checks for a session containing the members id, returns member if it exists & returns null if not
    public static Member getLoggedInMember(){
        Member member = null;
        if (session.contains("logged_in_Memberid")) {
            String memberId = session.get("logged_in_Memberid");
            member = Member.findById(Long.parseLong(memberId));
        } else {
            login();
        }
        return member;
    }

    //checks for a session containing the trainers id, returns member if it exists & returns null if not
    public static Trainer getLoggedInTrainer(){
        Trainer trainer = null;
        if (session.contains("logged_in_Trainerid")){
            String trainerId = session.get("logged_in_Trainerid");
            trainer = Trainer.findById(Long.parseLong(trainerId));
        } else {
            login();
        }
        return trainer;
    }

    //finds the currently logged in members profile & updates the profile view
    public static void update(String name,
                              String email,
                              String password,
                              String address,
                              String gender,
                              double height,
                              double startingWeight) {

        Member member = Accounts.getLoggedInMember();
        member.setName(name);
        member.setEmail(email);
        member.setPassword(password);
        member.setAddress(address);
        member.setGender(gender);
        member.setHeight(height);
        member.setStartingWeight(startingWeight);
        member.save();

        Logger.info("Updating: " + member.name);
        redirect("/profile");
    }
package models;

import play.db.jpa.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.*;

@Entity
public class Member extends Model {
    public String name;
    public String email;
    public String password;
    public String address;
    public String gender;
    public double height;
    public double startingWeight;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Assessment> assessments = new ArrayList<>();

    public Member(
            String name,
            String email,
            String password,
            String address,
            String gender,
            double height,
            double startingWeight) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.height = height;
        this.startingWeight = startingWeight;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setStartingWeight(double startingWeight) {
        this.startingWeight = startingWeight;
    }

    //allows a search by email function
    public static Member findByEmail(String email) {
        return find("email", email).first();
    }

    //check if password entered is correct
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public double idealBodyWeight() {
        double genderWeight = 0;
        double fiveFeet = 60;

        double inches = convertHeightMetresToInches();
        if (inches < fiveFeet) {
            inches = fiveFeet;
        }

        if (gender.equals("MALE")) {
            genderWeight = 50.00;
        } else {
            genderWeight = 45.50;
        }

        double idealBodyWeight = genderWeight + ((inches - fiveFeet) * 2.3);
        return idealBodyWeight;
    }


        //method returns a String of classes if member has ideal weight or not
        public String isIdealBodyWeight () {
            double weight = 0;
            if (getLatestAssessment() != null) {
                weight = getLatestAssessment().weight;
            } else {
                weight = startingWeight;
            }

            double idealBodyWeight = idealBodyWeight();

            if (weight >= (idealBodyWeight - 0.2) && weight <= (idealBodyWeight + 0.2)) {
                return "green";
            } else {
                return "red";
            }
        }

        //calculates a members BMI
        public double calculateBMI() {
            return toTwoDecimalPlaces(getLatestAssessment().weight /(height * height));
        }

        //round calculations to two decimal places
        private double toTwoDecimalPlaces ( double num){
            return (int) (num * 100.0) / 100.0;
        }

        public String getBMICategory (){
            double bmiValue = calculateBMI();
            if (bmiValue < 16) return "SEVERELY UNDERWEIGHT";
            else if (bmiValue >= 16 && bmiValue < 18.5) return "UNDERWEIGHT";
            else if (bmiValue >= 18.5 && bmiValue < 25) return "NORMAL";
            else if (bmiValue >= 25 && bmiValue < 30) return "OVERWEIGHT";
            else if (bmiValue >= 30 && bmiValue < 35) return "MODERATELY OBESE";
            else if (bmiValue >= 35) return "SEVERELY OBESE";
            return "CANNOT DETERMINE BMI";
        }

        //finds the most recent assessment, returns null if not
        public Assessment getLatestAssessment () {
            if (assessments.size() != 0) {
                return sortedAssessments().get(0);
            }
            return null;
        }

        //converts Height from metres to inches
        public double convertHeightMetresToInches () {
            return toTwoDecimalPlaces(height * 39.37);
        }

        //sort's assessments by date,
        public List<Assessment> sortedAssessments () {
            List<Assessment> sortedList = new ArrayList<Assessment>(assessments);
            Collections.sort(sortedList, new Comparator<Assessment>() {
                @Override
                public int compare(Assessment assessment1, Assessment assessment2) {
                    return assessment2.epoch.compareTo(assessment1.epoch);
                }
            });
            return sortedList;
        }

    }

package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class Assessment extends Model {
    public Long epoch;
    public double weight;
    public double chest;
    public double thigh;
    public double upperArm;
    public double waist;
    public double hips;
    public String comment;

    public Assessment(
            double weight,
            double chest,
            double thigh,
            double upperArm,
            double waist,
            double hips,
            String comment) {

            this.epoch = System.currentTimeMillis();

            this.weight = weight;
            this.chest = chest;
            this.thigh = thigh;
            this.upperArm = upperArm;
            this.waist = waist;
            this.hips = hips;
            this.comment = comment;

    }

    //return the date of assessment in readable format
    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("d-MMM-yyyy HH:mm:ss");
        return dateFormat.format(epoch);
    }

    //Setter for comment
    public void setComment(String comment) {
        this.comment = comment;
    }

}

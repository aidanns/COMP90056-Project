package controllers;

import com.aidanns.streams.project.models.CallDataRecord.CDRType;
import com.aidanns.streams.project.models.Constraint;
import com.aidanns.streams.project.models.ConstraintFactory;
import com.aidanns.streams.project.models.Rule;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controller to encapsulate debugging tasks.
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
public class DebuggingController extends Controller {

	 /**
	  * Add a rule to the database.
	  * @return All the rules and constraints currently in the database.
	  */
     @Transactional
     public static Result addRuleOne() {
     	Rule r = new Rule();
     	r.name = "Rule One";
     	r.active = true;
     	r.windowSize = 10;
     	r.numberOfConstraintMatches = 5;
     	r.constraint = ConstraintFactory.makeCDRTypeConstraint(CDRType.MobileOriginatedCall).and(ConstraintFactory.makeIMEIConstraint("A3472BF79")).get();
     	// Save the new rule to the database using JPA.
     	JPA.em().persist(r);
     	// Select all the objects of a certain type from the database.
     	return ok("Rules: " + JPA.em().createQuery("SELECT r from Rule r", Rule.class).getResultList().toString() + "\n\n" 
     			+ "Constraints: " + JPA.em().createQuery("SELECT c from Constraint c", Constraint.class).getResultList().toString());
     }
   
     /**
      * Add a rule to the database.
      * @return All the rules and constraints currently in the database.
      */
     @Transactional
     public static Result addRuleTwo() {
     	Rule r = new Rule();
     	r.name = "Rule Two";
     	r.active = false;
     	r.windowSize = 15;
     	r.numberOfConstraintMatches = 2;
     	r.constraint = ConstraintFactory.makeCDRTypeConstraint(CDRType.MobileTerminatedCall).and(ConstraintFactory.makeIMSIConstraint("03447BFAB")).get();
     	JPA.em().persist(r);
     	return ok("Rules: " + JPA.em().createQuery("SELECT r from Rule r", Rule.class).getResultList().toString() + "\n\n" 
     			+ "Constraints: " + JPA.em().createQuery("SELECT c from Constraint c", Constraint.class).getResultList().toString());
     }
   
     /**
      * Show the rules that are currently in the database.
      * @return All the rules that are currently in the database.
      */
     @Transactional
     public static Result showAllRules() {
     	return ok("Rules: " + JPA.em().createQuery("SELECT r from Rule r", Rule.class).getResultList().toString());
     }
    
     /**
      * Show the constraints that are currently in the database.
      * @return All the constraints that are currently in the database.
      */
     @Transactional
     public static Result showAllConstraints() {
    	 return ok("Constraints: " + JPA.em().createQuery("SELECT c from Constraint c", Constraint.class).getResultList().toString());
     }
	
}
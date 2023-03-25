package com.github.sebastiankg02.csy2061.as1.quiz;

import android.app.Activity;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

//Simple class used to store user scores
public class Certificate {
    //Quiz Identifier
    public String quizName;
    //Data set, UUID gained from User.ID, Integer gained from scores on QuizPlayFragment
    public HashMap<UUID, Integer> scores;

    //Default constructor, initialise variables
    public Certificate(String quizName) {
        this.quizName = quizName;
        scores = new HashMap<UUID, Integer>();
    }

    /**
     * Gets a certificate from file (loads file, loads JSON, constructs certificate)
     *
     * @param quiz   The name of the Quiz this certificate corresponds to
     * @param caller The activity in charge of the method call (getFilesDir()
     * @return the loaded Certificate object
     * @throws JSONException any JSON error
     */
    public static Certificate getCertificate(String quiz, Activity caller) throws JSONException {
        //Initialise new certificate
        Certificate output = new Certificate(quiz);
        //Load file from internal storage
        AppHelper.FileSystemReturn loadedCert = AppHelper.loadFile(caller.getFilesDir() + "/qscore/" + quiz + ".quiz.json", caller, false, false, false, null);
        //If loaded file successfully loaded, generate certificate from JSON from payload, otherwise return blank certificate
        if (loadedCert.equals(AppHelper.FileSystemReturn.FILE_PRESENT)) {
            return (fromJSON(new JSONObject(loadedCert.payload)));
        } else {
            return output;
        }
    }

    /**
     * Gets a certificate from raw JSON data
     *
     * @param j the JSON Data to use
     * @return the loaded Certificate object
     * @throws JSONException any JSON error
     */
    public static Certificate fromJSON(JSONObject j) throws JSONException {
        //Initialise new certificate
        Certificate output = new Certificate(j.getString("quiz"));
        //Create sub-JSONObject from passed JSON
        JSONObject userContent = j.getJSONObject("scores");
        //Get array of keys from new JSONObject
        Iterator<String> keys = userContent.keys();
        //Loop through keys and add into output HashMap
        while (keys.hasNext()) {
            String key = keys.next();
            output.scores.put(UUID.fromString(key), userContent.getInt(key));
        }
        return output;
    }

    //Saves current file, returns FSR status
    public AppHelper.FileSystemReturn save(Activity a) throws JSONException {
        return AppHelper.saveFile("/qscore/", this.quizName + ".quiz.json", toJSON().toString(), a, null);
    }

    //Converts current Certificate to raw JSON
    public JSONObject toJSON() throws JSONException {
        //Initialise output JSONObject
        JSONObject output = new JSONObject();
        //Add quiz name from object
        output.put("quiz", quizName);

        //Create new JSONObject
        JSONObject scoresOutput = new JSONObject();

        //Loop through all keys in data set, add entry (UUID String) to new JSONObject, register score against entry
        for (UUID i : scores.keySet()) {
            scoresOutput.put(i.toString(), scores.get(i));
        }

        //Add new JSONObject with complete scores to output
        output.put("scores", scoresOutput);
        return output;
    }

    //Get score of a specific user (U)
    public Integer getUserScore(User u) {
        //Check if data set contains entry for this user
        if (scores.containsKey(u.getId())) {
            //Return loaded score
            return scores.get(u.getId());
        } else {
            return -1;
        }
    }

    //Set the score of a specific user U, returns Certificate for immediate saving
    public Certificate setUserScore(User u, int score) {
        //Check if data set contains user
        if (scores.containsKey(u.getId())) {
            //If it does, replace score
            scores.replace(u.getId(), score);
        } else {
            //If not, add to data set
            scores.put(u.getId(), score);
        }
        return this;
    }
}

package com.github.sebastiankg02.csy2061.as1.quiz;

import android.app.Activity;
import android.util.Log;

import com.github.sebastiankg02.csy2061.as1.fragments.apps.AppHelper;
import com.github.sebastiankg02.csy2061.as1.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Certificate {
    public String quizName;
    public HashMap<UUID, Integer> scores;

    public Certificate(String quizName) {
        this.quizName = quizName;
        scores = new HashMap<UUID, Integer>();
    }

    public static Certificate getCertificate(String quiz, Activity caller) throws JSONException {
        Certificate output = new Certificate(quiz);
        AppHelper.FileSystemReturn loadedCert = AppHelper.loadFile("qscore\\", caller.getFilesDir() + "/qscore/" + quiz + ".quiz.json", caller, false, false, false, null, null);
        Log.i("QA", String.valueOf(loadedCert));
        if (loadedCert.equals(AppHelper.FileSystemReturn.FILE_PRESENT)) {
            return (fromJSON(new JSONObject(loadedCert.payload)));
        } else {
            return output;
        }
    }

    public static Certificate fromJSON(JSONObject j) throws JSONException {
        Certificate output = new Certificate(j.getString("quiz"));
        JSONObject userContent = j.getJSONObject("scores");
        Iterator<String> keys = userContent.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            output.scores.put(UUID.fromString(key), userContent.getInt(key));
        }
        return output;
    }

    public AppHelper.FileSystemReturn save(Activity a) throws JSONException {
        return AppHelper.saveFile("/qscore/", this.quizName + ".quiz.json", toJSON().toString(), a, null, true);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject output = new JSONObject();
        output.put("quiz", quizName);
        JSONObject scoresOutput = new JSONObject();

        for (UUID i : scores.keySet()) {
            scoresOutput.put(i.toString(), scores.get(i));
        }

        output.put("scores", scoresOutput);
        return output;
    }

    public Integer getUserScore(User u) {
        if (scores.containsKey(u.getId())) {
            return scores.get(u.getId());
        } else {
            return -1;
        }
    }

    public Certificate setUserScore(User u, int score) {
        if (scores.containsKey(u.getId())) {
            scores.replace(u.getId(), score);
        } else {
            scores.put(u.getId(), score);
        }
        return this;
    }
}

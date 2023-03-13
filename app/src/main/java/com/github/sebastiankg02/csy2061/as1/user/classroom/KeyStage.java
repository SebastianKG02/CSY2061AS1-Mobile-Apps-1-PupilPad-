package com.github.sebastiankg02.csy2061.as1.user.classroom;

import com.github.sebastiankg02.csy2061.as1.R;

public enum KeyStage {
    RECEPTION(0,
            R.string.ks_reception_long,
            R.string.ks_reception_short
    ),
    KEY_STAGE_1(1,
            R.string.ks_one_long,
            R.string.ks_one_short
    ),
    KEY_STAGE_2(2,
            R.string.ks_two_long,
            R.string.ks_two_short
    ),
    KEY_STAGE_3(3,
            R.string.ks_three_long,
            R.string.ks_three_short
    ),
    KEY_STAGE_4(4,
            R.string.ks_four_long,
            R.string.ks_one_short
    ),
    KEY_STAGE_5(5,
            R.string.ks_five_long,
            R.string.ks_five_short
    ),
    UNDERGRADUATE(6,
            R.string.ks_undergrad_long,
            R.string.ks_undergrad_short
    ),
    POSTGRADUATE(7,
            R.string.ks_postgrad_long,
            R.string.ks_postgrad_short
    ),
    DOCTORATE(8,
            R.string.ks_doctor_long,
            R.string.ks_doctor_short
    ),
    GUEST(-1,
            R.string.ks_none_long,
            R.string.ks_none_short
    );

    public static final KeyStage[] all = {
            RECEPTION,
            KEY_STAGE_1,
            KEY_STAGE_2,
            KEY_STAGE_3,
            KEY_STAGE_4,
            KEY_STAGE_5,
            UNDERGRADUATE,
            POSTGRADUATE,
            DOCTORATE,
            GUEST
    };

    private int educationLevel;
    private int educationTag_Long;
    private int educationTag_Short;

    private KeyStage(int level, int tag_Long, int tag_Short){
        this.educationLevel = level;
        this.educationTag_Long = tag_Long;
        this.educationTag_Short = tag_Short;
    };

    public int getEducationLevel() {
        return this.educationLevel;
    }

    public int getLongEducationTag() {
        return this.educationTag_Long;
    }

    public int getShortEducationTag(){
        return this.educationTag_Short;
    }

    public static KeyStage fromEducationLevel(int level){
        for (KeyStage ks:
             all) {
            if(ks.educationLevel == level){
                return ks;
            }
        }
        return null;
    }

}

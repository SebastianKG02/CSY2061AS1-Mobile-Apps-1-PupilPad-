package com.github.sebastiankg02.csy2061.as1.user.classroom;

import com.github.sebastiankg02.csy2061.as1.R;

public enum Class {
    YEAR_0(0,
            R.string.yr_reception,
            KeyStage.RECEPTION
    ),
    YEAR_1(1,
            R.string.yr_year1,
            KeyStage.KEY_STAGE_1
    ),
    YEAR_2(2,
            R.string.yr_year2,
            KeyStage.KEY_STAGE_1
    ),
    YEAR_3(3,
            R.string.yr_year3,
            KeyStage.KEY_STAGE_2
    ),
    YEAR_4(4,
            R.string.yr_year4,
            KeyStage.KEY_STAGE_2
    ),
    YEAR_5(5,
            R.string.yr_year5,
            KeyStage.KEY_STAGE_2
    ),
    YEAR_6(6,
            R.string.yr_year6,
            KeyStage.KEY_STAGE_2
    ),
    YEAR_7(7,
            R.string.yr_year7,
            KeyStage.KEY_STAGE_3
    ),
    YEAR_8(8,
            R.string.yr_year8,
            KeyStage.KEY_STAGE_3
    ),
    YEAR_9(9,
            R.string.yr_year9,
            KeyStage.KEY_STAGE_3
    ),
    YEAR_10(10,
            R.string.yr_year10,
            KeyStage.KEY_STAGE_4
    ),
    YEAR_11(11,
            R.string.yr_year11,
            KeyStage.KEY_STAGE_4
    ),
    YEAR_12(12,
            R.string.yr_year12,
            KeyStage.KEY_STAGE_5
    ),
    YEAR_13(13,
            R.string.yr_year13,
            KeyStage.KEY_STAGE_5
    ),
    UNDERGRAD(14,
            R.string.yr_undergrad,
            KeyStage.UNDERGRADUATE
    ),
    POSTGRAD(15,
            R.string.yr_postgrad,
            KeyStage.POSTGRADUATE
    ),
    DOCTORAL(16,
            R.string.yr_phd,
            KeyStage.DOCTORATE
    );

    private int level;
    private int tag;
    private KeyStage keyStage;

    private Class(int level, int tag, KeyStage stage){
        this.level = level;
        this.tag = tag;
        this.keyStage = stage;
    };

    public int getLevel() {
        return this.level;
    }

    public int getTag(){
        return this.tag;
    }

    public KeyStage getKeyStage(){
        return keyStage;
    }
}

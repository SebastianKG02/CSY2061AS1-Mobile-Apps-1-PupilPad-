package com.github.sebastiankg02.csy2061.as1.user.note;

public enum PupilNoteDisplayType {
    STANDARD_NOTE(0),
    FLASHCARD(1),
    CHECKLIST(2),
    DIARY(3);

    private int typeCode;

    public static final PupilNoteDisplayType[] all = {
            PupilNoteDisplayType.STANDARD_NOTE,
            PupilNoteDisplayType.FLASHCARD,
            PupilNoteDisplayType.CHECKLIST,
            PupilNoteDisplayType.DIARY
    };

    PupilNoteDisplayType(int type){
        this.typeCode = type;
    }

    public int getTypeCode(){
        return this.typeCode;
    }

    public static PupilNoteDisplayType getNoteDisplayType(int type){
        for (PupilNoteDisplayType t:
             all) {
            if(t.typeCode == type){
                return t;
            }
        }
        return null;
    }
}

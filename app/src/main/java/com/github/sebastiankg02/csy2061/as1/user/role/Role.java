package com.github.sebastiankg02.csy2061.as1.user.role;

import com.github.sebastiankg02.csy2061.as1.R;

public enum Role {
    ADMINISTRATOR(3,
            R.string.role_admin_long,
            R.string.role_admin_short
    ),
    MODERATOR(2,
            R.string.role_mod_long,
            R.string.role_mod_short
    ),
    USER(1,
            R.string.role_basic_long,
            R.string.role_basic_short
    ),
    NONE(0,
            R.string.role_none_long,
            R.string.role_none_short
    );

    private int level;
    private int roleID_Long;
    private int roleID_Short;

    private Role(int accessLevel, int roleIDLong, int roleIDShort) {
        this.level = accessLevel;
        this.roleID_Long = roleIDLong;
        this.roleID_Short = roleIDShort;
    }

    ;

    public static Role fromInt(int accessLevel) {
        switch (accessLevel) {
            case 1:
                return Role.USER;
            case 2:
                return Role.MODERATOR;
            case 3:
                return Role.ADMINISTRATOR;
            default:
                return Role.NONE;
        }
    }

    public static int toInt(Role r) {
        return r.level;
    }

    public int getAccessLevel() {
        return level;
    }

    public int getLongRoleID() {
        return roleID_Long;
    }

    public int getShortRoleID() {
        return roleID_Short;
    }
}

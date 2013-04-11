package org.sina4j.domain;

public class SinaUserDTO {

    private String email = null;

    private String password = null;

    private String uniqueid = null;

    private String userid = null;

    private String displayname = null;

    private String userdomain = null;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(final String userid) {
        this.userid = userid;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(final String displayname) {
        this.displayname = displayname;
    }

    public String getUserdomain() {
        return this.userdomain;
    }

    public void setUserdomain(final String userdomain) {
        this.userdomain = userdomain;
    }

    public String getUniqueid() {
        return this.uniqueid;
    }

    public void setUniqueid(final String uniqueid) {
        this.uniqueid = uniqueid;
    }
}

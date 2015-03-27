package infinity.runapp;

/**
 * Created by ADC on 3/6/2015.
 */
public class User {
    private int mID;
    private String mEmail;
    private String mFname;
    private String mLname;

    public User(){

    }

    public User(int id, String email, String fname, String lname)
    {
        mID = id;
        mEmail = email;
        mFname = fname;
        mLname = lname;
    }

    public User(String email, String fname, String lname){
        mEmail = email;
        mFname = fname;
        mLname = lname;
    }

    public String getEmail(){
        return mEmail;
    }

    public void setEmail(String email){
        mEmail = email;
    }

    public String getFname() {
        return mFname;
    }

    public void setFname(String mFname) {
        this.mFname = mFname;
    }

    public String getLname() {
        return mLname;
    }

    public void setLname(String mLname) {
        this.mLname = mLname;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }
}

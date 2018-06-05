package pl.radekbonk.ankietygramatowski;

public class Answer {
    private String localization;
    private String how;
    private String what;
    private long rating;
    private String time;

    public Answer () {}

    public Answer (String localization, String how, String what, long rating, String time) {
        this.localization = localization;
        this.how = how;
        this.what = what;
        this.rating = rating;
        this.time = time;
    }

    public String getLocalization() {return localization;}
    public void setLocalization(String localization) {this.localization = localization;}

    public String getHow() {return how;}
    public void setHow(String how) {this.how = how;}

    public String getWhat() {return what;}
    public void setWhat(String what) {this.what = what;}

    public long getRating() {return rating;}
    public void setRating(long rating) {this.rating=rating;}

    public String getTime() {return time;}
    public void setTime(String what) {this.time = time;}
}

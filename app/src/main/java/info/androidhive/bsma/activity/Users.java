/**
 * Created by Kiuloper on 22/01/2018.
 *
 * No touchies pls, it took me some time for coding this thing!!!
 *
 * this class will hold on to usr info from database for individual use
 */
package info.androidhive.bsma.activity;

public class Users {

    int id;
    String first;
    String last;
    String check;

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getFirst() {
        return first;
    }
    public void setFirst(String first) {
        this.first = first;
    }
    public String getLast() {
        return last;
    }
    public void setLast(String last) {
        this.last = last;
    }
    public String getCheck() {return check;}
    public void setCheck(String check) {this.check = check;}

}
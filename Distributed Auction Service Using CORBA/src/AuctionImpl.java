/**
 * Created by chenguanghe on 3/30/15.
 */

import P3.infoPOA;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;

public class AuctionImpl extends infoPOA{
    private Auction cur = null;
    private Auction pre = null;
    class Auction{
        private long price;
        private String creater;
        private String highest_bidder;
        private String itemname;
        private boolean done = false;
        private Auction(){}

        public Auction(long price, String creater, String highest_bidder, String itemname){
            this.price = price;
            this.creater = creater;
            this.highest_bidder = highest_bidder;
            this.itemname = itemname;
        }
    }
    private ORB orb;

    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    public void shutdown() {
        orb.shutdown(false);
    }

    @Override
    public void view_item(String username, StringHolder c) {
        if (cur == null && pre != null && !pre.done && username.equals(pre.highest_bidder)) {
            c.value = "Congregations!, you won " + pre.itemname;
            pre.done =true;
            return;
        }
        if (cur == null)
            c.value = "There is no current bidding!";
        else {
            if (!cur.highest_bidder.equals(username))
                c.value = "Current Item is: " + cur.itemname + " created by " + cur.creater + " price is " + cur.price + " You are NOT the highest bidder" ;
            else{
                c.value = "Current Item is: " + cur.itemname + " created by " + cur.creater + " price is " + cur.price + " You are the highest bidder";
            }

        }
    }

    @Override
    public void sell(String username, String itemname, int price, StringHolder c) {
        if (cur == null){
            cur = new Auction(price,username,username,itemname);
            c.value = "Your Auction has been created!";
        }
        else
            c.value = "Current Auction is still in bidding!";
    }

    @Override
    public void bid(String username, int price, StringHolder c) {
        if (cur == null){
            c.value = "There is no item in bidding!";
        }
        else{
            if (price <= cur.price)
                c.value = "Your price is less than current price";
            else{
                cur.price = price;
                cur.highest_bidder = username;
                c.value = "Your place has been placed";
            }

        }
    }

    @Override
    public void view_high_bidder(String username, StringHolder name, StringHolder c) {
        if (cur == null){
            name.value = "There is current aution item!";
            c.value = "-1";
        }else {
            if (username.equals(cur.highest_bidder)) {
                name.value = cur.highest_bidder;
                c.value = String.valueOf(cur.price);
            }
            else{
                name.value = "You are NOT the highest bidder";
                c.value = "-1";
            }

        }
    }
    @Override
    public void done(String username, StringHolder output) {
        if (cur == null){
            output.value = "There is no current auction item!";
            return;
        }
        if (username.equals(cur.creater)){
            output.value = "Your item sells to "+cur.highest_bidder;
            pre = cur;
            cur = null;
        }
        else
            output.value = "You are not current item's creater";
    }
}

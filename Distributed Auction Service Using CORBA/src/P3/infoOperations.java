package P3;


/**
* P3/infoOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from auction.idl
* Tuesday, March 31, 2015 2:36:44 PM CDT
*/

public interface infoOperations 
{
  void view_item (String username, org.omg.CORBA.StringHolder c);
  void sell (String username, String itemname, int price, org.omg.CORBA.StringHolder c);
  void bid (String username, int price, org.omg.CORBA.StringHolder c);
  void view_high_bidder (String username, org.omg.CORBA.StringHolder name, org.omg.CORBA.StringHolder c);
  void done (String username, org.omg.CORBA.StringHolder output);
} // interface infoOperations

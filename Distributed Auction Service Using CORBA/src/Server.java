import P3.info;
import P3.infoHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
/**
 * Created by chenguanghe on 3/30/15.
 */
public class Server {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            AuctionImpl auctionImpl = new AuctionImpl();
            auctionImpl.setORB(orb);
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(auctionImpl);
            info href = infoHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "info";
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);
            System.out.println("Server ready and waiting ...");
            orb.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

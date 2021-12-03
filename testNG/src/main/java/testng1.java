import org.testng.annotations.Test ;
public class testng1 {

    @Test(priority=1)
    public void setup(){
        System.out.println("setting up");
    }
    @Test(priority=2)
    public void initialise(){
        System.out.println("initialising");
    }
    @Test(priority=3)
    public void finish(){
        System.out.println("finishing");
    }   
}

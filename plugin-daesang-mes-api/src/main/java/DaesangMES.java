import com.mes.controller.MESApiController;
import com.mes.service.MesApiService;

public class DaesangMES {

    public static void main(String[] args) throws Exception {
        MESApiController controller = new MESApiController(new MesApiService());
        controller.safetyInspections();

//        MESBatchController controller = new MESBatchController();
//        controller.safetyWorkPermitLogic();
    }
}





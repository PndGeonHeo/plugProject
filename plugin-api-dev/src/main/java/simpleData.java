import com.common.develop.Common;
import com.fr.data.AbstractTableData;
import com.fr.general.data.TableDataException;

public class simpleData extends AbstractTableData {


    @Override
    public int getColumnCount() throws TableDataException {
        return 0;
    }

    @Override
    public String getColumnName(int i) throws TableDataException {
        return null;
    }

    @Override
    public int getRowCount() throws TableDataException {
        return 0;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return null;
    }

    public String properties() throws Exception {
        Common common = new Common();
        if(common == null) common = new Common();
        common.getPropertyJson("conf/realtime");
        return "";
    }


}



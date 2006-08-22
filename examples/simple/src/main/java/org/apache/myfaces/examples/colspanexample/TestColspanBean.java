package org.apache.myfaces.examples.colspanexample;

import java.util.ArrayList;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

public class TestColspanBean {
    public DataModel getLines() {
        ArrayList a = new ArrayList();
        a.add(new Line("1-1", "1-2", "1-3", "1-4", "1-5"));
        a.add(new Line("2-1", "2-2", "2-3", "2-4", "2-5"));
        a.add(new Line("3-1", "3-2", "3-3", "3-4", "3-5"));
        a.add(new Line("4-1", "4-2", "4-3", "4-4", "4-5"));
        DataModel testModel = new ListDataModel();
        testModel.setWrappedData(a);
        return testModel;
    }

    public DataModel getCellLines() {
        ArrayList a = new ArrayList();
        a.add(new CellLineBean());
        a.add(new CellLineBean());
        a.add(new CellLineBean());
        a.add(new CellLineBean());
        DataModel testModel = new ListDataModel();
        testModel.setWrappedData(a);
        return testModel;
    }
}

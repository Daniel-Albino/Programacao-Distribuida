package pt.isec.a2020134077.ex14;

import java.io.Serial;
import java.io.Serializable;

public class SendInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int idWorker;
    private int nWorkers;
    private int nIntervals;

    public SendInfo(int idWorker, int nWorkers, int nIntervals) {
        this.idWorker = idWorker;
        this.nWorkers = nWorkers;
        this.nIntervals = nIntervals;
    }

    public int getIdWorker() {
        return idWorker;
    }

    public int getnWorkers() {
        return nWorkers;
    }

    public int getnIntervals() {
        return nIntervals;
    }
}

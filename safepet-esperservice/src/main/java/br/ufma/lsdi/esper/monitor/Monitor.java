package br.ufma.lsdi.esper.monitor;

public interface Monitor {
    String getRuleEpl();
    void update();
    String getRuleName();
    String getRuleId();
}

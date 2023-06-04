package com.samnamja.deboost.api.dto.riot.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManufactureAverageResponseDto {
    private double dtm;
    private double dpm;
    private double kap;
    private double vs;
    private double dbgpm;
    private double csm;
    private double gpm;
    private double dmgp;
    private double vspm;
    private double avgwpm;
    private double avgwcpm;
    private double avgvwpm;

    @Builder
    public ManufactureAverageResponseDto(double dtm, double dpm, double kap, double vs, double dbgpm, double csm, double gpm, double dmgp, double vspm, double avgwpm, double avgwcpm, double avgvwpm) {
        this.dtm = dtm;
        this.dpm = dpm;
        this.kap = kap;
        this.vs = vs;
        this.dbgpm = dbgpm;
        this.csm = csm;
        this.gpm = gpm;
        this.dmgp = dmgp;
        this.vspm = vspm;
        this.avgwpm = avgwpm;
        this.avgwcpm = avgwcpm;
        this.avgvwpm = avgvwpm;
    }

}

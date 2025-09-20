package com.test.mvc.model.commanddto;

import java.math.BigDecimal;

public record ProjectStat(
        Long projectId,
        String projectName,
        long pledgeCount,
        BigDecimal raisedAmount
) {}

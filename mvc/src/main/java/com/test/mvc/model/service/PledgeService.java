package com.test.mvc.model.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PledgeService {

    /** ผลลัพธ์จากการพิจารณา Pledge */
    record Result(boolean approved, String message) {}

    /**
     * ประมวลผลการสนับสนุน (ตรวจ Business Rules แล้วบันทึก/ปฏิเสธ)
     *
     * @param userId        ผู้ใช้ที่ล็อกอิน (nullable = guest)
     * @param projectId     โครงการปลายทาง
     * @param rewardTierId  รางวัลที่เลือก (nullable = ไม่เลือกรางวัล)
     * @param amount        จำนวนเงินที่สนับสนุน (ต้อง > 0)
     * @return Result(approved, message)
     */


    Result pledge(Long userId, Long projectId, Long rewardTierId, BigDecimal amount);

}

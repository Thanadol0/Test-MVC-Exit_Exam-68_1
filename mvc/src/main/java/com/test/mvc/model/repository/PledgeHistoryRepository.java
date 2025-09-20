package com.test.mvc.model.repository;

import com.test.mvc.model.commanddto.ProjectStat;
import com.test.mvc.model.entity.PledgeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PledgeHistoryRepository extends JpaRepository<PledgeHistory, Long> {

    long countByUser_Id(Long userId);
    long countByUser_IdAndStatus(Long userId, String status);

    List<PledgeHistory> findByUser_IdOrderByCreatedAtDesc(Long userId);

    @Query("""
      select new com.test.mvc.model.commanddto.ProjectStat(
          ph.project.id,
          ph.project.name,
          count(ph),
          coalesce(sum(case when ph.status='APPROVED' then ph.amount else 0 end), 0)
      )
      from PledgeHistory ph
      where ph.user.id = :uid
      group by ph.project.id, ph.project.name
      order by ph.project.name
    """)
    List<ProjectStat> summarizePerProject(@Param("uid") Long userId);
}



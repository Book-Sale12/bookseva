package com.bookseva.report.repository;

import com.bookseva.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(Report.ReportStatus status);
    boolean existsByReporterIdAndTargetTypeAndTargetId(Long reporterId, Report.TargetType targetType, Long targetId);
}
